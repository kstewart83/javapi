package pithreads.framework.debug;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.PiThreadBroadcast;

public class DebugFactory implements PiFactory{

	Boolean debugMode;
	PiThread pithread;
	PiThreadBroadcast pithreadBroadcast;
	
	public DebugFactory(Boolean b){
		debugMode = b;
	}
	
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent){
		pithreadBroadcast = new PiThreadBroadcast(agent);
		return pithreadBroadcast;
	}
	
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, Task task){
		pithreadBroadcast = new PiThreadBroadcast(agent);
		pithreadBroadcast.assignTask(task);
		return pithreadBroadcast;
	}
	
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, String str){
		pithreadBroadcast = new PiThreadBroadcast(agent,str);
		return pithreadBroadcast;
	}
	
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, String str, Task task){
		pithreadBroadcast = new PiThreadBroadcast(agent,str);
		pithreadBroadcast.assignTask(task);
		return pithreadBroadcast;
	}
	
	public <T> PiChannelBroadcast<T> newPiChannelBroadcast(PiAgent agent){
			return new PiChannelBroadcast<T>(agent);
	}

	public <T> PiChannelBroadcast<T> newPiChannelBroadcast(PiAgent agent, String str){
			return new PiChannelBroadcast<T>(agent,str);
	}	
	
	public PiThread newPiThread(PiAgent agent, Task task){
		if (debugMode){
			pithread = new PiThreadDebug((PiAgentDebug)agent);
			pithread.assignTask(task);
			return pithread;}
		else{
			pithread = new PiThread(agent);
			pithread.assignTask(task);
			return pithread;}	
	}

	public PiThread newPiThread(PiAgent agent, String str, Task task){
		if (debugMode){
			pithread = new PiThreadDebug((PiAgentDebug)agent,str);
			pithread.assignTask(task);
			return pithread;}
		else{
			pithread = new PiThread(agent,str);
			pithread.assignTask(task);
			return pithread;}
	}
	public <T> PiChannel<T> newPiChannel(PiAgent agent){
		if (debugMode)
		return new PiChannelDebug<T>(agent);
		else
			return new PiChannel<T>(agent);
	}

	public <T> PiChannel<T> newPiChannel(PiAgent agent, String str){
		if (debugMode)
		return new PiChannelDebug<T>(agent,str);
		else
			return new PiChannel<T>(agent,str);
	}	
	public PiAgent newPiAgent(){
		if (debugMode)
		return new PiAgentDebug();
		else
			return new PiAgent();
	}

	public PiAgent newPiAgent(String name){
		if (debugMode)
		return new PiAgentDebug(name);
		else
			return new PiAgent(name);
	}


}
