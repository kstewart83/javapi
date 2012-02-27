package pithreads.framework;

import java.util.ArrayDeque;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import pithreads.framework.event.AwakeEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.framework.event.LogEvent;
import pithreads.framework.event.RegisterEvent;
import pithreads.framework.event.UnregisterEvent;
import pithreads.framework.event.WaitEvent;
import pithreads.framework.utils.ImmediateLock;
import pithreads.framework.utils.Pair;

/**
 * The default implementation of a pi-thread.
 * 
 * @author Frederic Peschanski
 *
 */
public class DefaultPiThread extends PiThread {
    private volatile int id; /** The pi-thread id  (unique agent-wise). */
  
    private ImmediateLock immediateLock; /** the lock used to make the pi-thread blocking. */
    private AtomicBoolean awakeLock; /** the lightweight lock used for awaking the pi-thread. */
 
    private volatile boolean terminateFlag; /** a flag used in the termination protocol (cf. agent). */
               
    private static int genNameSuffix = 0; /** used for (somewhat dirty) unique name generation. */
    
    /**
     * Creates a new Pi-thread
     * @param agent a manager agent
     * @param name the name of the process (for debugging purpose)
     */     
    /* package */ DefaultPiThread(PiAgent agent, String name) {
         	super(agent,name);
            id = -1;
            immediateLock = new ImmediateLock(); // 0 permit and not fair (not needed)
            awakeLock = new AtomicBoolean(false);
            terminateFlag = false;
            try {
                    agent.receiveEvent(new RegisterEvent(this));
            } catch (InterruptedException e) {
                    Error error = new Error("Thread interrupted: "+e.getMessage());
                    error.initCause(e);
                    throw error;
            }
            while(id==PiAgent.ID_NOT_ASSIGNED) { yield(); } // wait until the id is set
            if(id==PiAgent.ID_ALREADY_REGISTERED)
                    throw new Error("Id already registered");
            if(id==PiAgent.ID_TOO_MANY)
                    throw new Error("Too many threads in agent");
    }
    
    /**
     * Creates a new Pi-thread (default name)
     * @param agent a manager agent
     */
    /* package */ DefaultPiThread(PiAgent agent) {
            this(agent,"thread"+(genNameSuffix++));
    }
    
	/////////////////////////// AGENT-LEVEL OPERATIONS  ///////////////////////////

    @Override
    public int getThreadId() {
		return id;
	}

    
    @Override
    /* package */ final void assignThreadId(int id) {     
		this.id = id;
	}

    @Override
    /* package */ final  boolean terminateFromAgent() {
		if(!awakeLock.compareAndSet(false, true)) {
			return false;
		}

		if(!immediateLock.isBlocking()) {
			awakeLock.set(false);
			return false;
		}
		ValidFlag flag = getValidFlag();
		setValidFlag(null);
		if(flag!=null)
			flag.valid = false;
		terminateFlag = true;
		immediateLock.release();
		return true;
	}

	/////////////////////////// CONTROL OPERATIONS  ///////////////////////////

	@Override
	protected final void waitForCommitment() throws RunException {
		// block until awaken
		try {
			// TODO: only if termination detection is enabled
			sendEvent(new WaitEvent(this));
			immediateLock.acquire();
			if(terminateFlag) {
				throw new TerminationException();
			}
			// TODO: only if termination detection is enabled
			sendEvent(new AwakeEvent(this));
		} catch(InterruptedException e) {
			RunException re = new RunException("Cannot acquire lock: thread interrupted");
			re.initCause(e);
			throw re;
		}
	}

	@Override
	protected final boolean awake(Commitment commit, Object val) throws RunException {
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

		if(!awakeLock.compareAndSet(false, true)) {
			// cannot take the lock, it's too late
			// someone else awoke the thread
			return false;
		}

		// here we acquired the lock

		if(commit.getValidFlag()!=getValidFlag()) {
			// the commitment is too old
			awakeLock.set(false);
			return false;
		}

		// perhaps we went a little bit too fast
		// and the source thread is not yet waiting !
		while(!immediateLock.isBlocking()) { Thread.yield(); }

		// for choice need to remember the guard index
		setEnabledGuardIndex(commit.getGuardIndex());
		// if input, receive the value
		if(val!=null) {
			receiveValue(val);
		}

		// release the lock
		//agent.receiveEvent(new ThreadLogEvent(Thread.currentThread(),"Akawing: "+getName()));
		ValidFlag flag = getValidFlag();
		setValidFlag(null); // cancels the commitments
		flag.valid = false;  // broadcast the invalidation

		// no awake the process
		immediateLock.release();

		// release the awake lock
		awakeLock.set(false);
		return true;
	}

	@Override
	protected final PiThread spawn(String name) {
		return getAgent().getFactory().createThread(name);
	}

