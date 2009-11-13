package pithreads.framework;

public class RunEngineException extends RunException {
	private static final long serialVersionUID = -2166843027099223143L;

	public RunEngineException(String message) {
		super("Engine error: "+message+" (please report)");
	}
}
