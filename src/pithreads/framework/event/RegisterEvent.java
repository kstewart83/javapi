package pithreads.framework.event;

import pithreads.framework.PiThread;

public class RegisterEvent extends ControlEvent {
	private static final long serialVersionUID = 6225082852453496354L;

	private final PiThread thread;
	
	public RegisterEvent(PiThread thread) {
		super(thread,Type.REGISTER_THREAD);
		this.thread = thread;
	}
	
	public PiThread getPiThread() {
		return thread;
	}
	
	
}
