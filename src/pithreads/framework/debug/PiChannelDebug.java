package pithreads.framework.debug;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.RunEngineException;
import pithreads.framework.RunException;
import pithreads.framework.debug.event.ChannelAquiredEvent;
import pithreads.framework.debug.event.ChannelReleasedEvent;
import pithreads.framework.debug.event.FailedChannelAquireEvent;
import pithreads.framework.event.*;
//import pithreads.framework.event.ControlEvent.Type;

public class PiChannelDebug<T> extends PiChannel<T> {

	private PiChannelDebugInfo infos;
	
    public PiChannelDebug(PiAgent agent) {
        super(agent); 
    }

    public PiChannelDebug(PiAgent agent, String str) {
        super(agent, str);
    }

    /* package */ boolean tryAcquire(Thread thread) throws RunException {
        if (acquired.compareAndSet(false, true) == true) {
            // ok we acquired the channel
            owner = thread;
            nbOwners++;
            try {
                sendEvent(new ChannelAquiredEvent(thread, this));
            } catch (InterruptedException e) {
                Error error = new Error("Thread interrupted: " + e.getMessage());
                error.initCause(e);
                throw error;
            }
            if (nbOwners != 1) {
                throw new RunEngineException("Multiple owners for channel");
            }
            return true;
        }
        try {
            sendEvent(new FailedChannelAquireEvent(thread, this));
        } catch (InterruptedException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }
        return false;
    }

	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void acquire(Thread thread) throws RunException {
        //int count = 0;

        while (!acquired.compareAndSet(false, true)) {
  
                try {
                    sendEvent(new FailedChannelAquireEvent(thread, this));
                } catch (InterruptedException e) {
                    Error error = new Error("Thread interrupted: " + e.getMessage());
                    error.initCause(e);
                    throw error;
                }
  
            // block until acquired CAREFUL USE !
            
            Thread.yield(); // XXX: faster ? slower ? ==> Seems a lot faster !
        }

        owner = thread;
        nbOwners++;
        try {
            sendEvent(new ChannelAquiredEvent(thread, this));
        } catch (InterruptedException e) {
            Error error = new Error("Thread interrupted: " + e.getMessage());
            error.initCause(e);
            throw error;
        }
        if (nbOwners != 1) {
            throw new RunEngineException("Multiple owners for channel");
        }
    }

    /* package */ void sendEvent(ControlEvent event) throws InterruptedException {
        agent.receiveEvent(event);
    }

	/*
	 * Public but should be protected in framework and debug packages
	 */
	public  void release(Thread thread) throws RunException {
		if(thread!=owner)
			throw new RunEngineException("Channel must be released by owner");
		owner = null;
		nbOwners--;
		if(nbOwners!=0)
			throw new RunEngineException("Invalid owners count for channel");
		acquired.set(false);
		 try {
	            sendEvent(new ChannelReleasedEvent(thread, this));
	        } catch (InterruptedException e) {
	            Error error = new Error("Thread interrupted: " + e.getMessage());
	            error.initCause(e);
	            throw error;
	        }
	}
	
	public String toString(){
        infos = new PiChannelDebugInfo(this);
		return infos.toString();
	}
}
