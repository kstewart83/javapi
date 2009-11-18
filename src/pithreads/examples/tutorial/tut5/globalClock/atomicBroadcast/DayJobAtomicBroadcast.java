package pithreads.examples.tut5.globalClock.atomicBroadcast;

import pithreads.framework.PiAgent;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.PiThreadBroadcast;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.PiFactory;

public class DayJobAtomicBroadcast {
	private static final int NBWORKERS = 10;
	private static final int TIMETOLIVE = 10;
	
	
	public static void main(String... args) {
	
		PiFactory myFactory = new DebugFactory(false);
		
		PiAgent agent = myFactory.newPiAgent();
		
		final PiChannelBroadcast<Integer> tick = myFactory.newPiChannelBroadcast(agent,"tick");		

		for(int i = 0;i<NBWORKERS;i++){
			PiThreadBroadcast worker = myFactory.newPiThreadBroadcast(agent, "worker#"+(i+6));
			Worker2AtomicBroadcast task2 =  new Worker2AtomicBroadcast(worker.id,tick,new Task() {
				@Override
				public void body() throws RunException {
					log(" working");
				}},i);
			worker.assign(task2);
			worker.start();
		}
		
		PiThreadBroadcast clock = myFactory.newPiThreadBroadcast(agent, "clock", new Task() {
			@Override
			public void body() throws RunException {
				log(" 1ere tache");
			}});
		
		ClockAtomicBroadcast taskClock=new ClockAtomicBroadcast(tick,0,TIMETOLIVE);
		clock.assign(taskClock);
		clock.start();
		
		//PiThread init = myFactory.newPiThread(agent, "init",new Init(register,tick,barrier,NBWORKERS,TIMETOLIVE,clock));
		//init.start();
		agent.detach();
		
	}
}
