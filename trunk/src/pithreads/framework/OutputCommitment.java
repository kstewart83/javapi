package pithreads.framework;

import pithreads.framework.PiThread;

public class OutputCommitment extends Commitment {
	private Object value;
	
	public OutputCommitment(PiThread thread, int chanId, long turn, Object value) {
		super(Type.OUTPUT,thread, chanId, turn);
		this.value = value;
	}
	
	public OutputCommitment(PiThread thread, int chanId, long turn, int guardIndex, Object value) {
		super(Type.OUTPUT,thread, chanId, turn, guardIndex);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
	
}
