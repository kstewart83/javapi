package pithreads.examples.tut5.globalClock.broadcast;

import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.TaskBroadCast;

public class WorkerBroadcast extends TaskBroadCast{

	private int id;
	private PiChannelBroadcast<Integer> tick;
	private Task work;
	
	public WorkerBroadcast(int id, PiChannelBroadcast<Integer> tick,Task work){	
		this.id=id;
		this.tick=tick;
		this.work=work;
		//log(work.toString());
	}

	@Override
	public void body() throws RunException {
		log(" worker "+id+" started" );
		while(true){
			//wait for the next tick
			Integer count = listen(tick,this.id);
			log("Worker "+id+" Tick #"+count+" received");
			work.execute(this.getPiThread());
		}
	}
}
