package pithreads.examples.tut5.globalClock;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;


public class Worker extends Task{

	private int id;
	private PiChannel<Integer> register;
	private PiChannel<Integer> tick;
	private PiChannel<Integer> barrier;
	private Task work;
	
	public Worker(int id, PiChannel<Integer> register, PiChannel<Integer> tick, PiChannel<Integer> barrier,Task work){
		this.id=id;
		this.register=register;
		this.tick=tick;
		this.barrier=barrier;
		this.work=work;
		//log(work.toString());
	}

	@Override
	public void body() throws RunException {
		log(" worker "+id+" started" );
		//register
		send(register,id);
		while(true){
			//wait for the next tick
			int count = receive(tick);
			log("Worker "+id+" Tick #"+count+" received");
			work.execute(this.getPiThread());
			send(barrier,id);
		}
	}
}
