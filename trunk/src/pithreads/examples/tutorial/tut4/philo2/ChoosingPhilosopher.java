package pithreads.examples.tutorial.tut4.philo2;

import pithreads.examples.tutorial.tut4.philo1.Place;
import pithreads.framework.Choice;
import pithreads.framework.InputTask;
import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.ValueMaker;


public class ChoosingPhilosopher extends Task{

	private Place place;
	private PiChannel<Place> seat;
	private PiChannel<Integer> takeFork1;
	private PiChannel<Integer> takeFork2;
	private PiChannel<Integer> first;
	private PiChannel<Integer> second;
	private Integer stats;
	private Boolean aquired;
	
	public int getStats(){
		return stats;
	}
	
	public ChoosingPhilosopher(PiChannel<Place> seat){
		stats=0;
		this.seat=seat;
	}

	
	private Choice makeChoice() {
		aquired=false;
		Choice choice = new Choice();
		takeFork1=place.getFork1().getPiChannel();
		takeFork2=place.getFork2().getPiChannel();
		choice.addInput(takeFork1, new InputTask<Integer>() {
			@Override
			public void body(Integer receivedValue) throws RunException {
				
				receivedValue=1;
				first=takeFork1;
				log("takes left fork");
				second=takeFork2;
				log("waits for right fork");
				
			}
		});
		choice.addInput(takeFork2, new InputTask<Integer>() {
			@Override
			public void body(Integer receivedValue) throws RunException {
			
				receivedValue=1;
				first=takeFork2;
				log("takes right fork");
				second=takeFork1;
				log("waits for left fork");
				
			}
		});
		return choice;
	}
	
	private Choice makeChoice2() {
		Choice choice = new Choice();
		
		choice.addInput(second, new InputTask<Integer>() {
			@Override
			public void body(Integer receivedValue) throws RunException {
				log("takes second fork");
				aquired=true;
				
			}
		});
		
		choice.addOutput(first, new ValueMaker<Integer>() {
			//@Override
			public Integer make() {
				return 1;
			}			
		}, new Task() {

			@Override
			public void body() throws RunException {
				log("releases taken fork");
				
			}
			
		});
		
		return choice;}
	
	@Override
	public void body() throws RunException {
		log("philo started");
		//premier tour
		log(" thinks");
		place = receive(seat);
		log("seats "+place.getPlate().toString());
		
		aquired=false;
		Choice choice1;
		Choice choice2;
		
		while(!aquired) {
			choice1 = makeChoice();
			choose(choice1);
			choice2 = makeChoice2();//
			choose(choice2);
		}
		
		Integer nbEatten = receive(place.getPlate().getPiChannel());
		stats += nbEatten;
		log(" ate " + stats + " noddle(s)");
		send(second,1);
		send(first,1);
		log(" releases the forks");
		if (nbEatten==0){
			send(place.getLeave(),false);
			log(" .. Nothing to eat ?\n leaves the table (unhappily)");
		}
		else {
			send(place.getLeave(),true);
			log (" leaves the table");
		}
		//boucle
		while (true){
			log(" thinks");
			place = receive(seat);
			log("seats "+place.getPlate().toString());
			
			aquired = false;
			while(!aquired){
				choice1 = makeChoice();
				choose(choice1);
				choice2 = makeChoice2();
				choose(choice2);
				}

			nbEatten = receive(place.getPlate().getPiChannel());
			stats += nbEatten;
			log(" ate " + stats + " noddle(s)");
			send(second,1);
			send(first,1);
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

