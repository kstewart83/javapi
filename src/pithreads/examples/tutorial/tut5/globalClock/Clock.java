package pithreads.examples.tut5.globalClock;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Clock extends Task{

	private PiChannel<Integer> register;
	private PiChannel<Integer> tick;
	private PiChannel<Integer> barrier;
	private int nbReg;
	private int ttl;
	
	public Clock(PiChannel<Integer> register, PiChannel<Integer> tick, PiChannel<Integer> barrier,int nbReg, int ttl){
		this.register=register;
		this.tick=tick;
		this.barrier=barrier;
		this.nbReg=nbReg;
		this.ttl=ttl;
	}
	
	
	@Override
	public void body() throws RunException {
		for(int count=0;count<ttl;count++){
			if (nbReg==0) {
				return;
			}
			//emit the tick
			for(int i =0;i<nbReg;i++){
				send(tick,count);
			}
			//synchronization barrier
			for(int i=0;i<nbReg;i++){
				int pid=receive(barrier);
				log("worker "+pid+" synchronized");
			}
			// TODO
			while(tryReceive(register)){
				//if(tryReceive(register)){
				nbReg++;
			}
		}
	}
}
