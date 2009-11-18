package pithreads.examples.tutorial.tut4.philo1;

import java.util.ArrayList;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiFactory;
import pithreads.framework.PiThread;

public class Dinner {
	
	private static final int NBRES = 5;
	private static final Integer QUANTITY = 10;
	private static final int NBPHILO = 7;

	public static void main(String... args) {
		PiFactory factory = new PiFactory(true);
		PiAgent agent = factory.createAgent();
		
		ArrayList<Fork> couverts = new ArrayList<Fork>();
		for(int i =0;i<NBRES;i++){
			PiChannel<Integer> take = factory.createChannel("take"+i);
			PiThread fork = factory.createThread("fork"+i);
			Fork oneFork = new Fork(take,i);
			couverts.add(oneFork);
			fork.assign(oneFork);
			fork.start();
		}
		
		PiChannel<Place> seat =  factory.createChannel("seat");
		ArrayList<Plate> plates = new ArrayList<Plate>();
		ArrayList<PiChannel<Place>> pichTab = new ArrayList<PiChannel<Place>>();
		for(int i =0;i<NBRES;i++){
			PiChannel<Integer> eat = factory.createChannel("eat"+i);
			PiThread plate = factory.createThread("plate"+i);
			Plate onePlate = new Plate(eat,QUANTITY);
			plates.add(onePlate);
			plate.assign(onePlate);
			plate.start();
			
			//PiChannel<Place> seat =  new PiChannel<Place>(agent,"seat"+i);
			PiThread chair = factory.createThread("chair"+i);
			PiChannel<Boolean>  leave = factory.createChannel("leave"+i);
			Place place = new Place(couverts.get(i),couverts.get((i+1)%NBRES),plates.get(i),leave);
			chair.assign(new Chair(place,seat));
			pichTab.add(seat);
			chair.start();
		}
		
		for(int i =0;i<NBPHILO;i++){
			PiThread philo = factory.createThread("philo"+i);
			philo.assign(new Philosopher(seat));//pichTab.get(i)
			philo.start();
		}
		
		agent.detach();
				
		
	}
}
