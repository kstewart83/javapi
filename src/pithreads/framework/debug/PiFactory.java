package pithreads.framework.debug;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.Task;
import pithreads.framework.broadcast.PiChannelBroadcast;
import pithreads.framework.broadcast.PiThreadBroadcast;


public interface PiFactory {

	
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent);
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, Task task);
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, String str);
	public PiThreadBroadcast newPiThreadBroadcast(PiAgent agent, String str, Task task);
	public <T> PiChannelBroadcast<T> newPiChannelBroadcast(PiAgent agent);
	public <T> PiChannelBroadcast<T> newPiChannelBroadcast(PiAgent agent, String str);
	public PiThread newPiThread(PiAgent agent, Task task);
	public PiThread newPiThread(PiAgent agent, String str, Task task);
	public <T> PiChannel<T> newPiChannel(PiAgent agent);
	public <T> PiChannel<T> newPiChannel(PiAgent agent, String str);
	public PiAgent newPiAgent();
	public PiAgent newPiAgent(String name);
	
}
