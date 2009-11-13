package pithreads.framework.debug;


import java.util.logging.Level;
import java.util.logging.Logger;
import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.TerminationException;
import pithreads.framework.commit.Commitment;
import pithreads.framework.commit.InputCommitment;
import pithreads.framework.commit.OutputCommitment;
import pithreads.framework.debug.event.AddInputCommitmentEvent;
import pithreads.framework.debug.event.AddOutputCommitmentEvent;
import pithreads.framework.debug.event.NextTurnEvent;
import pithreads.framework.debug.event.TakeInputCommitmentEvent;
import pithreads.framework.debug.event.TakeOutputCommitmentEvent;
import pithreads.framework.debug.event.ThreadCreateEvent;
import pithreads.framework.event.*;

public class PiThreadDebug extends PiThread {
	
	private PiThreadDebugInfo infos;
	
    public PiThreadDebug(PiAgentDebug agent2) {
        super(agent2);
    }

    public PiThreadDebug(PiAgentDebug agent2, String name) {
        super(agent2, name);
    }
    
    
    @Override
    /*
	 * Public but should be protected in framework and debug packages
	 */
	public  synchronized void nextTurn() {
        if (turn == Long.MAX_VALUE) {
            // should cleanup all commitments
            // (maybe sending a blocking event to the agent ?)
        }
        turn++;
        try {
            sendEvent(new NextTurnEvent(this));
        } catch (InterruptedException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }
        turn++;
    }
    @Override
    public  void log(String message) {
        try {
            sendEvent(new ThreadLogEvent(this, message));
        } catch (InterruptedException ex) {
            Logger.getLogger(PiThreadDebug.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

    @Override
    public void run() {
        try {
        	while(id==PiAgent.ID_NOT_ASSIGNED) { yield(); } // wait until the id is set
    		if(id==PiAgent.ID_ALREADY_REGISTERED)
    			throw new Error("Id already registered");
    		if(id==PiAgent.ID_TOO_MANY)
    			throw new Error("Too many threads in agent");
        	try {
                sendEvent(new ThreadCreateEvent(this));
            } catch (InterruptedException e) {
                Error error = new Error("Thread interrupted: " + e.getMessage());
                error.initCause(e);
                throw error;
            }
            while (!plan.isEmpty()) {
                Task task = nextTask();
                task.execute(this);
            }
        } catch (TerminationException te) {
            log(te.getMessage());
            return;
        } catch (RunException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }

        try {
            agent.receiveEvent(new UnregisterEvent(this));
        } catch (InterruptedException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }

    }
    
    @Override
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void waitForCommitment() throws RunException {
        // block until awaken
        try {
            sendEvent(new WaitEvent(this));
            immediateLock.acquire();
            if (terminateFlag) {
                throw new TerminationException();
            }
            sendEvent(new AwakeEvent(this));
        } catch (InterruptedException e) {
            RunException re = new RunException("Thread interrupted");
            re.initCause(e);
            throw re;
        }
    }

    @Override
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  boolean awake(Commitment commit, Object val) throws RunException {
        // XXX: we need a lock to avoid a situation when 2 threads awake
        // the same thread
        // such a situation can occur in the case of a choice, a typical example is :
        //
        //  [t1] c?(x),P + d?(y),Q   ||  [t2] c!a,R   ||  [t3] d!b,S
        //
        // suppose the leftmost thread [t1] deposit the two commitments  c?,d?
        //
        // now the following may occur:
        // 1) in [t2] c!a is effected, we find the commitment c?
        // 2) context-switch and in [t3] then d!b is effected we find the commitment d?
        // 3) [t3] calls awake(d!b) on [t1]
        // 4) but before effecting the commitment [t2] calls awake(c!a)

        // we need a lock so that only one of the two gets actually effected

        if (!awakeLock.compareAndSet(false, true)) {
            // cannot take the lock, it's too late
            // someone else awoke the thread
            return false;
        }

        // here we acquired the lock

        if (commit.getTurn() != getTurn()) {
            // the commitment is too old
            awakeLock.set(false);
            return false;
        }

        // perhaps we went a little bit too fast
        // and the source thread is not yet waiting !
        while (!immediateLock.isBlocking()) {
            Thread.yield();
        }

        // for choice need to remember the guard index
        enableGuardIndex(commit.getGuardIndex());
        if (val != null) {
            receivedValue = val;
        }

        // if input, receive the value

        // release the lock
        //agent.receiveEvent(new ThreadLogEvent(Thread.currentThread(),"Akawing: "+getName()));
        nextTurn(); // the turn change when awaken

        // no awake the process
        immediateLock.release();

        // release the awake lock
        awakeLock.set(false);
        try {
            sendEvent(new AwakeEvent(this));
        } catch (InterruptedException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }
        return true;
    }

    @Override
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  <T> T receiveOrCommit(PiChannel<T> channel, int guardIndex) throws RunException {
        while (true) { // need to restart asking if got an invalid commitment
            // first look for an output commitment
            OutputCommitment output = channel.searchOutputCommitment();
            if (output == null) {
                channel.addInputCommitment(new InputCommitment(this, channel.getId(), turn, guardIndex));
                try {
                    sendEvent(new AddInputCommitmentEvent(this, channel));
                } catch (InterruptedException e) {
                    Error error = new Error("Thread interrupted: " + e.getMessage());
                    error.initCause(e);
                    throw error;
                }
                return null;
            } else {
                // ok found a commitment
                // awake the sender
                PiThread sender = output.getThreadReference();
                if (sender != null && sender.awake(output, null)) {
                    try {
                        sendEvent(new TakeOutputCommitmentEvent(this, channel, sender));
                    } catch (InterruptedException e) {
                        Error error = new Error("Thread interrupted: " + e.getMessage());
                        error.initCause(e);
                        throw error;
                    }
                    nextTurn(); // invalidate the commitments
                    return (T) output.getValue();
                }
            }
        }
    }

    @Override
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  <T> boolean sendOrCommit(PiChannel<T> channel, T value, int guardIndex) throws RunException {

        while (true) {
            // first look for an output commitment
            InputCommitment input = channel.searchInputCommitment();
            if (input == null) {
                channel.addOutputCommitment(new OutputCommitment(this, channel.getId(), turn, guardIndex, value));
                try {
                    sendEvent(new AddOutputCommitmentEvent(this, channel));
                } catch (InterruptedException e) {
                    Error error = new Error("Thread interrupted: " + e.getMessage());
                    error.initCause(e);
                    throw error;
                }
                return false;
            } else {
                // now awake the receiver
                PiThread receiver = input.getThreadReference();
                if (receiver != null && receiver.awake(input, value)) { // the turn of the receiver changes there

                    try {
                        sendEvent(new TakeInputCommitmentEvent(this, channel, receiver));
                    } catch (InterruptedException e) {
                        Error error = new Error("Thread interrupted: " + e.getMessage());
                        error.initCause(e);
                        throw error;
                    }			// the turn of the receiver changes there
                    nextTurn(); // now proceed to the next turn to invalidate the commitments
                    return true;
                }
            }
        }

    }
    public String toString(){
        infos = new PiThreadDebugInfo(this );
		return infos.toString();
	}
}
