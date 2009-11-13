package pithreads.examples.tutorial.tut2.cs;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class ObserverTask extends Task {
	private PiChannel<Integer> obs;
	private int count;
	
	public ObserverTask(PiChannel<Integer> obs) {
		this.obs = obs;
		count = 0;
	}

	@Override
	public void body() throws RunException {
		log("Starting");
		while(true) {
			log("Wait to receive");
			Integer nobj = receive(obs);
			log("Lock taken by : "+nobj+" (count="+count+")");
			count++;		
		}
	}
	
}
