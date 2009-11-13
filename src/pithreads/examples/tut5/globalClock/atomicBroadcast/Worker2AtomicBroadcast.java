package pithreads.examples.tut5.globalClock.atomicBroadcast;

import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class Worker2AtomicBroadcast extends TaskBroadCast{

	private int id;
	private int ttr;
	private PiChannelBroadcast<Integer> tick;
	private Task work;
	
	public Worker2AtomicBroadcast(int id, PiChannelBroadcast<Integer> tick,Task work,int ttr){	
		this.id=id;
		this.ttr=ttr;
		this.tick=tick;
		this.work=work;
	}

	@Override
	public void body() throws RunException {
		log(" worker "+id+" started" );
		while(true){
			//wait for the next tick
			Integer count = listen(tick,this.id);
			log("Worker "+id+" Tick #"+count+" received");
			//if(count==ttr){return;}
			work.execute(this.getPiThread());
		}
	}
}
