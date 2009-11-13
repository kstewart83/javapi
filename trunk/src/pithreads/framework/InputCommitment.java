package pithreads.framework;

import pithreads.framework.PiThread;

public class InputCommitment extends Commitment {
	public InputCommitment(PiThread thread, int chanId, long turn) {
		super(Type.INPUT,thread, chanId, turn);
	}
	
	public InputCommitment(PiThread thread, int chanId, long turn, int guardIndex) {
		super(Type.INPUT,thread, chanId, turn, guardIndex);
	}
	
}
