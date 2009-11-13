package pithreads.framework.event;

import pithreads.framework.PiThread;

public class RegisterEvent extends ControlEvent {
	private static final long serialVersionUID = 6225082852453496354L;

	public RegisterEvent(PiThread thread) {
		super(thread);
	}
	
	
	
}
