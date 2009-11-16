package pithreads.framework.event;

import pithreads.framework.PiThread;

/**
 * 
 * This is the base class for the hierarchy of
 * event types that are used for the asynchronous communication
 * between the PiThreads and their managing PiAgent.
 * 
 * These classes are not intended to be used at the user level.
 * 
 * @author Frederic Peschanski
 * 
 */
public abstract class ControlEvent {
	private final long time;	
	private PiThread source;
	
	protected ControlEvent(PiThread source) {
		time = System.currentTimeMillis();
		this.source = source;
	}
	
	public PiThread getSource() {
		return source;
	}

	public long getTime() {
		return time;
	}
		
}
