package pithreads.framework.event;

import pithreads.framework.PiThread;

public class UnregisterEvent extends ControlEvent {
	private static final long serialVersionUID = -7461942562084213245L;
	
	public UnregisterEvent(PiThread thread) {
		super(thread);
	}
	
	
}
