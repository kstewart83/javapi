package pithreads.framework.event;

import pithreads.framework.PiChannel;

public class ReclaimEvent extends ControlEvent {
	private static final long serialVersionUID = -7913957383311185736L;

	private final PiChannel<?> chan;
	
	public ReclaimEvent(PiChannel<?> chan) {
		super(chan, Type.RECLAIM_CHANNEL);
		this.chan = chan;
	}
	
	public PiChannel<?> getPiChannel() {
		return chan;
	}
	
	
}
