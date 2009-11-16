package pithreads.examples.tutorial.tut1.pingpong;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiFactory;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class PingPong extends Task {
	private PiChannel<String> input;
	private PiChannel<String> output;
	private String message;
	
	public PingPong(PiChannel<String> input, PiChannel<String> output, String message) {
		this.input = input;
		this.output = output;
		this.message = message;
	}
	
	@Override
	public void body() throws RunException {
		log("Started");
		while(true) {
			String msg = receive(input);
			log("Received "+msg);
			send(output,message);
		}
	}

	
	public static void main(String... args) {
		PiFactory factory = new PiFactory(false); // no termination detection, no debugging
		PiAgent agent = factory.createAgent();
		
		final PiChannel<String> ping = new PiChannel<String>(agent,"ping");
		PiChannel<String> pong = new PiChannel<String>(agent,"pong");
		
		PiThread pinger = new PiThread(agent,"pinger");
		pinger.assignTask(new PingPong(ping,pong,"<<PING>>"));
		pinger.start();
		
		PiThread ponger = new PiThread(agent,"ponger");
		ponger.assignTask(new PingPong(pong,ping,"<<PONG>>"));
		ponger.start();
		
		PiThread init = new PiThread(agent,"init");
		init.assignTask(new Task() {
			@Override
			public void body() throws RunException {
				send(ping,"<<INIT>>");
			}			
		});
		init.start();
		
		agent.detach();
	}
	
}
