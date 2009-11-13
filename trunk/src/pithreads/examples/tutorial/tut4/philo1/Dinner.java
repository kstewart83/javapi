package pithreads.examples.tutorial.tut4.philo1;

import java.util.ArrayList;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class Dinner {
	
	private static final int NBRES = 5;
	private static final Integer QUANTITY = 10;
	private static final int NBPHILO = 7;

	public static void main(String... args) {
		PiAgent agent = new PiAgent();
		
		ArrayList<Fork> couverts = new ArrayList<Fork>();
		for(int i =0;i<NBRES;i++){
			PiChannel<Integer> take = new PiChannel<Integer>(agent,"take"+i);
			PiThread fork = new PiThread(agent,"fork"+i);
			Fork oneFork = new Fork(take,i);
			couverts.add(oneFork);
			fork.assignTask(oneFork);
			fork.start();
		}
		
		PiChannel<Place> seat =  new PiChannel<Place>(agent,"seat");
		ArrayList<Plate> plates = new ArrayList<Plate>();
		ArrayList<PiChannel<Place>> pichTab = new ArrayList<PiChannel<Place>>();
		for(int i =0;i<NBRES;i++){
			PiChannel<Integer> eat = new PiChannel<Integer>(agent,"eat"+i);
			PiThread plate = new PiThread(agent,"plate"+i);
			Plate onePlate = new Plate(eat,QUANTITY);
			plates.add(onePlate);
			plate.assignTask(onePlate);
			plate.start();
			
			//PiChannel<Place> seat =  new PiChannel<Place>(agent,"seat"+i);
			PiThread chair = new PiThread(agent,"chair"+i);
			Place place = new Place(couverts.get(i),couverts.get((i+1)%NBRES),
									plates.get(i),new PiChannel<Boolean>(agent,"leave"+i));
			chair.assignTask(new Chair(place,seat));
			pichTab.add(seat);
			chair.start();
		}
		
		for(int i =0;i<NBPHILO;i++){
			PiThread philo = new PiThread(agent,"philo"+i);
			philo.assignTask(new Philosopher(seat));//pichTab.get(i)
			philo.start();
		}
		
		agent.detach();
		
		/* version 1 
		 
		//Fork's creation
		ArrayList<PiThread> couverts = new ArrayList<PiThread>();
		ArrayList<PiChannel<Integer>> takeTab = new ArrayList<PiChannel<Integer>>();
		for(int i =0;i<NBRES;i++){
			takeTab.add(new PiChannel<Integer>(agent,"take"+i));
		}
		for(int i = 0;i<NBRES;i++){
			PiThread fork = new PiThread(agent,"fork"+i);
			couverts.add(fork);
			couverts.get(i).assignTask(new Fork(takeTab.get(i),i));
			couverts.get(i).start();
		}
		//end of fork's creation
		
		//Plate's creation
		ArrayList<PiThread> porcelaine = new ArrayList<PiThread>();
		ArrayList<PiChannel<Integer>> eatTab = new ArrayList<PiChannel<Integer>>();
		for(int i =0;i<NBRES;i++){
			eatTab.add(new PiChannel<Integer>(agent,"eat"+i));
		}
		for(int i = 0;i<NBRES;i++){
			PiThread plate = new PiThread(agent,"plate"+i);
			porcelaine.add(plate);
			porcelaine.get(i).assignTask(new Plate(eatTab.get(i),QUANTITY));
			porcelaine.get(i).start();
		}
		//end of plates creation
		
		
		//Chair's creation
		ArrayList<PiChannel<Place>> pichTab = new ArrayList<PiChannel<Place>>();
		for(int i = 0;i<NBRES;i++){
			
			pichTab.add(new PiChannel<Place>(agent,"seat"+i));
			PiThread chair = new PiThread(agent,"chair"+i);
			
			Place place = new Place(couverts.get(i),couverts.get((i+1)%NBRES),
									porcelaine.get(i),new PiChannel<Boolean>(agent,"leave"+i));
			chair.assignTask(new Chair(place,pichTab.get(i)));
			chair.start();
		}
		//end of chair's creation
		
		//Philosopher's creation
		for(int i = 0;i<NBPHILO;i++){
			PiThread philo = new PiThread(agent,"philo"+i);
			philo.assignTask(new Philosopher(pichTab.get(i)));
			philo.start();
			
		}
		//end of philosopher's creation
		 */
		
		
	}
}
