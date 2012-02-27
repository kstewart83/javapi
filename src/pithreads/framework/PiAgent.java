package pithreads.framework;

import java.io.PrintStream;
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
 * Some notable responsibilities of the Pi-agent are as follows:
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
	private BlockingDeque<ControlEvent> eventQueue;
	private final int QUEUE_CAPACITY = 256;
	private TreeMap<Integer,PiThread> piThreads;
	private TreeMap<Integer,PiChannel<?>> piChannels;
	private Set<Integer> waitThreads;
	private boolean initialSequence;
	private long startTime;
	private int genThreadId;
	private int genChanId;
	private PrintStream logStream;
	private PiFactory factory;
	private final boolean terminationDetector;


	public static final int ID_NOT_ASSIGNED = -1;
	public static final int ID_ALREADY_REGISTERED = -2;
	public static final int ID_TOO_MANY = -3;

	protected static int genId = 0;

	/**
	 * Create a Pi-agent (default name)
	 */
	/* package */ PiAgent(boolean terminationDetector, PrintStream logStream) {
		this("agent"+(genId++), terminationDetector, logStream);
	}

	/**
	 * Create a named Pi-agent
	 * @param name the name of the Pi-agent
	 * @param tracemode 
	 */
	protected PiAgent(String name, boolean terminationDetector, PrintStream logStream) {
		super("agent"+(genId++));

		initialSequence = true;
		eventQueue = new LinkedBlockingDeque<ControlEvent>(QUEUE_CAPACITY);
		piThreads = new TreeMap<Integer,PiThread>();
		initialSequence = true;
		piChannels = new TreeMap<Integer,PiChannel<?>>();
		startTime = System.currentTimeMillis();
		genThreadId = 0;
		genChanId = 0;
		waitThreads = new HashSet<Integer>();
		this.terminationDetector = terminationDetector;
		this.logStream = logStream;

		start();
	}
	
	protected void setFactory(PiFactory factory){
		this.factory=factory;
	}

	PiFactory getFactory() {
		return factory;
	}

	public boolean detectTermination() {
		return terminationDetector;
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
	/* package */ void receiveEvent(ControlEvent event) throws InterruptedException { // should not be synchronized but ...
		eventQueue.putFirst(event);
	}


	private synchronized int generateId(int startId, Set<Integer> assignedIDs) {
		//int count = 0;
		//int id = startId;

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

	private void processRegisterEvent(RegisterEvent event) {
		PiThread thread = event.getSource();

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

	private void processUnregisterEvent(UnregisterEvent event) {
		if(piThreads.remove(event.getSource().getThreadId())==null)
			throw new IllegalArgumentException("PiThread not registered");
		waitThreads.remove(event.getSource().getThreadId());
		event.getSource().assignThreadId(ID_NOT_ASSIGNED);
		log("Thread unregistered : " + event.getSource().getName());
	}

	private void processNewEvent(NewEvent event) {
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

	private void processReclaimEvent(ReclaimEvent event) {
		if(piChannels.remove(event.getPiChannel().getId())==null)
			throw new IllegalArgumentException("Pichannel not registered");
		event.getPiChannel().assignId(ID_NOT_ASSIGNED);
	}

	private void processWaitEvent(WaitEvent event) {
		waitThreads.add(event.getSource().getThreadId());
	}

	private void processAwakeEvent(AwakeEvent event) {
		waitThreads.remove(event.getPiThread().getThreadId());
	}

	private void processLogEvent(LogEvent event) {
		synchronized(logStream) {
			StringBuffer buf = new StringBuffer();
			buf.append("LOG@");
			buf.append(event.getTime()-startTime);
			buf.append("[PiThread:");
			buf.append(event.getSource().getName());
			buf.append("]");
			buf.append(">");
			buf.append(event.getMessage());
			logStream.println(buf.toString());
			logStream.flush();
		}
	}

	private void log(String message) {
		synchronized(logStream) {
			StringBuffer buf = new StringBuffer();
			buf.append("LOG@");
			buf.append(System.currentTimeMillis()-startTime);
			buf.append("[Agent:");
			buf.append(getName());
			buf.append("]");
			buf.append(">");
			buf.append(message);
			logStream.println(buf.toString());
			logStream.flush();
		}		
	}

	private void processEvent(ControlEvent event) {
		if(event instanceof RegisterEvent) {
			processRegisterEvent((RegisterEvent) event);
		} else if(event instanceof UnregisterEvent) {
			processUnregisterEvent((UnregisterEvent) event);
		} else if(event instanceof LogEvent) {
			processLogEvent((LogEvent) event);
		} else if(event instanceof NewEvent) {
			processNewEvent((NewEvent) event);
		} else if(event instanceof ReclaimEvent) {
			processReclaimEvent((ReclaimEvent) event);
		} else if(event instanceof WaitEvent) {
			processWaitEvent((WaitEvent) event);
		} else if(event instanceof AwakeEvent) {
			processAwakeEvent((AwakeEvent) event);
		} else {
			throw new IllegalArgumentException("Does not understand event: "+event);
		}
	}

	@Override
	public void run() {
		boolean finished = false;
		while(!finished) {

			try {
				//System.out.println("AGENT: next event = ");
				ControlEvent event = null;
				event = eventQueue.pollLast(250, TimeUnit.MILLISECONDS); // wait for an event
				//System.out.println("AGENT: process event = "+event);
				if(event!=null) {
					processEvent(event);
				}

				//log("Remaining threads = "+piThreads.size());

				if(terminationDetector && waitThreads.size() == piThreads.size() && eventQueue.size()==0 && !initialSequence) {
					/* Termination detection is started
					 * look for all threads we know are waiting
					 * (deposited commitments)
					 */
					log("termination detection started");
					Set<PiThread> waitingThreads = new HashSet<PiThread>();
					for(PiChannel<?> chan : piChannels.values()) {
						chan.acquire(this);
						waitingThreads.addAll(chan.cleanup());
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
						System.out.println(piThreads.size());
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


