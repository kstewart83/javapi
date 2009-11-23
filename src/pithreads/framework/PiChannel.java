package pithreads.framework;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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
	private CommitNode<InputCommitment> inCommitNode;
	private CommitNode<OutputCommitment> outCommitNode;
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
		
		inCommitNode = new CommitNode<InputCommitment>();
		outCommitNode = new CommitNode<OutputCommitment>();		
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
		inCommitNode.insertBefore(input);
	}
	
	/* package */  void addOutputCommitment(OutputCommitment output) {
		outCommitNode.insertBefore(output);
	}

	/* package */  Set<PiThread> getWaitingThreads() {
		Set<PiThread> waitingThreads = new HashSet<PiThread>();
		
		// browse the output commitment
		CommitNode<OutputCommitment> ocommit = outCommitNode;
		if(ocommit.getContent()!=null) {
			do {
				if(ocommit.getContent().isValid()) {
					PiThread pithread = ocommit.getContent().getThreadReference();
					if(pithread!=null) {
						waitingThreads.add(pithread);
						ocommit = ocommit.getNext();					
					} else {
						ocommit = ocommit.remove(); // cleanup because invalid
					}
				} else {
					ocommit = ocommit.remove(); // cleanup because invalid
				}
			} while (ocommit!=outCommitNode);
		}
		
		// browse the input commitment
		CommitNode<InputCommitment> icommit = inCommitNode;
		if(icommit.getContent()!=null) {
			do {
				if(icommit.getContent().isValid()) {
					PiThread pithread = icommit.getContent().getThreadReference();
					if(pithread!=null) {
						waitingThreads.add(pithread);
						icommit = icommit.getNext();					
					} else {
						icommit = icommit.remove(); // cleanup because invalid
					}
				} else {
					icommit = icommit.remove(); // cleanup because invalid
				}
			} while(icommit!=inCommitNode);
		}

		return waitingThreads;
	}
	
	/* package */  void cleanup() {
		// browse the output commitment
		CommitNode<OutputCommitment> ocommit = outCommitNode;
		if(ocommit.getContent()!=null) {
			do {
				if(ocommit.getContent().isValid()) {
					PiThread pithread = ocommit.getContent().getThreadReference();
					if(pithread!=null) {						
						ocommit = ocommit.getNext();					
					} else {
						ocommit = ocommit.remove(); // cleanup because invalid
					}
				} else {
					ocommit = ocommit.remove(); // cleanup because invalid
				}
			} while(ocommit!=outCommitNode);
		}
		
		// browse the input commitment
		CommitNode<InputCommitment> icommit = inCommitNode;
		if(icommit.getContent()!=null) {
			do {
				if(icommit.getContent().isValid()) {
					PiThread pithread = icommit.getContent().getThreadReference();
					if(pithread!=null) {
						icommit = icommit.getNext();					
					} else {
						icommit = icommit.remove(); // cleanup because invalid
					}
				} else {
					icommit = icommit.remove(); // cleanup because invalid
				}
			} while(icommit!=inCommitNode);
		}
	}
	
	/* package */ OutputCommitment searchOutputCommitment() {
		CommitNode<OutputCommitment> found = outCommitNode.search();
		if(found==null) {
			return null;
		} else {
			outCommitNode = found;
			return found.getContent();
		}
	}
	
	/* package */  InputCommitment searchInputCommitment() {
		CommitNode<InputCommitment> found = inCommitNode.search();
		if(found==null) {
			return null;
		} else {
			inCommitNode = found;
			return found.getContent();
		}
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

/* package */ class CommitNode<T extends Commitment> {
	private T commit;
	private CommitNode<T> prev;
	private CommitNode<T> next;

	public CommitNode() {
		this.commit = null;
		prev = null;
		next = null;
	}
	
	public boolean isEmpty() {
		return commit==null;
	}
	
	/* package */ CommitNode<T> getPrev() {
		return prev;
	}
	
	/* package */ void setPrev(CommitNode<T> prev) {
		this.prev = prev;
	}

	/* package */ CommitNode<T> getNext() {
		return next;
	}

	/* package */ void setNext(CommitNode<T> next) {
		this.next = next;
	}

	/* package */ T getContent() {
		return commit;
	}
	
	/* package */ void setContent(T commit) {
		this.commit = commit;
	}
	
	/* package */ CommitNode<T> search() {
		if(commit==null) {
			return null;
		} else if(prev==this) {
			if(commit.isValid()) {
				return this;
			} else {
				return remove();
			}
		}
		CommitNode<T> found = this;
		while(true) {
			if(found.getContent().isValid()) {
				return found;
			} else {
				found = found.remove();
			}
		}
	}
	
	/* package */ CommitNode<T> insertBefore(T newCommit) {
		if(commit==null) {
			commit=newCommit;
			prev = this;
			next = this;
			return this;
		} else if(prev==this) {
			if(next!=this) {
				throw new IllegalStateException("Single-node list is not circular");
			}
			CommitNode<T> newNode = new CommitNode<T>();
			setPrev(newNode);
			setNext(newNode);
			newNode.setPrev(this);
			newNode.setNext(this);
			return newNode;
		} else { // general case
			CommitNode<T> newNode = new CommitNode<T>();
			prev.setNext(newNode);
			newNode.setPrev(prev.getPrev());
			setPrev(newNode);
			newNode.setNext(this);
			return newNode;
		}		
	}
	
	/* package */ CommitNode<T> remove() {
		if(commit==null) {
			throw new Error("Cannot remove empty commitment node");
		} else if(prev==this) {
			setContent(null); // last link
			return this;
		} else if(prev==next) { 
			prev.setNext(prev);
			prev.setPrev(prev);
			return prev;
		} else { // general case
			prev.setNext(next);
			next.setPrev(next);
			return next;
		}
	}
}
