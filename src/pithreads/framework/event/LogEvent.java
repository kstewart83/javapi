package pithreads.framework.event;


public class LogEvent extends ControlEvent {
	private static final long serialVersionUID = -5812802233497079332L;

	private final String message;
	
	public LogEvent(Object source, String message) {
		super(source,Type.LOG);
		this.message = message;
	}
	 
	public String getMessage() {
		return message;
	}
	
	
}
