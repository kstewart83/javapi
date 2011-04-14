package pithreads.framework;

import java.lang.ref.WeakReference;

import pithreads.framework.PiThread;

public class Commitment {
	public static enum Type { INPUT, OUTPUT, CONDITION, IN_COND, OUT_COND };
	
	private final Type type;
	private final int chanId;
	private final ValidFlag validFlag;
	private int guardIndex;
	
	private final int commitId;
	
	private static int genId = 0;
	
	private WeakReference<PiThread> threadRef;
	
	protected Commitment(Type type, PiThread thread, int chanId, ValidFlag validFlag, int guardIndex) {
		this.type = type;
		threadRef = new WeakReference<PiThread>(thread);		
		this.chanId = chanId;
		this.validFlag = validFlag;
		commitId = genId++;
		this.guardIndex = guardIndex;
	}

	protected Commitment(Type type, PiThread thread, int chanId, ValidFlag validFlag) {
		this(type,thread,chanId,validFlag,-1); // not a commitment for guard
	}

	public Type getType() {
		return type;
	}
	
	public int getGuardIndex() {
		return guardIndex;
	}
	
	public boolean isInput() {
		return type==Type.INPUT;
	}
	
	public boolean isOutput() {
		return type==Type.OUTPUT;
	}
	
	public InputCommitment asInput() {
		return (InputCommitment) this;
	}
	
	public OutputCommitment asOutput() {
		return (OutputCommitment) this;
	}

	public PiThread getThreadReference() {
		return threadRef.get();
	}
	
	public int getChannelId() {
		return chanId;
	}
	
	public ValidFlag getValidFlag() {
		return validFlag;
	}
	
	public boolean isValid() {
		return validFlag.valid;
	}
	
	@Override
	public int hashCode() {
		return commitId;
	}
	
	@Override
	public boolean equals(Object o) {
		return o==this;  // referential equality
	}
	
}
