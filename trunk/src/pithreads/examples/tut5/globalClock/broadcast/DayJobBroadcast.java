package pithreads.examples.tut5.globalClock.broadcast;

import pithreads.framework.PiAgent;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.PiThreadBroadcast;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.PiFactory;

public class DayJobBroadcast {
	
	private static final int NBWORKERS = 10;
	private static final int TIMETOLIVE = 20;
	
	
	public static void main(String... args) {
	
		PiFactory myFactory = new DebugFactory(false);
		
		PiAgent agent = myFactory.newPiAgent();
		
		final PiChannelBroadcast<Integer> tick = myFactory.newPiChannelBroadcast(agent,"tick");

		for(int i = 0;i<NBWORKERS;i++){
			PiThreadBroadcast worker = myFactory.newPiThreadBroadcast(agent, "worker#"+i);
			Worker2Broadcast task2 =  new Worker2Broadcast(i,tick,new Task() {
				@Override
				public void body() throws RunException {
					log(" working");
				}},i);
			worker.assignTask(task2);
			worker.start();
		}
		
		PiThreadBroadcast clock = myFactory.newPiThreadBroadcast(agent, "clock", new Task() {
			@Override
			public void body() throws RunException {
				log(" 1ere tache");
			}});
		
		ClockBroadcast taskClock=new ClockBroadcast(tick,0,TIMETOLIVE);
		clock.assignTask(taskClock);
		clock.start();
		
		agent.detach();
		
	}
}
