package pithreads.framework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
 * 
 * This class provides the basic implementation of a Pi-thread.
 * 
 * A Pi-thread is a plain (Java) Thread with extensions to support the Pi-calculus
 * model of computation.
 * 
 * A Pi-thread is managed by a Pi-agent.
 * It has a set of tasks to execute (see {@link pithreads.framework.Task})
 * (in most cases a Pi-threads executes a single task)
 * 
 * @author Frederic Peschanski
 *
 */
public class PiThread extends Thread {
	protected final PiAgent agent;
	private volatile int id;
	private Deque<Task> tasks;
	protected ImmediateLock immediateLock;
	protected Object receivedValue;
	protected int enabledGuardIndex;
	private AtomicBoolean awakeLock;
	private volatile boolean terminateFlag;
		
	//public should be protected
	public volatile long turn; // need a volatile value so than any change is
	                            // immediately globally visible
	
	private static int genNameSuffix = 0;
	
	/**
	 * Creates a new Pi-thread
	 * @param agent a manager agent
	 * @param name the name of the process (for debugging purpose)
	 */	
	/* package */ PiThread(PiAgent agent, String name) {
		super(name);
		this.agent = agent;
		id = -1;
		turn = 0;
		immediateLock = new ImmediateLock(); // 0 permit and not fair (not needed)
		receivedValue = null;
		tasks = new ArrayDeque<Task>();
		enabledGuardIndex = -1;
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
	/* package */ PiThread(PiAgent agent) {
		this(agent,"thread"+(genNameSuffix++));
	}
	
	public PiAgent getAgent() {
		return agent;
	}
	
	/**
	 * Assigns a task to this Pi-thread. If there is already a running task
	 * the specified task will be executed after the current one. 
	 * @param task the task to execute
	 */
	public synchronized void assign(Task task) {
		tasks.addLast(task);
	}

	
	protected synchronized Task nextTask() {
		return tasks.removeFirst();
	}
	
	/* package */ synchronized void runTask(Task task) throws RunException {
		tasks.addFirst(task); // preemption
		task.execute(this);
		tasks.removeFirst();		
	}
	
	/**
	 * Get the identifier of the Pi-thread. This identifier is ensured unique
	 * for a given manager agent.
	 * @return the id as an integer
	 */
	public int getThreadId() {
		return id;
	}
	
	/**
	 * Check if this Pi-threads is currently blocking
	 * @return true if the Pi-threads is blocking, false if running
	 */
	//public boolean isBlocking() {
		//return immediateLock.isBlocking();
	//}
	
	/**
	 * Get the turn count of this Pi-thread.
	 * This gives an idea of the progression of the execution
	 * (the count starts at 0) but the main use of the counter
	 * is for internal purpose. It drives the invalidation of
	 * process commitments.
	 * @return a long integer representing the count.
	 */
	/* package */ synchronized long getTurn() {
		return turn;
	}

	/**
	 * Increments the local turn
	 */
	protected synchronized void nextTurn() {
		if(turn==Long.MAX_VALUE) {
			// should cleanup all commitments
			// (maybe sending a blocking event to the agent ?)
		}
		turn++;
	}
	
	/**
	 * Used by the Agent to assign the thread id upon registration
	 * @param id The assigned thread identifier
	 */
	/* package */ void assignThreadId(int id) {	
		this.id = id;
	}
	
	protected void sendEvent(ControlEvent event) throws RunException {
		try {
			agent.receiveEvent(event);
		} catch (InterruptedException e) {
			RunException re = new RunException("Cannot send event to agent (Thread interrupted)");
			re.initCause(e);
			throw re;
		}		
	}
	
	/**
	 * Log a message for the agent
	 * @param message
	 */
	protected  void log(String message) throws RunException {
		sendEvent(new LogEvent(this,message));
	}
	
	@Override
	public void run() {
		try {
			while(!tasks.isEmpty()) {
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
	
	protected void receiveValue(Object value) {
		this.receivedValue = value;
	}
	
	/* package */ Object getReceivedValue() {
		return receivedValue;
	}
	
	protected void enableGuardIndex(int index) {
		enabledGuardIndex = index;
	}
	
	protected void waitForCommitment() throws RunException {
		// block until awaken
		try {
			sendEvent(new WaitEvent(this));
			immediateLock.acquire();
			if(terminateFlag) {
				throw new TerminationException();
			}
			sendEvent(new AwakeEvent(this));
		} catch(InterruptedException e) {
			RunException re = new RunException("Cannot acquire lock: thread interrupted");
			re.initCause(e);
			throw re;
		}
	}
	
	/* package */  boolean terminateFromAgent() {
		if(!awakeLock.compareAndSet(false, true)) {
			return false;
		}
		
		if(!immediateLock.isBlocking()) {
			awakeLock.set(false);
			return false;
		}
		turn++;
		terminateFlag = true;
		immediateLock.release();
		return true;
	}
	
	protected  boolean awake(Commitment commit, Object val) throws RunException {
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
		
		if(commit.getTurn()!=getTurn()) {
			// the commitment is too old
			awakeLock.set(false);
			return false;
		}
		
		// perhaps we went a little bit too fast
		// and the source thread is not yet waiting !
		while(!immediateLock.isBlocking()) { Thread.yield(); }

		// for choice need to remember the guard index
		enableGuardIndex(commit.getGuardIndex());
		if(val!=null) {
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
		return true;
	}
	

	@SuppressWarnings("unchecked")
	protected <T> Pair<T,PiThread> receiveOrCommit(PiChannel<T> channel, int guardIndex) throws RunException {
		while(true) { // need to restart asking if got an invalid commitment
			// first look for an output commitment
			OutputCommitment output = channel.searchOutputCommitment();
			if(output==null) {
				channel.addInputCommitment(new InputCommitment(this,channel.getId(),turn,guardIndex));
				return null;
			} else {
				// ok found a commitment
				// awake the sender
				PiThread sender = output.getThreadReference();							
				if(sender!=null && sender.awake(output,null)) {
					nextTurn(); // invalidate the commitments
					return new Pair<T,PiThread>((T) output.getValue(),sender);
				}
			}
		}
	}

	/* package */ <T> T receive(PiChannel<T> channel) throws RunException {
		channel.acquire(this);
		Pair<T,PiThread> valueAndSender = receiveOrCommit(channel,-1);
		if(valueAndSender==null) {
			// ko no value received (commitment)
			channel.release(this);
			waitForCommitment();
			@SuppressWarnings("unchecked")
			T value = (T) receivedValue;
			receivedValue = null;
			return value;
		} else {
			// ok a value has been received (synchronization)
			channel.release(this);
			return valueAndSender.getFirst();
		}
	}
	
	protected  <T> PiThread sendOrCommit(PiChannel<T> channel, T value, int guardIndex) throws RunException {
		while(true) {
			// first look for an output commitment
			InputCommitment input = channel.searchInputCommitment();
			if(input==null) {
				channel.addOutputCommitment(new OutputCommitment(this,channel.getId(),turn,guardIndex,value));
				return null;
			} else {
				// now awake the receiver
				PiThread receiver = input.getThreadReference();
				if(receiver!=null && receiver.awake(input,value)) { // the turn of the receiver changes there
					nextTurn(); // now proceed to the next turn to invalidate the commitments
					return receiver;
				}
			}
		}
		
	}

	/* package */  <T> void send(PiChannel<T> channel, T value) throws RunException {
		channel.acquire(this);
		PiThread receiver = sendOrCommit(channel, value, -1);
		if(receiver==null) {
			// ko no synchronization is possible
			channel.release(this);
			waitForCommitment();
			return;
		} else {
			// ok a synchronization took place
			channel.release(this);
			return;
		}
	}
	
	protected void releaseAllChannels(Set<PiChannel<?>> chans) throws RunException {
		for(PiChannel<?> chan : chans)
			chan.release(this);
	}
	
	/* package */ Guard choose(List<Guard> guards, TreeSet<PiChannel<?>> guardedChannels) throws RunException {
		
		// first phase, need to acquire all the channels
		Set<PiChannel<?>> acquiredChannels = new HashSet<PiChannel<?>>();

		boolean allAcquired = false;
		
		while(!allAcquired) {
			Iterator<PiChannel<?>> iter = guardedChannels.iterator();
			boolean restart = false;
			while(iter.hasNext() && !restart) {
				PiChannel<?> chan = iter.next();
				boolean acquired = chan.tryAcquire(this);
				if(!acquired) { // if cannot acquire
					restart = true; // then restart (so other threads have the opportunity to acquire the 
					                // channels)
					yield(); // force a context switch
				} else {
					acquiredChannels.add(chan);
				}
			}
			if(restart) {  // need to restart acquiring the channel
				releaseAllChannels(acquiredChannels); // restart from scratch
				acquiredChannels.clear();
				yield(); // force a context switch
			} else { // ok we acquired all the channels
				allAcquired = true;
			}
		}
		
		// here we acquired all the channels referenced by this choice
				
		// second phase, try to enable the choice, or deposit some commitment
		for(int i=0;i<guards.size();i++) {
			Guard guard = guards.get(i);
			boolean check = guard.checkGuard();
			if(!check) {
				continue; // skip this guard
			}
			switch(guard.getType()) {
			case INPUT:
				Pair<?,?> valueAndSender = receiveOrCommit(guard.asInputGuard().getChannel(), i);
				if(valueAndSender!=null) {
					receivedValue = valueAndSender.getFirst();
					releaseAllChannels(acquiredChannels);
					return guard;
				}
				break;
			case OUTPUT:
				PiThread receiver = sendOrCommit(guard.asOutputGuard().getChannel(),guard.asOutputGuard().getValue(), i);
				if(receiver!=null) {
					releaseAllChannels(acquiredChannels);
					return guard;
				}
				break;
			case USER: // the check has already been done			
					releaseAllChannels(acquiredChannels);
					return guard;
			default:
					throw new Error("Unsupported guard type: "+guard.getType());
			}
		}
		
		// third phase : block until the choice gets enabled		
		
		// here, all guards are not enabled
		// no we need to block until an input or an output occur
		// (user guards are not evaluated anymore)
		
		// special case : if no acquired channel, the whole choice fails
		if(acquiredChannels.size()==0)
			return null;
		
		releaseAllChannels(acquiredChannels);

		// now block until one commitment gets enabled 
		waitForCommitment();
		
		// when awaken, get the index of the selected guard
		Guard enabledGuard = guards.get(enabledGuardIndex);
		enabledGuardIndex = -1;
		return enabledGuard;		
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		return o==this; // only referential equality
	}

	/* package */ <T> boolean trySend(PiChannel<T> channel,T value) throws RunException{
		boolean inputTook=false;
		channel.acquire(this);
		
		InputCommitment input = channel.searchInputCommitment();
		if(input==null) {
			inputTook=false;
		} else {
			// now awake the receiver
			PiThread receiver = input.getThreadReference();
			if(receiver!=null && receiver.awake(input,value)) { // the turn of the receiver changes there
				nextTurn(); // now proceed to the next turn to invalidate the commitments
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
	
	
	@SuppressWarnings("unchecked")
	/* package */ <T> Pair<T,Boolean> tryReceive(PiChannel<T> channel) throws RunException{
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
				nextTurn(); // invalidate the commitments
				value =  (T) output.getValue();
			}
		}
		
		if(value==null) {
			channel.release(this);
			value = (T) receivedValue;
			receivedValue = null;
			return new Pair<T,Boolean>(null,false);
		} else {
			channel.release(this);
			return new Pair<T,Boolean>(value,true);
		}
	}
	
}
