package pithreads.framework.event;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;

public class ReclaimEvent extends ControlEvent {
	private static final long serialVersionUID = -7913957383311185736L;

	private final PiChannel<?> chan;
	
	public ReclaimEvent(PiThread source, PiChannel<?> chan) {
		super(source);
		this.chan = chan;
	}
	
	public PiChannel<?> getPiChannel() {
		return chan;
	}
	
	
}
