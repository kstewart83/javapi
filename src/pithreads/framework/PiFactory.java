package pithreads.framework;

import java.io.PrintStream;

public class PiFactory {
	private boolean terminationDetector;
	private boolean debugMode;
	private PrintStream logStream;
	private PrintStream debugStream;
	
	private PiAgent agent;
	
	public PiFactory(boolean terminationDetector, PrintStream logStream, boolean debugMode, PrintStream debugStream) {
		this.terminationDetector = terminationDetector;
		this.debugMode = debugMode;
		this.logStream = logStream;
		this.debugStream = debugStream;
		agent = null;
	}
	
	public PiFactory(PiAgent agent, boolean debugMode) {
		this.agent = agent;
		terminationDetector = agent.detectTermination();
		this.debugMode = debugMode;
		logStream = null;
		debugStream = null;
	}
	
	public PiAgent createAgent() {
		if(agent!=null) {
			throw new IllegalStateException("Agent already created");
		}
		agent = new PiAgent(terminationDetector, logStream, debugStream);
		return agent;		
	}
	
	public PiAgent createAgent(String name) {
		if(agent!=null) {
			throw new IllegalStateException("Agent already created");
		}
		agent = new PiAgent(name, terminationDetector, logStream, debugStream);
		return agent;
	}
	
	public PiThread createThread(String name) {
		if(agent!=null) {
			throw new IllegalStateException("Agent not created");
		}
		if(debugMode) {
			return new PiThreadDebug(agent,name);
		} else {
			return new PiThread(agent,name);
		}
	}

	public PiThread createThread() {
		if(agent!=null) {
			throw new IllegalStateException("Agent not created");
		}
		if(debugMode) {
			return new PiThreadDebug(agent);
		} else {
			return new PiThread(agent);
		}
	}
}
