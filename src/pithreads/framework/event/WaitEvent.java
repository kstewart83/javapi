package pithreads.framework.event;

import pithreads.framework.PiThread;
import pithreads.framework.debug.PiThreadDebug;

public class WaitEvent extends ControlEvent {
	private static final long serialVersionUID = -2594160473655841511L;

	private final PiThread thread;
	
	public WaitEvent(PiThread thread) {
		super(thread,Type.WAIT_THREAD);
		this.thread = thread;
	}
	
	public PiThread getPiThread() {
		return thread;
	}
	
	public String toString(){
		return "WAIT_THREAD\nSource :"+((PiThreadDebug)source).toString()+"\nend of WAIT_THREAD\n";
	}
	
}
