package pithreads.framework.event;

import pithreads.framework.PiThread;
import pithreads.framework.debug.PiThreadDebug;

public class AwakeEvent extends ControlEvent {
	private static final long serialVersionUID = 4532723131074142616L;
	private final PiThread thread;
	
	public AwakeEvent(PiThread thread) {
		super(thread,Type.AWAKE_THREAD);
		this.thread = thread;
	}
	
	public PiThread getPiThread() {
		return thread;
	}
	
	public String toString(){
		return "AWAKE_THREAD\nSource :"+((PiThreadDebug)source).toString()+"\nend of AWAKE_THREAD\n";
	}
	
}
