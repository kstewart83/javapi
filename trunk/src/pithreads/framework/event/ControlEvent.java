package pithreads.framework.event;

import java.util.EventObject;

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
public abstract class ControlEvent extends EventObject {
	private static final long serialVersionUID = -4605970832989343301L;
	private final long time;
	
	protected ControlEvent(PiThread source) {
		super(source);
		time = System.currentTimeMillis();
	}
	
	@Override
	public PiThread getSource() {
		return (PiThread) super.getSource();
	}

	public long getTime() {
		return time;
	}
		
}
