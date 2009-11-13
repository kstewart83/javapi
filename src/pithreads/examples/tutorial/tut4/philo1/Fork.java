package pithreads.examples.tutorial.tut4.philo1;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Fork extends Task{
	
	private PiChannel<Integer> take;
	private Integer n;
	
	public Fork(PiChannel<Integer> take, Integer n){
		this.take = take;
		this.n = n;
	}
	
	public PiChannel<Integer> getPiChannel(){
		return take;
	}
	
	@Override
	public void body() throws RunException {
		log(" fork started");
		while(true){
			send(take, n);//??????
			receive(take);
		}
		
	}

}
