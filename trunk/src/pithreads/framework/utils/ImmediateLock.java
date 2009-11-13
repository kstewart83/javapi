package pithreads.framework.utils;


/**
 * 
 * Immediate locking 
 * (same as Semaphore(0))
 *
 * @author Frederic Peschanski
 * Created 16 mars 2006
 *
 */

public class ImmediateLock extends Semaphore {

	/**
	 * Immediate locking constructor
	 */
	public ImmediateLock() {
		super(0);	
	}
		
}
