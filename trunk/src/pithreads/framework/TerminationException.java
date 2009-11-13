package pithreads.framework;

public class TerminationException extends RunException {
	private static final long serialVersionUID = -2211890270683687863L;

	public TerminationException() {
		super("Thread terminated by agent");
	}
}
