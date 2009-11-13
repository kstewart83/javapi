package pithreads.examples.tut4.philo1;

import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;

public class Philosopher extends Task{

	private Place place;
	private PiChannel<Place> seat;
	Integer stats;
	
	
	public int getStats(){
		return stats;
	}
	
	public Philosopher(PiChannel<Place> seat){
		stats=0;
		this.seat=seat;
	}
	
	public Philosopher() {
		stats=0;
	}

	@Override
	public void body() throws RunException {
		while (true){
			log(" thinks");
			place = receive(seat);
			log("seats "+place.getPlate().toString());
			receive(place.getFork1().getPiChannel());
			log("takes left fork");
			receive(place.getFork2().getPiChannel());
			log("takes right fork");
			Integer nbEatten = receive(place.getPlate().getPiChannel());
			stats += nbEatten;
			log(" ate " + stats + " noddle(s)");
			send(place.getFork2().getPiChannel(),1);
			send(place.getFork1().getPiChannel(),1);
			log(" releases the forks");
			if (nbEatten==0){
				send(place.getLeave(),false);
				log(" .. Nothing to eat ?\n leaves the table (unhappily)");
			}
			else {
				send(place.getLeave(),true);
				log (" leaves the table");
			}
		}
	}

}
