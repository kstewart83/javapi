package pithreads.examples.tutorial.tut3.async;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Writer extends Task {
	private PiChannel<String> put;
	private String msg;
	private int count;
	
	public Writer(PiChannel<String> put, String msg, int count) {
		this.put = put;
		this.msg = msg;
		this.count = count;
	}
	
	@Override
	public void body() throws RunException {
		for(int i=1;i<=count;i++) {
			String message = msg+i;
			log("Sends: "+message);
			send(put,message);
		}
	}
}
