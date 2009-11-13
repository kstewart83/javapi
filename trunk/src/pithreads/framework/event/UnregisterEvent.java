package pithreads.framework.event;

import pithreads.framework.PiThread;

public class UnregisterEvent extends ControlEvent {
	private static final long serialVersionUID = -7461942562084213245L;

	private final PiThread thread;
	
	public UnregisterEvent(PiThread thread) {
		super(thread,Type.UNREGISTER_THREAD);
		this.thread = thread;
	}
	
	public PiThread getPiThread() {
		return thread;
	}
	
	
}
