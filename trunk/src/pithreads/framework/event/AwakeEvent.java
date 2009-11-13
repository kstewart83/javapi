package pithreads.framework.event;

import pithreads.framework.PiThread;

public class AwakeEvent extends ControlEvent {
	private static final long serialVersionUID = 4532723131074142616L;
	private final PiThread thread;
	
	public AwakeEvent(PiThread thread) {
		super(thread);
		this.thread = thread;
	}
	
	public PiThread getPiThread() {
		return thread;
	}
	
	
}
