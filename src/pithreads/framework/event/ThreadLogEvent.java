package pithreads.framework.event;



public class ThreadLogEvent extends LogEvent {
	private static final long serialVersionUID = 7249334886593799619L;

	public ThreadLogEvent(Thread thread, String message) {		
		super(thread,"["+thread.getName()+"]: "+message);
	}
		
}
