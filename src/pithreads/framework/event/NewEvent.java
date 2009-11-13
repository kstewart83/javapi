package pithreads.framework.event;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class NewEvent extends ControlEvent {
	private static final long serialVersionUID = 5617967843604996867L;

	private final PiChannel<?> chan;
	
	public NewEvent(PiThread source, PiChannel<?> chan) {
		super(source);
		this.chan = chan;
	}
	
	public PiChannel<?> getPiChannel() {
		return chan;
	}
	
	
}
