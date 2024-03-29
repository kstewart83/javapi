package pithreads.examples.tutorial.tut1.pingpong;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiConfig;
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
		while(true) {
			String msg = receive(input);
			log("Received "+msg);
			send(output,message);
		}
	}

	
	public static void main(String... args) {
		PiConfig config = new PiConfig();
		config.setTerminationDetection(false); // no termination detection (forced option)
		PiFactory factory = PiFactory.getFactory(config);
		PiAgent agent = factory.createAgent("pingpong1");
		
		final PiChannel<String> ping = factory.createChannel("ping");
		PiChannel<String> pong = factory.createChannel("pong");
		
		PiThread pinger = factory.createThread("pinger");
		pinger.assign(new PingPong(ping,pong,"<<PING>>"));
		pinger.start();
		
		PiThread ponger = factory.createThread("ponger");
		ponger.assign(new PingPong(pong,ping,"<<PONG>>"));
		ponger.start();
		
		factory.createThread("init")
			.assign(new Task() {
				@Override
				public void body() throws RunException {
					send(ping,"<<INIT>>");
				}			
			}).start();
		
		agent.detach();
	}
	
}
