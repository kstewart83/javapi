package pithreads.examples.cs;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class CSTask extends Task {
	private PiChannel<PiChannel<Integer>> lock;
	private int n;
	
	public CSTask(int n, PiChannel<PiChannel<Integer>> lock) {
		this.lock = lock;
		this.n = n;
	}

	@Override
	public void body() throws RunException {
		log("Starting");
		PiChannel<Integer> fbk = receive(lock);
		log("Received: "+fbk);
		log("Entering critical section");
		/* CRITICAL */
		log("Sending: "+n);
		send(fbk,n);
		/* SECTION */
		log("Leaving critical section");
		send(lock,fbk);
		/* then dies */		
	}
	
}
