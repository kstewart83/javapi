package pithreads.examples.tut3.async;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Reader extends Task {
	private PiChannel<String> take;
	
	public Reader(PiChannel<String> take) {
		this.take = take;
	}
	
	@Override
	public void body() throws RunException {
		while(true) {
			String msg = receive(take);
			log("received: "+msg);
		}
	}
}
