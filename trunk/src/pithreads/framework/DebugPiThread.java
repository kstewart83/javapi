package pithreads.framework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pithreads.framework.event.debug.GuardEvent;
import pithreads.framework.event.debug.ReceiveEvent;
import pithreads.framework.event.debug.SendEvent;
import pithreads.framework.event.debug.SyncEvent;
import pithreads.framework.utils.Pair;



public class DebugPiThread extends PiThread {

	/* package */ DebugPiThread(PiAgent agent, String name) {
		super(agent, name);
	}

	
	/* package */ DebugPiThread(PiAgent agent) {
		super(agent);
	}
	
	/* package */ <T> T receive(PiChannel<T> channel) throws RunException {
		channel.acquire(this);
		Pair<T,PiThread> valueAndSender = receiveOrCommit(channel,-1);
		if(valueAndSender==null) {
			// ko no value received (commitment)
			sendEvent(new ReceiveEvent<T>(this,channel));
			channel.release(this);
			// wait for a synchronization
			waitForCommitment();
			@SuppressWarnings("unchecked")
			T value = (T) receivedValue;
			receivedValue = null;
			// the SyncEvent is sent by the sender
			return value;
		} else {
			// ok a value has been received (synchronization)
			sendEvent(new SyncEvent<T>(this,valueAndSender.getSecond(),this,channel,valueAndSender.getFirst()));
			channel.release(this);
			return valueAndSender.getFirst();
		}
	}

	/* package */  <T> void send(PiChannel<T> channel, T value) throws RunException {
		channel.acquire(this);
		PiThread receiver = sendOrCommit(channel, value, -1);
		if(receiver==null) {
			// ko no synchronization is possible
			sendEvent(new SendEvent<T>(this,channel,value));
			channel.release(this);
			waitForCommitment();
			return;
		} else {
			// ok a synchronization took place
			sendEvent(new SyncEvent<T>(this,this,receiver,channel,value));
			channel.release(this);
			return;
		}
	}

	/* package */ Guard choose(List<Guard> guards, TreeSet<PiChannel<?>> guardedChannels) throws RunException {
		
		// first phase, need to acquire all the channels
		Set<PiChannel<?>> acquiredChannels = new HashSet<PiChannel<?>>();

		boolean allAcquired = false;
		
		while(!allAcquired) {
			Iterator<PiChannel<?>> iter = guardedChannels.iterator();
			boolean restart = false;
			while(iter.hasNext() && !restart) {
				PiChannel<?> chan = iter.next();
				boolean acquired = chan.tryAcquire(this);
				if(!acquired) { // if cannot acquire
					restart = true; // then restart (so other threads have the opportunity to acquire the 
					                // channels)
					yield(); // force a context switch
				} else {
					acquiredChannels.add(chan);
				}
			}
			if(restart) {  // need to restart acquiring the channel
				releaseAllChannels(acquiredChannels); // restart from scratch
				acquiredChannels.clear();
				yield(); // force a context switch
			} else { // ok we acquired all the channels
				allAcquired = true;
			}
		}
		
		// here we acquired all the channels referenced by this choice
				
		List<GuardEvent> gevents = new ArrayList<GuardEvent>();
		// second phase, try to enable the choice, or deposit some commitment
		for(int i=0;i<guards.size();i++) {
			Guard guard = guards.get(i);
			boolean check = guard.checkGuard();
			if(!check) {	
				continue; // skip this guard
			}
			gevents.add(new GuardEvent(this,guard,check));
			switch(guard.getType()) {
			case INPUT:
				Pair<?,?> valueAndSender = receiveOrCommit(guard.asInputGuard().getChannel(), i);
				if(valueAndSender!=null) {
					sendEvent(new SyncEvent<?>(this,valueAndSender.getSecond(),this,guard.asInputGuard().getChannel(),valueAndSender.getFirst(),i));
					receivedValue = valueAndSender.getFirst();
					releaseAllChannels(acquiredChannels);
					return guard;
				}
				break;
			case OUTPUT:
				PiThread receiver = sendOrCommit(guard.asOutputGuard().getChannel(),guard.asOutputGuard().getValue(), i);
				if(receiver!=null) {
					releaseAllChannels(acquiredChannels);
					return guard;
				}
				break;
			case USER: // the check has already been done			
					releaseAllChannels(acquiredChannels);
					return guard;
			default:
					throw new Error("Unsupported guard type: "+guard.getType());
			}
		}
		
		// third phase : block until the choice gets enabled		
		
		// here, all guards are not enabled
		// no we need to block until an input or an output occur
		// (user guards are not evaluated anymore)
		
		// special case : if no acquired channel, the whole choice fails
		if(acquiredChannels.size()==0)
			return null;
		
		releaseAllChannels(acquiredChannels);

		// now block until one commitment gets enabled 
		waitForCommitment();
		
		// when awaken, get the index of the selected guard
		Guard enabledGuard = guards.get(enabledGuardIndex);
		enabledGuardIndex = -1;
		return enabledGuard;		
	}


}
