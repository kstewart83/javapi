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
	private PiAgentConfig config;
	private PiAgent agent;
	
	/**
	 * Get a factory for agents, pi-threads, etc.
	 * @param config the configuration for the factory
	 * @return
	 */
	public static PiFactory getFactory(PiAgentConfig config) {
		return new PiFactory(config);
	}

	/** Create a factory instance. */
	private PiFactory(PiAgentConfig config) {
		this.config = config;
		agent = null;
	}
			
	/**
	 * Create a new Pi-thread agent with default name.
	 * @throws IllegalStateException if an agent is already attached to the factory.
	 */
	public PiAgent createAgent() {
		if(agent!=null) {
			throw new IllegalStateException("Agent already created");
		}
		agent = new PiAgent(terminationDetector, debugMode, logStream, debugStream);
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
		agent = new PiAgent(name, terminationDetector, debugMode, logStream, debugStream);
		return agent;
	}
	
	/**
	 * Create a new Pi-thread with the specified name.
	 * @param name The name of the Pi-thrads (mostly used for debugging purpose)
	 * @return a newly created Pi-Thread
	 */
	public PiThread createThread(String name) {
		if(agent==null) {
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
		if(agent==null) {
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
		if(agent==null) {
			throw new IllegalStateException("Agent not created");
		}		
		return new PiChannel<T>(agent,null,name);
	}
}
