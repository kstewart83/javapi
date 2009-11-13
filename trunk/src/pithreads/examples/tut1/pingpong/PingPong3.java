package pithreads.examples.tut1.pingpong;



import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.PiAgentDebug;
import pithreads.framework.debug.PiDataDebug;
import pithreads.framework.debug.PiFactory;
import pithreads.framework.debug.Receiver;
import pithreads.framework.utils.Pair;

public class PingPong3 extends Task {
	private PiChannel<Pair<String, Integer>> input;
	private PiChannel<Pair<String, Integer>> output;
	private String message;

	public PingPong3(PiChannel<Pair<String, Integer>> input,
			PiChannel<Pair<String, Integer>> output, String message) {
		this.input = input;
		this.output = output;
		this.message = message;
	}

	@Override
	public void body() throws RunException {
		log("Started");
		while (true) {
			Pair<String, Integer> msg = receive(input);
			log("Received " + msg.toString());
			if (msg.getSecond() == 0) {
				break;
			}
			send(output,
					new Pair<String, Integer>(message, msg.getSecond() - 1));
		}
	}

	public static void main(String... args) {

		PiFactory myFactory = new DebugFactory(true);
		PiDataDebug sauv = new PiDataDebug("test.txt");

		sauv.start();
		PiAgent agent = myFactory.newPiAgent();
		
        ((PiAgentDebug) agent).bind((Receiver)sauv);
        
        
		final PiChannel<Pair<String, Integer>> ping = myFactory.newPiChannel(
				agent, "ping");
		PiChannel<Pair<String, Integer>> pong = myFactory.newPiChannel(agent,
				"pong");

		PiThread pinger = myFactory.newPiThread(agent, "pinger", new PingPong3(
				ping, pong, "<<PING>>"));
		pinger.start();

		PiThread ponger = myFactory.newPiThread(agent, "ponger", new PingPong3(
				pong, ping, "<<PONG>>"));
		ponger.start();

		PiThread init = myFactory.newPiThread(agent, "init", new Task() {
			@Override
			public void body() throws RunException {
				send(ping, new Pair<String, Integer>("<<INIT>>", 20));
			}
		});
		init.start();
		
		agent.detach();

	}
}
