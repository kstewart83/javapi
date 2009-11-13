package pithreads.examples.tut5.globalClock.atomicBroadcast;

import pithreads.framework.RunException;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class ClockAtomicBroadcast extends TaskBroadCast{

	private PiChannelBroadcast<Integer> tick;
	private int nbReg;
	private int ttl;
	
	public ClockAtomicBroadcast(PiChannelBroadcast<Integer> tick,int nbReg, int ttl){
		this.tick=tick;
		this.nbReg=nbReg;
		this.ttl=ttl;
	}
	
	
	@Override
	public void body() throws RunException {
		log("clock started");
		
		for(int count=0;count<ttl;count++){
			atomicBroadcast(tick,count,10);
			nbReg=tick.waitThreads.size();
		}
	}
}
