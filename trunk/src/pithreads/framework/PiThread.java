package pithreads.framework;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pithreads.framework.event.ControlEvent;
import pithreads.framework.event.LogEvent;
import pithreads.framework.utils.Pair;

/**
 * 
 * This class provides the basis for implementations of Pi-threads.
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
public abstract class PiThread extends Thread {
    private final PiAgent agent;  /** The agent managing the pi-thread. */
	private Deque<Task> tasks; /** The queue of tasks to execute */
	private int enabledGuardIndex; /** The last selected guard index (cf. choice protocol). */
	private Object receivedValue; /** The received value (cf. reception protocol). */
	private ValidFlag validFlag; /** the object used for the lazy invalidation of outdated commitments. */
	
	/** Allocate a named pi-thread.
	 * @param name the name is mostly used for debugging
	 */
	protected PiThread(PiAgent agent, String name) {
		super(name);
		this.agent = agent;
		tasks = new ArrayDeque<Task>();
		enabledGuardIndex = -1;
		receivedValue = null;
		validFlag = null;
	}

	/////////////////////////// AGENT-LEVEL OPERATIONS  ///////////////////////////

	/** Get the agent managing this thread */
	public final PiAgent getAgent() {
		return agent;
	}

	/**
	 * Get the identifier of the Pi-thread. This identifier is ensured unique
	 * for a given manager agent.
	 * @return the id as an integer
	 */
	public abstract int getThreadId();

	/** Assign an agent-specific thread id (cf. ID assignment protocol) */
	/* package */ abstract void assignThreadId(int id);

	/** Send a control-level event (for tracing, etc.). */
	protected final void sendEvent(ControlEvent event) throws RunException {
		try {
			agent.receiveEvent(event);
		} catch (InterruptedException e) {
			RunException re = new RunException("Cannot send event to agent (Thread interrupted)");
			re.initCause(e);
			throw re;
		}               
	}

	/** Log a message for the agent */
	protected final void log(String message) throws RunException {
		sendEvent(new LogEvent(this,message));
	}

	/** Terminate this pi-thread preemptively as ordered by the managing agent.
	 * (cf. termination detection).
	 */
	/* package */ abstract boolean terminateFromAgent();


	/////////////////////////// TASK MANAGEMENT ///////////////////////////

	/**
	 * Assigns a task to this Pi-thread. If there is already a running task
	 * the specified task will be executed after the current one. 
	 * @param task the task to execute
	 */
	public final synchronized PiThread assign(Task task) {
		tasks.addLast(task);
		return this;
	}
	
	/** Check if there is a task pending. */
	protected final synchronized boolean taskPending() {
		return !tasks.isEmpty();
	}

	/** Get the next task to execute. */
	protected final synchronized Task nextTask() {
		return tasks.removeFirst();
	}

	/** Preempt the thread to execute the specified task. */
	/* package */ synchronized final void runTask(Task task) throws RunException {
		tasks.addFirst(task); // preemption
		task.execute(this);
		tasks.removeFirst();		
	}


	/////////////////////////// CONTROL OPERATIONS  ///////////////////////////

	/** The pi-thread is waiting for synchronization partners. */
	protected abstract void waitForCommitment() throws RunException;


	/** Get the valid flag for the lazy invalidation of outdated commitments. */
	protected final ValidFlag getValidFlag() {
		return validFlag;
	}
	
	/** Set the valid flag. */
	protected final void setValidFlag(ValidFlag flag) {
		validFlag = flag;
	}

	/** Awake this pi-thread for the specified commitment (and optional transmitted value). */
	protected abstract boolean awake(Commitment commit, Object val) throws RunException;

	/** Acquire the specified channel. */
	protected final void acquire(PiChannel<?> chan) throws RunException{
		chan.acquire(this);
	}

	/////////////////////////// RECEPTION PROTOCOL  ///////////////////////////

	/**
	 * Reception protocol. Either a partner output commitment is available and the reception is
	 * performed. Otherwise an input commitment is deposited.
	 * @param channel the channel to receive on.
	 * @param guardIndex the branch index for an input guard in a choice (cf. choice protocol).
	 * @return the value received when available, otherwise null.
	 * @throws RunException if a thread-related fault occurs (e.g. interrupt).
	 */
	protected abstract <T> Pair<T,PiThread> receiveOrCommit(PiChannel<T> channel, int branchIndex) throws RunException;

	/**
	 * Receive a value from the specified channel. This call is blocking is the pi-thread cannot synchronize.
	 */
	protected final <T> T receive(PiChannel<T> channel) throws RunException {
		acquire(channel);
		Pair<T,PiThread> valueAndSender = receiveOrCommit(channel,-1);
		if(valueAndSender==null) {
			// ko no value received (commitment)
			return blockingReceive(channel);
		} else {
			// ok a value has been received (synchronization)
			return syncReceive(channel,valueAndSender);
		}
	}

	/** Non-blocking variant of receive. 
	 * @return a pair (v,flag) with v the received value and flag is true if a value has been received,
	 * otherwise the value v is unspecified.
	 */
	protected abstract <T> Pair<T,Boolean> tryReceive(PiChannel<T> channel) throws RunException;

	/** Perform a synchronization (reception protocol). */
	protected abstract <T> T syncReceive(PiChannel<T> channel,Pair<T, PiThread> valueAndSender) throws RunException;

	/** Block until synchronization (reception protocol). */
	protected abstract <T> T blockingReceive(PiChannel<T> channel) throws RunException;

	/** Receive a value (reception protocol). */
	protected final void receiveValue(Object value) {
		receivedValue = value;
	}

	/** Get the received value (reception protocol). */
	/* package */ final Object getReceivedValue() {
		return receivedValue;
	}

	/////////////////////////// EMISSION PROTOCOL  ///////////////////////////

	/**
	 * Emission protocol. Either a partner input commitment is available and the emission is
	 * performed. Otherwise an output commitment is deposited.
	 * @param channel the channel to emit on.
	 * @param value the value to emit.
	 * @param guardIndex the branch index for an output guard in a choice (cf. choice protocol).
	 * @return the receiver pi-thread, if the emission is possible, null otherwise.
	 * @throws RunException if a thread-related fault occurs (e.g. interrupt).
	 */
	protected abstract <T> PiThread sendOrCommit(PiChannel<T> channel, T value, int guardIndex) throws RunException;

	/** Send a value on the specified channel. This call is blocking is the pi-thread cannot synchronize. */
	protected final <T> void send(PiChannel<T> channel, T value) throws RunException {
		acquire(channel);
		if(getValidFlag()!=null) {
			throw new Error("Valid flag should be null (please report)");
		}

		PiThread receiver = sendOrCommit(channel, value, -1);


		if(receiver==null) {
			if(getValidFlag()==null) {
				throw new Error("Valid flag should not be null (please report)");
			}

			// ko no synchronization is possible
			blockingSend(channel);

			if(getValidFlag()!=null) {
				throw new Error("Valid flag should be null (please report)");
			}

			return;
		} else {
			syncSend(channel,receiver,value);

			return;
		}
	}

	/** Non-blocking variant of send (emission protocol).
	 * @return true if the emission has been performed, false otherwise
	 */
	protected  abstract <T> boolean trySend(PiChannel<T> channel,T value) throws RunException;


	/** Perform an synchronization (emission protocol). */
	protected abstract <T> void syncSend(PiChannel<T> channel,PiThread receiver,T value) throws RunException;

	/** Block until synchronization (emission protocol). */
	protected abstract <T> void blockingSend(PiChannel<T> channel) throws RunException;

	/////////////////////////// CHOICE PROTOCOL  ///////////////////////////

	/**
	 * Choice protocol. Try a list of guards and select for execution the first enabled guard.
	 * This call is blocking until at least one of the guards becomes enabled.
	 * @param guardedChannels the set of the channels referenced by the guards.
	 * @return the selected guard.
	 * @throws RunExceptionif a thread-related fault occurs (e.g. interrupt).
	 */
	/* package */ final Guard choose(List<Guard> guards, TreeSet<PiChannel<?>> guardedChannels) throws RunException {

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
				Pair<Object,PiThread> valueAndSender = receiveOrCommit(guard.asInputGuard().getChannel(), i);
				if(valueAndSender!=null) {

					syncReceive(guard.asInputGuard().getChannel(),valueAndSender);
					acquiredChannels.remove(guard.asInputGuard().getChannel());
					releaseAllChannels(acquiredChannels);
					return guard;
				}
				break;
			case OUTPUT:
				PiThread receiver = sendOrCommit(guard.asOutputGuard().getChannel(),guard.asOutputGuard().getValue(), i);
				if(receiver!=null) {
					syncReceive(guard.asOutputGuard().getChannel(), new Pair<Object,PiThread>(guard.asOutputGuard().getValue(),receiver));
					acquiredChannels.remove(guard.asOutputGuard().getChannel());
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
		if(enabledGuardIndex==-1) {
			throw new Error("Invalid guard index -1 (please report)");
		}
		Guard enabledGuard = guards.get(enabledGuardIndex);
		setEnabledGuardIndex(-1);
		return enabledGuard;            
	}
	
	/** set the enabled guard index. */
	protected final void setEnabledGuardIndex(int index) {
		enabledGuardIndex = index;
	}

	/** Release all channels of the specified set. */
	protected final void releaseAllChannels(Set<PiChannel<?>> chans) throws RunException {
		for(PiChannel<?> chan : chans)
			chan.release(this);
	}

	/////////////////////////// HASHING  ///////////////////////////

	@Override
	public final int hashCode() {
		return getName().hashCode();
	}

	@Override
	public final boolean equals(Object o) {
		return o==this; // only referential equality
	}

}



