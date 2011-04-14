package pithreads.framework;

import pithreads.framework.PiThread;

public class InputCommitment extends Commitment {
	public InputCommitment(PiThread thread, int chanId, ValidFlag validFlag) {
		super(Type.INPUT,thread, chanId, validFlag);
	}
	
	public InputCommitment(PiThread thread, int chanId, ValidFlag validFlag, int guardIndex) {
		super(Type.INPUT,thread, chanId, validFlag, guardIndex);
	}
	
}
