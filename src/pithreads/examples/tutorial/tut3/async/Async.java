package pithreads.examples.tutorial.tut3.async;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiFactory;
import pithreads.framework.PiThread;

public class Async {

	
	public static void main(String... args) {
		// Parse the command-line
		
		int NWRITERS = 100;
		if(args.length>=1) {
			try {
				NWRITERS = Integer.parseInt(args[0]);
			} catch(NumberFormatException e) {
				// do things
			}
		}
		
		int NWRITES = 10;
		if(args.length>=2) {
			try {
				NWRITES = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				// do things
			}
		}

		int NREADERS = 50;
		if(args.length>=3) {
			try {
				NREADERS = Integer.parseInt(args[2]);
			} catch(NumberFormatException e) {
				// do things
			}
		}

		int CAPACITY = 25;
		if(args.length>=4) {
			try {
				CAPACITY = Integer.parseInt(args[3]);
			} catch(NumberFormatException e) {
				// do things
			}
		}
		
		// Create the agent
		PiFactory factory = new PiFactory(true);
		PiAgent agent = factory.createAgent("async");
		
		// Create the channels
		PiChannel<String> put = factory.createChannel("put");
		PiChannel<String> take = factory.createChannel("take");
		
		// spawn the threads
		PiThread msgQueue = factory.createThread("msgQueue");
		msgQueue.assignTask(new MsgQueue<String>(put,take,CAPACITY));
		msgQueue.start();
		
		for(int i=1;i<=NWRITERS;i++) {
			PiThread writer = factory.createThread("writer"+i);
			writer.assignTask(new Writer(put,"<BEEP>",NWRITES));
			writer.start();
		}
		
		for(int i=1;i<=NREADERS;i++) {
			PiThread reader = factory.createThread("reader"+i);
			reader.assignTask(new Reader(take));
			reader.start();
		}
		
		agent.detach();
	}
}
