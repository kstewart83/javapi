package pithreads.framework.broadcast;

import pithreads.framework.utils.Pair;

import java.util.ArrayList;
import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;

public class PiThreadBroadcast extends PiThread{

	private ArrayList<PiChannelBroadcast> registered;
	
	public PiThreadBroadcast(PiAgent agent) {
		super(agent);
		registered=new ArrayList<PiChannelBroadcast>();
	}
	
	public PiThreadBroadcast(PiAgent agent, String name) {
		super(agent, name);
		registered=new ArrayList<PiChannelBroadcast>();
	}

	
	protected final <T> void atomicBroadcast(PiChannelBroadcast<T> channel,T value, int minReceiver) throws RunException {
		log("begin of atomicBroadcast");
		log(channel.getWaitThreads().toString());
		while(channel.getWaitThreads().size()<(minReceiver-1)){
			Integer val = receive(channel.getRegister());
			localRegister(channel, val);
			log("registered "+channel.getWaitThreads().size());
		}
		
		Pair<Integer,Boolean> registering = tryReceive(channel.getRegister());
		
		while(registering.getSecond()){
			localRegister(channel, registering.getFirst());
			log("registered");
			registering = tryReceive(channel.getRegister());
		}

		
		channel.acquire(this);
		int cpt =channel.getWaitThreads().size();
		channel.setNbReceiversLastTurn();
		log(channel.getWaitThreads().toString());
		channel.release(this);
		
		for (int i = 0;i<cpt;i++){
			log("tick#"+i);
			send(channel,value);
			yield();
		}
		
		channel.acquire(this);
		cpt =channel.getWaitThreads().size();
		log(channel.getWaitThreads().toString());
		channel.release(this);
		
		for (int i = 0;i<cpt;i++){
			receive(channel.getBarrier());
		}
		for (int i = 0;i<cpt;i++){
			receive(channel.getUnregister());
			localUnregister(channel, this.id);
			log("unregistered "+i);
		}
		Pair<Integer,Boolean> unregistering = tryReceive(channel.getUnregister());
		while(unregistering.getSecond()){
			localUnregister(channel, unregistering.getFirst());
			log("unregistered");
			unregistering = tryReceive(channel.getUnregister());
		}
		
	}
	
	protected final <T> void broadcast(PiChannelBroadcast<T> channel,T value, int minReceiver) throws RunException {
		while(channel.getWaitThreads().size()<(minReceiver-1)){
			Integer val = receive(channel.getRegister());
			localRegister(channel, val);
			log("registered "+channel.getWaitThreads().size());
		}
		
		Pair<Integer,Boolean> registering = tryReceive(channel.getRegister());
		
		while(registering.getSecond()){
			localRegister(channel, registering.getFirst());
			log("registered");
			registering = tryReceive(channel.getRegister());
		}
		
		Pair<Integer,Boolean> unregistering = tryReceive(channel.getUnregister());
		while(unregistering.getSecond()){
			localUnregister(channel, unregistering.getFirst());
			log("unregistered");
			unregistering = tryReceive(channel.getUnregister());
		}
		
		log(channel.getWaitThreads().toString());
		channel.acquire(this);
		int cpt =channel.getWaitThreads().size();
		channel.setNbReceiversLastTurn();
		channel.release(this);
		
		for (int i = 0;i<cpt;i++){
			log("tick#"+i);
			send(channel,value);
			yield();
		}
		
		channel.acquire(this);
		cpt =channel.getWaitThreads().size();
		channel.release(this);
		
		for (int i = 0;i<cpt;i++){
			log("receive barrier "+i);
			receive(channel.getBarrier());
		}

		unregistering = tryReceive(channel.getUnregister());
		while(unregistering.getSecond()){
			localUnregister(channel, unregistering.getFirst());
			log("unregistered");
			unregistering = tryReceive(channel.getUnregister());
		}
		
	}
	
	protected final <T> T listen(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		if (!registered.contains(channel)){
		send(channel.getRegister(),value);
		}
		//log("registered dans listen");
		
		T res = receive(channel);
		log("tick received");
		send(channel.getBarrier(),value);		
		return res; 
	}
	
	protected <T> void removeChannelFromRegistered(PiChannelBroadcast<T> channel){
		registered.remove(channel);
		//registered.clear();
	}
	
	protected final <T> void localRegister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		channel.acquire(this);
		channel.getWaitThreads().add(value);
		channel.release(this);
	}
	
	
	protected final <T> void localUnregister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		channel.acquire(this);
		log("localUnregister :" + channel.getWaitThreads().remove(0));
		channel.release(this);
	}
	
	protected final <T> void register(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		send(channel.getRegister(),value);
		registered.add(channel);
	}
	
	
	protected final <T> void unregister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		registered.remove(channel);
	}
	
	protected final <T> ArrayList<T> collect(PiChannel<T> channel, int nb) throws RunException {
		ArrayList<T> voteResults = new ArrayList<T>();
		for (int i = 0;i<nb;i++){
			voteResults.add(receive(channel));
		}
		/*Pair<T,Boolean> more = tryReceive(channel);
		while (more.getSecond()){
			voteResults.add(more.getFirst());
			more = tryReceive(channel);
		}*/
		return voteResults;
	}
	
	
	protected final <T> ArrayList<T> broadcastAndCollect(PiChannelBroadcast<PiChannel<T>> channel, int minReceiver,PiChannel<T> sentChannel) throws RunException {
		atomicBroadcast(channel,sentChannel,minReceiver);
		return collect(sentChannel,channel.getNbReceiversLastTurn());
	}

	public ArrayList<PiChannelBroadcast> getRegistered() {
		return registered;
	}

}
