package pithreads.framework;

public abstract class InputTask<T> extends Task {
	private T receivedValue;
	
	protected InputTask() {
		receivedValue = null;
	}
	
	/* package */ void setReceivedValue(T value) {
		receivedValue = value;
	}
	
	public final boolean isInputTask() {
		return true;
	}
	
	public final void body() throws RunException {
		body(receivedValue);
	}
	
	public abstract void body(T receivedValue);
	
}
