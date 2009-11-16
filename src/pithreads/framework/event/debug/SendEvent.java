package pithreads.framework.event.debug;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class SendEvent<T> extends DebugEvent {
	private static final long serialVersionUID = -5977406196818255357L;

	private PiChannel<T> channel;
	private T value;
	
	public SendEvent(PiThread source, PiChannel<T> channel, T value) {
		super(source);
		this.channel = channel;
		this.value = value;
	}
	
	public PiChannel<T> getChannel() {
		return channel;
	}
	
	public T getValue() {
		return value;
	}
	
}
