package pithreads.examples.tut5.globalClock;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.PiFactory;

public class DayJob {
	
	private static final int NBWORKERS = 4;
	private static final int TIMETOLIVE = 3;
	
	
	public static void main(String... args) {
	
		PiFactory myFactory = new DebugFactory(false);
		
		PiAgent agent = myFactory.newPiAgent();
		
		final PiChannel<Integer> register = myFactory.newPiChannel(agent,"register"); 
		PiChannel<Integer> tick = myFactory.newPiChannel(agent,"tick");
		PiChannel<Integer> barrier = myFactory.newPiChannel(agent,"barrier");
		
		for(int i = 0;i<NBWORKERS;i++){
			PiThread worker = myFactory.newPiThread(agent, "worker#"+i, new Worker(i,register,tick,barrier,new Task() {
				@Override
				public void body() throws RunException {
					log(" working");
				}}));
			worker.start();
		}
		
		PiThread init = myFactory.newPiThread(agent, "init",new Init(register,tick,barrier,TIMETOLIVE));
		init.start();
		agent.detach();
	}
}
