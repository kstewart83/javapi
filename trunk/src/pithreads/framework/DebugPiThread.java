package pithreads.framework;

import pithreads.framework.event.debug.ReceiveEvent;
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
			return value;
		} else {
			// ok a value has been received (synchronization)
			channel.release(this);
			return valueAndSender.getFirst();
		}
	}
	


}
