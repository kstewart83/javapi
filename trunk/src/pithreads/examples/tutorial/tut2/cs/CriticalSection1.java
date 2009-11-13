package pithreads.examples.tutorial.tut2.cs;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class CriticalSection1 {

	public static void main(String... args) {
		
		int nb_procs = 10;
		
		if(args.length>0) {
			nb_procs = Integer.parseInt(args[0]);
		}
		
		PiAgent agent = new PiAgent();
		
		// create the observer thread
		final PiChannel<Integer> obs = new PiChannel<Integer>(agent,"obs");
		PiThread observer = new PiThread(agent,"Observer");
		observer.assignTask(new ObserverTask(obs));
		
		observer.start();

		// create the critical section threads
		final PiChannel<PiChannel<Integer>> lock = new PiChannel<PiChannel<Integer>>(agent,"lock");

		for(int i=1;i<=nb_procs;i++) {
			PiThread cs = new PiThread(agent,"cs"+i);
			cs.assignTask(new CSTask(i,lock));
			cs.start();
		}

		// create the init process
		PiThread init = new PiThread(agent,"init");
		init.assignTask(new Task() {
			@Override
			public void body() throws RunException {
				send(lock,obs);
			}
		});
		init.start();
		
		agent.detach();
	}
	
}
