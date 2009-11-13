package pithreads.framework.broadcast;

import java.util.ArrayList;


import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;

public class PiChannelBroadcast<T> extends PiChannel<T>{
	
	private final PiChannel<Integer> register;
	private PiChannel<Integer> unregister;
	private PiChannel<Integer> barrier;
	private int nbReceiversLastTurn;
	public  ArrayList<Integer> waitThreads;
	
	public PiChannelBroadcast(PiAgent agent) {
		super(agent);
		waitThreads = new ArrayList<Integer>();
		register = new PiChannel<Integer>(agent,"register"); 
		unregister = new PiChannel<Integer>(agent,"unregister"); 
		barrier = new PiChannel<Integer>(agent,"barrier");
	}
	
	
	public PiChannelBroadcast(PiAgent agent, String name) {
		super(agent, name);
		nbReceiversLastTurn = -1;
		waitThreads = new ArrayList<Integer>();
		register = new PiChannel<Integer>(agent,"register");
		unregister = new PiChannel<Integer>(agent,"unregister");
		barrier = new PiChannel<Integer>(agent,"barrier");
	}

	public void register(Integer id){
		waitThreads.add(id);
	}
	
	public void setNbReceiversLastTurn(){
		nbReceiversLastTurn = waitThreads.size();
	}
	public int getNbReceiversLastTurn(){
		return nbReceiversLastTurn;
	}
	public void unregister(int id){
		waitThreads.remove(id);
	}
	
	public PiChannel<Integer> getRegister(){
		return register;
	}
	
	public ArrayList<Integer> getWaitThreads(){
		return waitThreads;
	}
	
	public PiChannel<Integer> getUnregister(){
		return unregister;
	}
	public PiChannel<Integer> getBarrier(){
		return barrier;
	}
}
