package pithreads.framework.event.debug;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class SyncEvent<T> extends DebugEvent {
	private static final long serialVersionUID = -5977406196818255357L;

	private PiChannel<T> channel;
	private PiThread sender;
	private PiThread receiver;
	private T value;
	private int guardIndex;
	
	public SyncEvent(PiThread source, PiThread sender, PiThread receiver, PiChannel<T> channel, T value, int guardIndex) {
		super(source);
		this.channel = channel;
		this.value = value;
		this.sender = sender;
		this.receiver = receiver;
		this.guardIndex = guardIndex;
	}

	public SyncEvent(PiThread source, PiThread sender, PiThread receiver, PiChannel<T> channel, T value) {
		this(source,sender,receiver,channel,value,-1);
	}
	
	public PiChannel<T> getChannel() {
		return channel;
	}
	
	public T getValue() {
		return value;
	}
	
	public PiThread getSender() {
		return sender;
	}
	
	public PiThread getReceiver() {
		return receiver;
	}
	
	public int getGuardIndex() {
		return guardIndex;
	}
	
}
