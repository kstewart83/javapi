package pithreads.framework;


public class GlobalPiThread extends PiThread {
	private Thread thread;
	
	/* package */ GlobalPiThread(PiAgent agent, Thread thread) {
		super(agent);
		this.thread = thread;
	}
	
}

