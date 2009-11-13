package pithreads.framework.event;

import pithreads.framework.PiThread;


public class LogEvent extends ControlEvent {
	private static final long serialVersionUID = -5812802233497079332L;

	private final String message;
	
	public LogEvent(PiThread source, String message) {
		super(source);
		this.message = message;
	}
	 
	public String getMessage() {
		return message;
	}
	
	
}
