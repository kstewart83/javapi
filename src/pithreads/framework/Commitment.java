package pithreads.framework;

import java.lang.ref.WeakReference;

import pithreads.framework.PiThread;

public class Commitment {
	public static enum Type { INPUT, OUTPUT, CONDITION, IN_COND, OUT_COND };
	
	private final Type type;
	private final int chanId;
	private final long turn;
	private int guardIndex;
	
	private final int commitId;
	
	private static int genId = 0;
	
	private WeakReference<PiThread> threadRef;
	
	protected Commitment(Type type, PiThread thread, int chanId, long turn, int guardIndex) {
		this.type = type;
		threadRef = new WeakReference<PiThread>(thread);		
		this.chanId = chanId;
		this.turn = turn;
		commitId = genId++;
		this.guardIndex = guardIndex;
	}

	protected Commitment(Type type, PiThread thread, int chanId, long turn) {
		this(type,thread,chanId,turn,-1); // not a commitment for guard
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
	
	public long getTurn() {
		return turn;
	}
	
	public boolean isValid() {
		PiThread thread = threadRef.get();
		if(thread==null)
			return false;
		return thread.getTurn()==turn;
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
