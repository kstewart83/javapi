package pithreads.framework;

import java.io.PrintStream;

/**
 * The PiFactory class is used to create instances of JavaPi components:
 * <ul>
 *   <li>Agents</li>
 *   <li>Pi-Threads</li>
 *   <li>Pi-Channels</li>
 * </ul>
 * 
 * The use of a factory allows to decouple the client code from the actual
 * implementations of these components.
 * 
 * @author F. Peschanski
 *
 */
public class PiFactory {
	private boolean terminationDetector;
	private boolean debugMode;
	private PrintStream logStream;
	private PrintStream debugStream;
	
	private PiAgent agent;
	
	/**
	 * Create a new factory for Pi-threads.
	 * @param terminationDetector if true then the termination detection algorithm will be enabled
	 * @param logStream the output stream for log messages
	 * @param debugMode if true the Pi-thread components will run with debugging support
	 * @param debugStream the output stream for debug information (traces).
	 */
	public PiFactory(boolean terminationDetector, PrintStream logStream, boolean debugMode, PrintStream debugStream) {
		this.terminationDetector = terminationDetector;
		this.debugMode = debugMode;
		this.logStream = logStream;
		this.debugStream = debugStream;
		agent = null;
	}
	
	/**
	 * Create a new factory for Pi-threads with default log and debug streams (stdout and stderr).
	 * @param terminationDetector if true then the termination detection algorithm will be enabled
	 * @param debugMode if true the Pi-thread components will run with debugging support
	 */
	public PiFactory(boolean terminationDetector, boolean debugMode) {
		this(terminationDetector,System.out,debugMode,System.err);
	}

	/**
	 * Create a new factory for Pi-threads without debugging and with default log and debug streams (stdout and stderr).
	 * @param terminationDetector if true then the termination detection algorithm will be enabled
	 */
	public PiFactory(boolean terminationDetector) {
		this(terminationDetector,System.out,false,System.err);
	}

	/**
	 * Create a new factory for Pi-Threads using an existing agent.
	 * @param agent The agent used for the factory.
	 * @param debugMode if true the Pi-thread components will run with debugging support
	 */
	public PiFactory(PiAgent agent, boolean debugMode) {
		this.agent = agent;
		terminationDetector = agent.detectTermination();
		this.debugMode = debugMode;
		logStream = null;
		debugStream = null;
	}
	
	/**
	 * Create a new factory for Pi-Threads using an existing agent without debugging.
	 * @param agent The agent used for the factory.
	 */
	public PiFactory(PiAgent agent) {
		this(agent,false);
	}

	/**
	 * Create a new Pi-thread agent with default name.
	 * @return a newly created agent.
	 * @throws IllegalStateException if an agent is already attached to the factory.
	 */
	public PiAgent createAgent() {
		if(agent!=null) {
			throw new IllegalStateException("Agent already created");
		}
		agent = new PiAgent(terminationDetector, logStream, debugStream);
		return agent;		
	}
	
	/**
	 * Create a new Pi-thread agent with the specified name
	 * @param name The name of the agent (mostly used for debugging purpose).
	 * @return a newly created agent.
	 * @throws IllegalStateException if an agent is already attached to the factory.
	 */
	public PiAgent createAgent(String name) {
		if(agent!=null) {
			throw new IllegalStateException("Agent already created");
		}
		agent = new PiAgent(name, terminationDetector, logStream, debugStream);
		return agent;
	}
	
	/**
	 * Create a new Pi-thread with the specified name.
	 * @param name The name of the Pi-thrads (mostly used for debugging purpose)
	 * @return a newly created Pi-Thread
	 */
	public PiThread createThread(String name) {
		if(agent!=null) {
			throw new IllegalStateException("Agent not created");
		}
		if(debugMode) {
			if(agent.detectTermination()) {
				return new DebugPiThread(agent,name);
			} else {
				return new DebugDaemonPiThread(agent,name);
			}
		} else {
			if(agent.detectTermination()) {
				return new PiThread(agent,name);
			} else {
				return new DaemonPiThread(agent,name);
			}
		}
	}

	public PiThread createThread() {
		if(agent!=null) {
			throw new IllegalStateException("Agent not created");
		}
		if(debugMode) {
			if(agent.detectTermination()) {
				return new DebugPiThread(agent);
			} else {
				return new DebugDaemonPiThread(agent);
			}
		} else {
			if(agent.detectTermination()) {
				return new PiThread(agent);
			} else {
				return new DaemonPiThread(agent);
			}
		}
		
	}
	
	/**
	 * Create a new global channel.
	 * @param <T> The type of the data conveyed on the channel.
	 * @param name The name of the channel (for debugging purpose)
	 * @return a newly created global channel.
	 */
	public <T> PiChannel<T> createChannel(String name) {
		if(agent!=null) {
			throw new IllegalStateException("Agent not created");
		}		
		return new PiChannel<T>(agent,null,name);
	}
}
