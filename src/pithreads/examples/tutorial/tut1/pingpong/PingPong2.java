package pithreads.examples.tutorial.tut1.pingpong;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiFactory;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.utils.Pair;

public class PingPong2 extends Task {
	private PiChannel<Pair<String,Integer>> input;
	private PiChannel<Pair<String,Integer>> output;
	private String message;
	
	public PingPong2(PiChannel<Pair<String,Integer>> input, PiChannel<Pair<String,Integer>> output, String message) {
		this.input = input;
		this.output = output;
		this.message = message;
	}
	
	@Override
	public void body() throws RunException {
		log("Started");
		while(true) {
			Pair<String,Integer> msg = receive(input);
			log("Received "+msg.toString());
			if (msg.getSecond()==0){break;}
			send(output,new Pair<String,Integer>(message,msg.getSecond()-1));
		}
	}

	
	public static void main(String... args) {
		PiFactory factory = PiFactory.getFactory(); // default config
		PiAgent agent = factory.createAgent();
		
		final PiChannel<Pair<String,Integer>> ping = factory.createChannel("ping");
		PiChannel<Pair<String,Integer>> pong = factory.createChannel("pong");
		
		PiThread pinger = factory.createThread("pinger");
		pinger.assign(new PingPong2(ping,pong,"<<PING>>"));
		pinger.start();
		
		PiThread ponger = factory.createThread("ponger");
		ponger.assign(new PingPong2(pong,ping,"<<PONG>>"));
		ponger.start();
		
		PiThread init = factory.createThread("init");
		init.assign(new Task() {
			@Override
			public void body() throws RunException {
				send(ping,new Pair<String,Integer>("<<INIT>>", 100));
			}			
		});
		init.start();
		
		agent.detach();
	}
	
}
