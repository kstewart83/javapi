package pithreads.framework;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


import pithreads.framework.commit.InputCommitment;
import pithreads.framework.commit.OutputCommitment;
import pithreads.framework.event.NewEvent;
import pithreads.framework.event.ReclaimEvent;

/**
 * 
 * @author Frederic Peschanski
 *
 * The implementation of Pi-channels.
 * Two Pi-threads can synchonize/communicate using a Pi-channel.
 * Each Pi-channel is a synchronous communication channel where at most one
 * value is communicated for each synchronization (note that the value can
 * be itself structures : e.g. an array, a graph, etc.)
 *
 * Each channel has a manager Pi-agent. Only Pi-threads managed by the same
 * Pi-agent can use the channel.
 *
 * @param <T> the type of the data passing through the channel
 */
public class PiChannel<T>  implements Comparable<PiChannel<T>> {
	//public should be protected
	public Set<InputCommitment> inCommits;
	//public should be protected
	public Set<OutputCommitment> outCommits;
	//public should be protected
	public int refCount;
	//public should be protected
	public final String name;
	//public should be protected
	public volatile int id;
	//public should be protected
	public PiAgent agent;
	
	//public should be protected
	public AtomicBoolean acquired;
	//public should be protected
	public Thread owner;
	
	//public should be protected
	public int nbOwners;
	
	/**
	 * Create a new named channel
	 * @param agent the manager agent
	 * @param name the name of the channel (for debugging purpose)
	 */
	public PiChannel(PiAgent agent, String name) {
		this.agent = agent;
		id = -1;
		try {
			agent.receiveEvent(new NewEvent(this));
		} catch(InterruptedException e) {
			Error error = new Error("Agent interrupted");
			error.initCause(e);
			throw error;
		}
		while(id==-1) {
			//System.out.println("ID = "+id);
			Thread.yield(); // the agent (in a separate thread) will set the id
		}
		if(id==-2) 
			throw new Error("Too many channels in agent");
		this.agent = agent;
		if(name==null) {
			this.name = "chan"+id;
		} else {
			this.name = name;
		}
		refCount=0;
		inCommits = new HashSet<InputCommitment>();
		outCommits = new HashSet<OutputCommitment>();
		acquired = new AtomicBoolean(false); // not acquired by default
		owner = null; // no owner
		nbOwners = 0;
	}

	/** Invariant check
	 * 
	 */
	public void checkInvariant() throws RunEngineException {
		if(nbOwners<0 || nbOwners>1)
			throw new RunEngineException("Invariant failed: nbowners = "+nbOwners);
		
		// TODO: no matching input/output commitments
	}
	
	/**
	 * Create a new channel (default name)
	 * @param agent 
	 */
	public PiChannel(PiAgent agent) {
		this(agent,null);
	}
	
	/* package */ void assignId(int id) {
		System.out.println("assign id = "+id);
		this.id = id;
	}

	/**
	 * Get the identifier of this channel.
	 * The identifier is unique for the manager agent.
	 * @return the identifier as an integer
	 */
	public int getId() {
		return id;
	}
	
	/* package */ boolean tryAcquire(Thread thread) throws RunException {
		if(acquired.compareAndSet(false, true)==true) {
			// ok we acquired the channel
			owner = thread;
			nbOwners++;
			if(nbOwners!=1)
				throw new RunEngineException("Multiple owners for channel");
			return true;
		}
		return false;
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void acquire(Thread thread) throws RunException {
		while(!acquired.compareAndSet(false, true)) {
			// block until acquired CAREFUL USE !
			Thread.yield(); // XXX: faster ? slower ? ==> Seems a lot faster ! 
		}
		owner=thread;
		nbOwners++;
		if(nbOwners!=1)
			throw new RunEngineException("Multiple owners for channel");
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void release(Thread thread) throws RunException {
		if(thread!=owner)
			throw new RunEngineException("Channel must be released by owner");
		owner = null;
		nbOwners--;
		if(nbOwners!=0)
			throw new RunEngineException("Invalid owners count for channel");
		acquired.set(false);		
	}
	
	/* package */ void reference() {
		if(refCount>=0)
			refCount++;
	}
	
	/* package */ void dereference() throws InterruptedException {
		if(refCount>0)
			refCount--;
		if(refCount==0)
			reclaim();
	}
	
	/* package */ void reclaim() throws InterruptedException {
		agent.receiveEvent(new ReclaimEvent(this));
		// should be released in Java also
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void addInputCommitment(InputCommitment input) {
		inCommits.add(input);
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void addOutputCommitment(OutputCommitment output) {
		outCommits.add(output);
	}

	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  Set<PiThread> getWaitingThreads() {
		Set<PiThread> waitingThreads = new HashSet<PiThread>();
		for(OutputCommitment commit : outCommits) {
			if(commit.isValid()) {
				PiThread pithread = commit.getThreadReference();
				if(pithread!=null)
					waitingThreads.add(pithread);
			}
		}
		for(InputCommitment commit : inCommits) {
			if(commit.isValid()) {
				PiThread pithread = commit.getThreadReference();
				if(pithread!=null)
					waitingThreads.add(pithread);
			}
		}
		return waitingThreads;
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void cleanup() {
		Iterator<OutputCommitment> iter = outCommits.iterator(); 
		while(iter.hasNext()) {
			OutputCommitment output = iter.next();
			if(!output.isValid()) {
				iter.remove(); // invalid commitment
			}
		}
		Iterator<InputCommitment> iter2 = inCommits.iterator(); 
		while(iter2.hasNext()) {
			InputCommitment input = iter2.next();
			if(!input.isValid()) {
				iter2.remove(); // invalid commitment
			}
		}
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  OutputCommitment searchOutputCommitment() {
		Iterator<OutputCommitment> iter = outCommits.iterator(); 
		while(iter.hasNext()) {
			OutputCommitment output = iter.next();
			if(output.isValid()) {
				return output;
			} else {
				iter.remove(); // invalid commitment
			}
		}
		return null; // no output commitment found
	}
	
	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  InputCommitment searchInputCommitment() {
		Iterator<InputCommitment> iter = inCommits.iterator(); 
		while(iter.hasNext()) {
			InputCommitment input = iter.next();
			if(input.isValid()) {
				return input;
			} else {
				iter.remove(); // invalid commitment
			}
		}
		return null; // no input commitment found
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		// need to be consistent with the natural ordering
		// because we use channels in tree sets
		if(o==null) return false;
		if(o==this) return true; // referential equality
		if(o.getClass()!=this.getClass())
			return false;
		return ((PiChannel<?>) o).getId()==getId();
	}

	//@Override 
	public int compareTo(PiChannel<T> o) {
		// we use the (arbitrary but fixed) order on channels
		// to avoid cyclic cycles in locking phases
		// (channels are always acquired using the same ordering)
		if(id<o.id)
			return -1;
		else if(id>o.id)
			return 1; 
			else return 0;
	}
	

	
}
