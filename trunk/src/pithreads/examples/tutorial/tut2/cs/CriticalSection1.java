package pithreads.examples.tutorial.tut2.cs;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiFactory;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class CriticalSection1 {

	public static void main(String... args) {
		
		int nb_procs = 10;
		
		if(args.length>0) {
			nb_procs = Integer.parseInt(args[0]);
		}
		
		PiFactory factory = PiFactory.getFactory();
		PiAgent agent = factory.createAgent();
		
		// create the observer thread
		final PiChannel<Integer> obs = factory.createChannel("obs");
		PiThread observer = factory.createThread("Observer");
		observer.assign(new ObserverTask(obs));
		
		observer.start();

		// create the critical section threads
		final PiChannel<PiChannel<Integer>> lock = factory.createChannel("lock");

		for(int i=1;i<=nb_procs;i++) {
			PiThread cs = factory.createThread("cs"+i);
			cs.assign(new CSTask(i,lock));
			cs.start();
		}

		// create the init process
		PiThread init = factory.createThread("init");
		init.assign(new Task() {
			@Override
			public void body() throws RunException {
				send(lock,obs);
			}
		});
		init.start();
		
		agent.detach();
	}
	
}
