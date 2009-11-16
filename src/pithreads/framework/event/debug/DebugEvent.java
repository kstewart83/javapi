package pithreads.framework.event.debug;

import pithreads.framework.PiThread;
import pithreads.framework.event.ControlEvent;

public abstract class DebugEvent extends ControlEvent {
	private static final long serialVersionUID = -2048350123764912703L;
	
	protected DebugEvent(PiThread source) {
		super(source);		
	}

	
}
