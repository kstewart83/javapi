package pithreads.framework.broadcast;

import java.util.ArrayList;

import pithreads.framework.Choice;
import pithreads.framework.InputTask;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.ValueMaker;

public abstract class TaskBroadCast extends Task{

	private boolean unregistered;
	private boolean manuallyRegistered;
	
	public TaskBroadCast(){
		super();
	}	

	protected final<T> void atomicBroadcast(PiChannelBroadcast<T> channel, T value, int minReceiver) throws RunException {
		((PiThreadBroadcast)this.getPiThread()).atomicBroadcast(channel,value,minReceiver);
	}
	
	protected final<T> void Broadcast(PiChannelBroadcast<T> channel, T value, int minReceiver) throws RunException {
		((PiThreadBroadcast)this.getPiThread()).broadcast(channel,value,minReceiver);
	}
	
	protected final <T> T listen(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		T res = ((PiThreadBroadcast)this.getPiThread()).listen(channel, value);
		if (!manuallyRegistered){
		Choice choice = makeChoice(channel,res);
		choose(choice);
		}
		return res;
	}

	protected final <T> void localRegister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		((PiThreadBroadcast)this.getPiThread()).localRegister(channel, value);
	}
	protected final <T> void localUnregister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		((PiThreadBroadcast)this.getPiThread()).localUnregister(channel, value);
	}
	protected final <T> void register(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		((PiThreadBroadcast)this.getPiThread()).register(channel, value);
		manuallyRegistered = true;
	}
	protected final <T> void unregister(PiChannelBroadcast<T> channel, Integer value) throws RunException {
		unregistered = false;
		int cpt = 0;
		while(!unregistered){
		log("waiting for next tick");
		T res =((PiThreadBroadcast)this.getPiThread()).listen(channel, value);
		Choice choice = makeChoice(channel,res);
		choose(choice);
		if (!unregistered){
			send(channel.getBarrier(),0);
		}
		cpt ++;
		log("compteur de choice ////////////////////////// " +cpt);
		}
		//localUnregister(channel, value);
	}
	protected final <T> ArrayList<T> collect(PiChannel<T> channel, int nb) throws RunException {
		return ((PiThreadBroadcast)this.getPiThread()).collect(channel,nb);
	}
	
	protected final <T> ArrayList<T> broadcastAndCollect(PiChannelBroadcast<PiChannel<T>> channel, int minReceiver,PiChannel<T> sentChannel) throws RunException {
		return ((PiThreadBroadcast)this.getPiThread()).broadcastAndCollect(channel,minReceiver,sentChannel);
	}
	
	private <T> Choice makeChoice(final PiChannelBroadcast<T> channel, T value) {
		final PiThreadBroadcast thread = ((PiThreadBroadcast)this.getPiThread());
		Choice choice = new Choice();
		
		choice.addInput((PiChannel<T>)channel, new InputTask<T>() {
			@Override
			public void body(T receivedValue) {
				log("compteur de choice failed ////////////////////////// failed");					
			}
		});
		
		choice.addOutput(channel.getUnregister(), new ValueMaker<Integer>() {
			//@Override
			public Integer make() {
				return ((PiThread)thread).id;
			}			
		}, new Task() {

			@Override
			public void body() throws RunException {
				//log("compteur de choice succeed ////////////////////////// succeed");
				thread.removeChannelFromRegistered(channel);
				unregistered=true;
			}
			
		});
		
		return choice;
	}
	
}
