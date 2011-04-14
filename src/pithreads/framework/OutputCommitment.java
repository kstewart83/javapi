package pithreads.framework;

import pithreads.framework.PiThread;

public class OutputCommitment extends Commitment {
	private Object value;
	
	public OutputCommitment(PiThread thread, int chanId, ValidFlag validFlag, Object value) {
		super(Type.OUTPUT,thread, chanId, validFlag);
		this.value = value;
	}
	
	public OutputCommitment(PiThread thread, int chanId, ValidFlag validFlag, int guardIndex, Object value) {
		super(Type.OUTPUT,thread, chanId, validFlag, guardIndex);
		this.value = value;
	}

	public Object getValue() {
		return value;
	}
	
	
}
