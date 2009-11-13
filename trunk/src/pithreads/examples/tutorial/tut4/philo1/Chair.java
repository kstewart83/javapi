package pithreads.examples.tutorial.tut4.philo1;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Chair extends Task{

	private Boolean ok;
	private Place place;
	private PiChannel<Place> seat;
	
	public Chair(Place place,PiChannel<Place> seat){
		this.place=place;
		this.seat=seat;
		ok=new Boolean(true);
	}
	
	@Override
	public void body() throws RunException {
		log(" chair started");
		while(ok){
			send (seat,place);
			ok=receive(place.getLeave());
		}
	}
}