	/////////////////////////// RECEPTION PROTOCOL  ///////////////////////////

	@Override
	@SuppressWarnings("unchecked")
	protected final <T> Pair<T,PiThread> receiveOrCommit(PiChannel<T> channel, int guardIndex) throws RunException {
		while(true) { // need to restart asking if got an invalid commitment
			// first look for an output commitment
			OutputCommitment output = channel.searchOutputCommitment();
			if(output==null) {
				if(getValidFlag()!=null) {
					throw new Error("validFlag should be null (please report)");
				}
				else { 
					setValidFlag(new ValidFlag());
				}
				channel.addInputCommitment(new InputCommitment(this,channel.getId(),getValidFlag(),guardIndex));
				return null;
			} else {
				// ok found a commitment
				// awake the sender
				PiThread sender = output.getThreadReference();  
				if(sender!=null && sender.awake(output,null)) ;

				return new Pair<T,PiThread>((T) output.getValue(),sender);
			}
		}
	}
    
	@SuppressWarnings("unchecked")
	protected final <T> Pair<T,Boolean> tryReceive(PiChannel<T> channel) throws RunException {
		T value=null;

		channel.acquire(this);

		OutputCommitment output = channel.searchOutputCommitment();
		if(output==null) {
			value = null;
		} else {
			// ok found a commitment
			// awake the sender
			PiThread sender = output.getThreadReference();                                                  
			if(sender!=null && sender.awake(output,null)) {
				value =  (T) output.getValue();
			}
		}

		if(value==null) {
			channel.release(this);
			value = (T) getReceivedValue();
			receiveValue(null);
			return new Pair<T,Boolean>(null,false);
		} else {
			channel.release(this);
			return new Pair<T,Boolean>(value,true);
		}
	}

	@Override
	protected <T> T syncReceive(PiChannel<T> channel, Pair<T, PiThread> valueAndSender) throws RunException {            
		// awake the sender
		channel.release(this);
		return valueAndSender.getFirst();
	}

    @Override
	protected <T> T blockingReceive(PiChannel<T> channel) throws RunException {
		channel.release(this);
		waitForCommitment();
		@SuppressWarnings("unchecked")
		T value = (T) getReceivedValue();
		receiveValue(null);
		return value;
		
	}

	/////////////////////////// EMISSION PROTOCOL  ///////////////////////////

    @Override
	protected  <T> PiThread sendOrCommit(PiChannel<T> channel, T value, int guardIndex) throws RunException {
		while(true) {
			// first look for an output commitment
			InputCommitment input = channel.searchInputCommitment();
			if(input==null) {
				if(getValidFlag()!=null) {
					throw new Error("validFlag should be null (please report)");
				}
				else
					setValidFlag(new ValidFlag());
				channel.addOutputCommitment(new OutputCommitment(this,channel.getId(),getValidFlag(),guardIndex,value));
				return null;
			} else {
				// ok found a commitment
				// awake the receiver
				PiThread receiver = input.getThreadReference();
				if(receiver!=null && receiver.awake(input,value));
				return receiver;

			}
		}
	}

    @Override
	protected final <T> boolean trySend(PiChannel<T> channel,T value) throws RunException{
		boolean inputTook=false;
		channel.acquire(this);

		InputCommitment input = channel.searchInputCommitment();
		if(input==null) {
			inputTook=false;
		} else {
			// now awake the receiver
			PiThread receiver = input.getThreadReference();
			if(receiver!=null && receiver.awake(input,value)) { // the turn of the receiver changes there
				inputTook = true;
			}
		}

		if(inputTook) {
			channel.release(this);
			waitForCommitment();
			return false;
		} else {
			channel.release(this);
			return true;
		}
	}

	@Override
	protected <T> void syncSend(PiChannel<T> channel, PiThread input,T value) throws RunException {
		channel.release(this);
	}

	@Override    
	protected <T> void blockingSend(PiChannel<T> channel) throws RunException {
		 channel.release(this);
		 waitForCommitment();
		
	}


	/////////////////////////// CHOICE PROTOCOL  ///////////////////////////

	// nothing to implement
	

	/////////////////////////// MAIN LOOP  ///////////////////////////
		
	
	@Override
	public final void run() {
		try {
			while(taskPending()) {
				Task task = nextTask();
				task.execute(this);
			}
		} catch(TerminationException te) {
			try {
				log(te.getMessage());
			} catch (RunException e) {
				Error error = new Error(e.getMessage());
				error.initCause(e);
				throw error;
			}
			//virer tous les commitment 
			return;
		} catch(RunException e) {
			Error error = new Error(e.getMessage());
			error.initCause(e);
			throw error;
		}

		try {
			sendEvent(new UnregisterEvent(this));
		} catch (RunException e) {
			Error err = new Error("PiThreads runtime exception");
			err.initCause(e);
			throw err;
		}

	}




















}
