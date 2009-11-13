package pithreads.framework.utils;


/**
 * 
 * Counting Semaphore implementation
 * (code adapted from "Concurrent Concurrent Programming in Java, 2nd Edition" by Doug Lea)
 * 
 * The difference with the JDK version is that we can test if the semaphore is blocking 
 * or not
 *
 * @author Frederic Peschanski
 * Created 16 mars 2006
 *
 */

public class Semaphore {
	protected long permits; // current number of available permits
	protected boolean isBlocking; // flag if the semaphore is in blocking state
	
	/**
	 * Create a semaphore
	 * @param initialPermits the number of threads that can acquire the semaphore
	 */
	public Semaphore(long initialPermits) { 
		permits = initialPermits;
		isBlocking = false;
	}
		
	/** 
	 * @return the current count for the semaphore (number of enabled threads)
	 */
	public synchronized long getCount() {
		return permits;
	}

	/**
	 * @return true if the semaphore is blocking acquiring threads
	 */
	public synchronized boolean isBlocking() {
		return isBlocking;
	}
	
	/**
	 * test if semaphore count can be decremented (no guarantee)
	 * @return true if may acquire
	 */
	public synchronized boolean mayAcquire() {		
		return permits>0;
	}

	/**
	 * Decrement the semaphore count. Blocks if count is zero.
	 * @throws InterruptedException if current thread has been interrupted
	 */
	public void acquire() throws InterruptedException {
		if (Thread.interrupted()) throw new InterruptedException();
		synchronized(this) {
			try {
				while (permits <= 0) {
					isBlocking = true;
					wait();
				}
				--permits;
				if(permits>=0)
					isBlocking=false;
			}
			catch (InterruptedException ie) {
				notify();
				throw ie;
			}
			//System.err.println("Semaphore "+this+" acquired by thread '"+Thread.currentThread().getName());
		}
	}
	
	/**
	 * non-blocking variant of acquire
	 * @return true if semaphore has been decremented, false otherwise
	 * @throws InterruptedException if current thread has been interrupted
	 * @see acquire
	 */
	public boolean tryAcquire() throws InterruptedException {
		if (Thread.interrupted()) throw new InterruptedException();
		synchronized(this) {
			if(permits<=0)
				return false;		    	
			--permits;
		}
		return true;
	}
	
	/**
	 * timeout variant of acquire
	 * @param msecs attempt timeout in milliseconds 
	 * @return true if semaphore has been decremented, false otherwise
	 * @throws InterruptedException InterruptedException if current thread has been interrupted
	 * @see acquire
	 */
	public boolean attempt(long msecs)throws InterruptedException{
		if (Thread.interrupted()) throw new InterruptedException();
		synchronized(this) {
			if (permits > 0) {     // Same as acquire but messier
				--permits;
				return true;
			}
			else if (msecs <= 0)   // avoid timed wait if not needed
				return false;
			else {
				try {
					long startTime = System.currentTimeMillis();
					long waitTime = msecs;
					
					for (;;) {
						isBlocking = true;
						wait(waitTime);
						isBlocking = false;
						if (permits > 0) {
							--permits;
							return true;
						}
						else {                   // Check for time-out
							long now = System.currentTimeMillis();
							waitTime = msecs - (now - startTime);
							if (waitTime <= 0)    
								return false;
						}
					}
				}
				catch(InterruptedException ie) { 
					notify();
					throw ie;
				}
			}
		}
	}
	
	/**
	 * increment the semaphore count. This does not block.
	 */
	public synchronized void release() {
		++permits;
		//System.err.println("Semaphore "+this+" released by thread '"+Thread.currentThread().getName());
		notify();
	}	
	
}

