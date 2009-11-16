package pithreads.framework.event.debug;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class ReceiveEvent<T> extends DebugEvent {
	private static final long serialVersionUID = -5977406196818255357L;

	private PiChannel<T> channel;
	
	public ReceiveEvent(PiThread source, PiChannel<T> channel) {
		super(source);
		this.channel = channel;
	}
	
	public PiChannel<T> getChannel() {
		return channel;
	}
	
}
