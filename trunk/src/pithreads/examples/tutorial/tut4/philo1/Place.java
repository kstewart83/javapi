package pithreads.examples.tutorial.tut4.philo1;

import pithreads.framework.PiChannel;

public class Place {

	private Fork fork1;
	private Fork fork2;
	private Plate plate;
	private PiChannel<Boolean> leave;
	
	public Place(Fork fork1,Fork fork2,Plate plate,PiChannel<Boolean> leave){
		this.fork1=fork1;
		this.fork2=fork2;
		this.plate=plate;
		this.leave=leave;
	}
	
	public Fork getFork1(){
		return fork1;
	}
	public Fork getFork2(){
		return fork2;
	}
	public Plate getPlate(){
		return plate;
	}
	public PiChannel<Boolean> getLeave(){
		return leave;
	}
}
