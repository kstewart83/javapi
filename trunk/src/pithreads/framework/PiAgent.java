package pithreads.framework;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import pithreads.framework.event.AwakeEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.framework.event.LogEvent;
import pithreads.framework.event.NewEvent;
import pithreads.framework.event.ReclaimEvent;
import pithreads.framework.event.RegisterEvent;
import pithreads.framework.event.UnregisterEvent;
import pithreads.framework.event.WaitEvent;

/**
 * 
 * The purpose of a Pi-agent is to manage a set of Pi-threads.
 * 
 * A Pi-agent possesses its own thread of control and is thus independent from
 * the Pi-threads it manages. The Pi-agent offers a centralized view of the
 * set of Pi-threads it manages. In order to avoid any interference or bottleneck,
 * the Pi-threads communicate with their controlling agent using an asynchronous event-queue.
 * 
 * Some notable responsabilities of the Pi-agent are as follows:
 * 
 * <ul>
 *   <li>assignment of unique identifiers to Pi-threads and channels</li>
 *   <li>implementation of broadcast primitives (weakly fair scheduler round)</li>
 *   <li>termination/deadlock detection</li>
 *   <li>logging</li>
 *   <li>Error management and debugging aids</li>
 * </ul>
 * 
 * Specialized Pi-agents can be created by subclassing this class
 * (protected interface).
 * 
 * @author Frederic Peschanski
 *
 */
public class PiAgent extends Thread {
	//public should be protected
	public BlockingDeque<ControlEvent> eventQueue;
	//public should be protected
	public  final int QUEUE_CAPACITY = 256;
	//public should be protected
	public  TreeMap<Integer,PiThread> piThreads;
	//public should be protected
	public  TreeMap<Integer,PiChannel<?>> piChannels;
	//public should be protected
	public  Set<Integer> waitThreads;
	//public should be protected
	public  boolean initialSequence;
	//public should be protected
	public  long startTime;
	//public should be protected
	public  int genThreadId;
	//public should be protected
	public  int genChanId;	
	
	public static final int ID_NOT_ASSIGNED = -1;
	public static final int ID_ALREADY_REGISTERED = -2;
	public static final int ID_TOO_MANY = -3;

	
	//public should be protected
	public  static int genId = 0;
	
	/**
	 * Create a Pi-agent (default name)
	 */
	public PiAgent() {
		this("agent"+(genId++));
	}
	
	/**
	 * Create a named Pi-agent
	 * @param name the name of the Pi-agent
	 */
	public PiAgent(String name) {
		super("agent"+(genId++));
		eventQueue = new LinkedBlockingDeque<ControlEvent>(QUEUE_CAPACITY);
		piThreads = new TreeMap<Integer,PiThread>();
		initialSequence = true;
		piChannels = new TreeMap<Integer,PiChannel<?>>();
		startTime = System.currentTimeMillis();
		genThreadId = 0;
		genChanId = 0;
		waitThreads = new HashSet<Integer>();
		start();
	}
	
	/**
	 * Detach the Pi-agent from the thread where it was constructed (e.g main application thread).
	 * This must be called so that the functionalities such as broadcast primitives and termination/deadlock
	 * detection are activated
	 */
	public void detach() {
		initialSequence = false;
	}
	
	/**
	 * Receive a control event in this agent. 
	 * @param event the event to receive
	 * @throws InterruptedException raised if the agent thread has been interrupted
	 */
	public void receiveEvent(ControlEvent event) throws InterruptedException { // should not be synchronized but ...
		eventQueue.putFirst(event);
	}
	
	
	protected synchronized int generateId(int startId, Set<Integer> assignedIDs) {
		int count = 0;
		int id = startId;
		
		/*while(count<=Integer.MAX_VALUE) {
			if(!assignedIDs.contains(id)) {
				return id;
			}
			id++;
			count++;
		}*/
		
		return genId++;
		
		//return ID_TOO_MANY;
	}
	
	protected void processRegisterEvent(RegisterEvent event) {
		PiThread thread = event.getPiThread();
		
		if(thread.getThreadId()!=ID_NOT_ASSIGNED) {
			thread.assignThreadId(ID_ALREADY_REGISTERED);
			return;
		}
		
		int id = generateId(genThreadId, piThreads.keySet());
		
		if(id==ID_TOO_MANY) {
			thread.assignThreadId(id);
			return;
		}
		
		piThreads.put(id,thread);
		thread.assignThreadId(id);
		genThreadId = (id+1);
	}
	
	protected void processUnregisterEvent(UnregisterEvent event) {
		if(piThreads.remove(event.getPiThread().getThreadId())==null)
			throw new IllegalArgumentException("PiThread not registered");
		waitThreads.remove(event.getPiThread().getThreadId());
		event.getPiThread().assignThreadId(ID_NOT_ASSIGNED);
		log("Thread unregistered : " + event.getPiThread().getName());
	}
	
	protected void processNewEvent(NewEvent event) {
		PiChannel<?> chan = event.getPiChannel();
		
		if(chan.getId()!=ID_NOT_ASSIGNED) {
			chan.assignId(ID_ALREADY_REGISTERED);
			return;
		}
		
		int id = generateId(genChanId, piChannels.keySet());
		
		if(id==ID_TOO_MANY) {
			chan.assignId(id);
			return;
		}
		
		piChannels.put(id,chan);
		chan.assignId(id);
		genChanId = (id+1);
	}
	
	protected void processReclaimEvent(ReclaimEvent event) {
		if(piChannels.remove(event.getPiChannel().getId())==null)
			throw new IllegalArgumentException("Pichannel not registered");
		event.getPiChannel().assignId(ID_NOT_ASSIGNED);
	}
	
	protected void processWaitEvent(WaitEvent event) {
		waitThreads.add(event.getPiThread().getThreadId());
	}

	protected void processAwakeEvent(AwakeEvent event) {
		waitThreads.remove(event.getPiThread().getThreadId());
	}

	protected synchronized void processLogEvent(LogEvent event) {
		System.out.print("LOG@");
		System.out.print(event.getTime()-startTime);
		System.out.print(">");
		System.out.println(event.getMessage());
		System.out.flush();
	}
	
	protected void log(String message) {
		LogEvent event = new LogEvent(this,message);
		processLogEvent(event);
	}

	protected void processEvent(ControlEvent event) {
		switch(event.getType()) {
		case REGISTER_THREAD:
			processRegisterEvent(event.asRegisterEvent());
			break;
		case UNREGISTER_THREAD:
			processUnregisterEvent(event.asUnregisterEvent());
			break;
		case LOG:
			processLogEvent(event.asLogEvent());
			break;
		case NEW_CHANNEL:
			processNewEvent(event.asNewEvent());
			break;
		case RECLAIM_CHANNEL:
			processReclaimEvent(event.asReclaimEvent());
			break; // for the moment do nothing
		case WAIT_THREAD:
			processWaitEvent(event.asWaitEvent());
			break; // for the moment do nothing
		case AWAKE_THREAD:
			processAwakeEvent(event.asAwakeEvent());
			break; // for the moment do nothing
		case USER:
			break; // TODO: user-defined event handling
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
					
					if(waitingThreads.size() == (piThreads.values().size())) {

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
		}
		
		// ok let's terminate the agent		
		log("End of agent");
	}	 
}
