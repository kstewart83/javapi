package pithreads.framework.event.debug;

import pithreads.framework.Guard;
import pithreads.framework.PiThread;

public class GuardEvent extends DebugEvent {
	private static final long serialVersionUID = -5977406196818255357L;

	private Guard guard;
	private boolean check;
	
	public GuardEvent(PiThread source, Guard guard, boolean check) {
		super(source);
		this.guard = guard;
		this.check = check;
	}
	
	public boolean guardIsEnabled() {
		return check;
	}
	
	public Guard getGuard() {
		return guard;
	}
	
}
