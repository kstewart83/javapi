package pithreads.framework;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import pithreads.framework.event.NewEvent;
import pithreads.framework.event.ReclaimEvent;
import pithreads.framework.utils.CircularList;

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
	private CircularList<InputCommitment> inCommits;
	private CircularList<OutputCommitment> outCommits;
	private final String name;
	private volatile int id;
	private PiAgent agent;
		
	private AtomicBoolean acquired;
	private Thread owner;	
	private int nbOwners;
	
	/**
	 * Create a new named channel
	 * @param agent the manager agent
	 * @param name the name of the channel (for debugging purpose)
	 */
	/* package */ PiChannel(PiAgent agent, PiThread thread, String name) {
		this.agent = agent;
		id = -1;
		try {
			agent.receiveEvent(new NewEvent(thread,this));
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
		
		inCommits = new CircularList<InputCommitment>();
		outCommits = new CircularList<OutputCommitment>();
		
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
		
	@Override
	protected void finalize() throws Throwable {		
		super.finalize();
		reclaim();
	}
	
	/* package */ void reclaim() throws InterruptedException {
		agent.receiveEvent(new ReclaimEvent(null,this)); // global context
	}
	
	/* package */  void addInputCommitment(InputCommitment input) {
		inCommits.insertBefore(input);
	}
	
	/* package */  void addOutputCommitment(OutputCommitment output) {
		outCommits.insertBefore(output);
	}

	/* package */  Set<PiThread> cleanup() {		
		Set<PiThread> waitingThreads = new HashSet<PiThread>();

		// browse the output commitments
		int remaining = outCommits.getSize();
		while(remaining>0) {
			OutputCommitment outCommit = outCommits.getElement();
			if(outCommit.isValid()) {
				PiThread pithread = outCommit.getThreadReference();
				if(pithread!=null) {
					waitingThreads.add(pithread);
					outCommits.next();
				} else {
					outCommits.remove();
				}
			} else {
				outCommits.remove();
			}
			remaining--;
		}
		
		// browse the input commitments
		remaining = inCommits.getSize();
		while(remaining>0) {
			InputCommitment inCommit = inCommits.getElement();
			if(inCommit.isValid()) {
				PiThread pithread = inCommit.getThreadReference();
				if(pithread!=null) {
					waitingThreads.add(pithread);
					inCommits.next();
				} else {
					inCommits.remove();
				}
			} else {
				inCommits.remove();
			}
			remaining--;
		}
		
		return waitingThreads;
	}
	
	private <U extends Commitment> Commitment searchValidCommitment(CircularList<U> list) {
		//System.out.println("Search valid commitments: "+list);
		int remaining = list.getSize();
		while(remaining>0) {
			Commitment commit = list.getElement();
			if(commit.isValid()) {
				return commit;
			} else {
				list.remove();
				remaining--;
			}
		}
		return null;
	}
	
	/* package */ OutputCommitment searchOutputCommitment() {
		return (OutputCommitment) searchValidCommitment(outCommits);
	}
	
	/* package */  InputCommitment searchInputCommitment() {
		return (InputCommitment) searchValidCommitment(inCommits);
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

