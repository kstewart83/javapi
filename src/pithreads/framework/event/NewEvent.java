package pithreads.framework.event;

import pithreads.framework.PiChannel;

public class NewEvent extends ControlEvent {
	private static final long serialVersionUID = 5617967843604996867L;

	private final PiChannel<?> chan;
	
	public NewEvent(PiChannel<?> chan) {
		super(chan, Type.NEW_CHANNEL);
		this.chan = chan;
	}
	
	public PiChannel<?> getPiChannel() {
		return chan;
	}
	
	
}
