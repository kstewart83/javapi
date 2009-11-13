package pithreads.examples.tut5.globalClock.broadcast;

import pithreads.framework.RunException;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class ClockBroadcast extends TaskBroadCast{

	private PiChannelBroadcast<Integer> tick;
	private int nbReg;
	private int ttl;
	
	public ClockBroadcast(PiChannelBroadcast<Integer> tick,int nbReg, int ttl){
		this.tick=tick;
		this.nbReg=nbReg;
		this.ttl=ttl;
	}
	
	
	@Override
	public void body() throws RunException {
		log("clock started");
		
		for(int count=0;count<ttl;count++){
			Broadcast(tick,count,0);
			nbReg=tick.waitThreads.size();
			log("nbReg -> "+nbReg);
			if (nbReg==0) {
				//return;
			}
		}
	}
}
