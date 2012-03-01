package pithreads.framework;

import java.io.File;
import java.io.FileNotFoundException;
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
	private final PiConfig config;
	private PiAgent agent;
	private long threadId;
	private long chanId;
	
	/**
	 * Get a factory for agents, pi-threads, etc.
	 * @param config the configuration for the factory
	 * @return
	 */
	public static PiFactory getFactory(PiConfig config) {
		return new PiFactory(config);
	}
	
	/** Get a factory with default configuration. */
	public static PiFactory getFactory() {
		return new PiFactory(new PiConfig());
	}

	/** Create a factory instance. */
	private PiFactory(PiConfig config) {
		this.config = config;
		agent = null;
		threadId = 1;
		chanId = 1;
	}
			
	/**
	 * Create a new Pi-thread agent with default name.
	 * @throws IllegalStateException if an agent is already attached to the factory.
	 */
	public PiAgent createAgent() {
		return createAgent("piagent");
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
		if(name=="" || name==null) {
			throw new IllegalArgumentException("Agent name missing");
		}

		PrintStream logStream = null;
		if(config.logUseStdout()) {
			logStream = System.out;
		} else if(config.logUseStderr()) {
			logStream = System.err;
		} else {
			String filename = config.logUseFile();
			if(filename==null) {
				throw new IllegalArgumentException("Log file name missing");
			}
			File logFile = new File(filename);
			try {
				logStream = new PrintStream(logFile);
			} catch(FileNotFoundException e) {
				throw new Error("Cannot create log file: "+filename);
			}
		}
		
		agent = new PiAgent(name, config.terminationDetection(), logStream);
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
		if(agent.detectTermination()) {
			return new DefaultPiThread(agent,name);
		} else {
			return new DaemonPiThread(agent,name);
		}
	}

	/** Create a new nameless Pi-thread. */
	public PiThread createThread() {
		return createThread("thread"+(threadId++));
	}
	
	/**
	 * Create a new synchronous channel.
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
	
	/** Create a new nameless synchronous channel. */
	public <T> PiChannel<T> createChannel() {
		return createChannel("chan"+(chanId++));
	}
}
