package pithreads.examples.tut5.globalClock;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;


public class Init extends Task{

	private PiChannel<Integer> register;
	private PiChannel<Integer> tick;
	private PiChannel<Integer> barrier;
	private int ttl;
	
	public Init(PiChannel<Integer> register, PiChannel<Integer> tick, PiChannel<Integer> barrier, int ttl){
		super();
		this.register=register;
		this.tick=tick;
		this.barrier=barrier;
		this.ttl=ttl;
	}
	
	@Override
	public void body() throws RunException {
		int nbReg=0;
		while(tryReceive(register)){
			nbReg++;
		}
		PiThread clock = new PiThread(this.getPiThread().agent, "clock");
		Clock taskClock=new Clock(register,tick,barrier,nbReg,ttl);
		clock.assign(taskClock);
		clock.start();
	}
}
