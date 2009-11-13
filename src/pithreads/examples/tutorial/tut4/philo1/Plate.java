package pithreads.examples.tut4.philo1;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Plate extends Task{

	private Integer quantity;
	private PiChannel<Integer> eat;
	
	public Plate(PiChannel<Integer> eat, Integer quantity){
		this.quantity = quantity;
		this.eat = eat;
	}
	
	public PiChannel<Integer> getPiChannel(){
		return eat;
	}
	
	@Override
	public void body() throws RunException {
		log(" plate started");
		while (quantity>0){
			send(eat,1);
			quantity--;
		}
		send(eat,0);
		
	}

}
