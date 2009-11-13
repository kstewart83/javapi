package pithreads.framework.debug;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.sun.opengl.impl.packrect.Level;


import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.debug.event.EndOfAgentEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.framework.event.RegisterEvent;



public class PiAgentDebug extends PiAgent{

	private ArrayList<Receiver> followEvent;
	private int failedAquiredEventCpt; 
	private PiAgentDebugInfo infos;
	    
    public PiAgentDebug(String name) {
		super(name);
		infos = new PiAgentDebugInfo();
		followEvent= new ArrayList<Receiver>();
		failedAquiredEventCpt = 0;
	}

	public PiAgentDebug() {
		super();
		infos = new PiAgentDebugInfo();
		followEvent= new ArrayList<Receiver>();
		failedAquiredEventCpt = 0;
	}
	
	protected void processDebugEvent(ControlEvent event) {

	}
	
	private void processFailedAquireEvent(ControlEvent event) {
		failedAquiredEventCpt++;
		if(failedAquiredEventCpt>10){
			failedAquiredEventCpt=0;
			//log("it happened !!!");
			sendEvent(event);
		}
	}
	
    public void bind(Receiver receiver){
    	followEvent.add(receiver);
       	}
    private void sendEvent(ControlEvent e){
    	if (!followEvent.isEmpty()){
    		for(Receiver r : followEvent){r.receiveEvent(e);}
    	}
    	//if (followEvent!=null){
    		//followEvent.receiveEvent(e);
    	//}
    }
		

	
	/**
	 * Receive a control event in this agent. 
	 * @param event the event to receive
	 * @throws InterruptedException raised if the agent thread has been interrupted
	 */
	@Override
	protected void processEvent(ControlEvent event) {
		switch(event.getType()) {
		case REGISTER_THREAD:
			processRegisterEvent(event.asRegisterEvent());
            //sendEvent(new RegisterEvent((PiThread)event.getSource()));
			break;
		case UNREGISTER_THREAD:{
			processUnregisterEvent(event.asUnregisterEvent());
            sendEvent(event);
			break;
        }
		case LOG:{

            
			processLogEvent(event.asLogEvent());
            sendEvent(event);
			break;
        }
		case NEW_CHANNEL:
			processNewEvent(event.asNewEvent());
            //sendEvent(event);
			break;
		case RECLAIM_CHANNEL:
			processReclaimEvent(event.asReclaimEvent());
			break; // for the moment do nothing
		case WAIT_THREAD:
			processWaitEvent(event.asWaitEvent());
            sendEvent(event);
			break; // for the moment do nothing
		case AWAKE_THREAD:
			processAwakeEvent(event.asAwakeEvent());
            sendEvent(event);
			break; // for the moment do nothing
		case USER:
			break; // TODO: user-defined event handling
		case ADD_INPUT_COMMITMENT:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case ADD_OUTPUT_COMMITMENT:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case CHANNEL_AQUIRED:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case FAILED_AQUIRE_CHANNEL:
			processFailedAquireEvent(event);
			break; // transfert the event usefull to the debogger
		case NEXT_TURN:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case RELEASE_CHANNEL:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case TAKE_INPUT_COMMITMENT:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case TAKE_OUTPUT_COMMITMENT:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
		case END_OF_AGENT:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
        case CREATE_THREAD:
			sendEvent(event);
			break; // transfert the event usefull to the debogger
			default:
				throw new IllegalArgumentException("Does not understand this event: "+event);
		}
	}



@Override
	public void run() {
		boolean finished = false;
		while(!finished) {
			
			try {
				//System.out.println("AGENT: next event = ");
				ControlEvent event = null;
				event = eventQueue.pollLast(1, TimeUnit.SECONDS); // wait for an event
				//System.out.println("AGENT: process event = "+event);
				if(event!=null) {
					processEvent(event);
				}

				if(waitThreads.size() == piThreads.values().size() && eventQueue.size()==0 && !initialSequence) {
					/* Termination detection is started
					 * look for all threads we know are waiting
					 * (deposited commitments)
					 */
					log("termination detection started");
					Set<PiThread> waitingThreads = new HashSet<PiThread>();
					for(PiChannel<?> chan : piChannels.values()) {
						chan.acquire(this);
						chan.cleanup();
						waitingThreads.addAll(chan.getWaitingThreads());
					}

					for(PiChannel<?> chan : piChannels.values()) {
						chan.release(this);
					}

					// if the detected waiting threads are all the threads
					// then the agent should stop
					if(waitingThreads.size() == piThreads.values().size()) {

						// terminate the waiting threads
						for(PiThread deadThread : waitingThreads) {
							boolean terminated = deadThread.terminateFromAgent();
							if(!terminated) {
								log("cannot terminate thread");
								break;
							} else {
								piThreads.remove(deadThread.getThreadId());
								waitThreads.remove(deadThread.getThreadId());
							}
						}

						if(piThreads.size()==0) {
							finished = true;
						}
					}
				}
			} catch (InterruptedException e) {
				Error error = new Error("Agent interrupted");
				error.initCause(e);
				throw error;
			}  catch (RunException e) {
				Error error = new Error("Agent run problem");
				error.initCause(e);
				throw error;
			}
			//infos.updateInfo(this);
		}

		// ok let's terminate the agent
		log("End of agent");
        sendEvent(new EndOfAgentEvent(this));
	}



	public String toString(){
		return "";//infos.toString();
	}
	

}
