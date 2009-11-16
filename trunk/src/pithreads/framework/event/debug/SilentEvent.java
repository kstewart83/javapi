package pithreads.framework.event.debug;

import pithreads.framework.PiThread;

public class SilentEvent extends DebugEvent {
	private static final long serialVersionUID = 8676868973464498602L;

	private int guardIndex;
	
	public SilentEvent(PiThread source, int guardIndex) {
		super(source);
		this.guardIndex = guardIndex;
	}
	
	public int getGuardIndex() {
		return guardIndex;
	}
	
}
