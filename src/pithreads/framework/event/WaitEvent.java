package pithreads.framework.event;

import pithreads.framework.PiThread;

public class WaitEvent extends ControlEvent {
	private static final long serialVersionUID = -2594160473655841511L;
	
	public WaitEvent(PiThread thread) {
		super(thread);
	}
		
}
