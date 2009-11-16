package pithreads.framework;

/**
 * 
 * The main ingredient for guarded constructs.
 * 
 * The choice construct uses guard to "protect" its execution branches.
 * 
 * @author Frederic Peschanski
 *
 */
public abstract class Guard {
	public static enum Type { INPUT, OUTPUT, USER };
	
	private final Type type;
	private final int index;
	
	private final GuardExpression guardExpr;
	
	protected Guard(Type type, int index, GuardExpression guardExpr) {
		this.type = type;
		this.index = index;
		this.guardExpr = guardExpr;
	}
	
	protected Guard(Type type, int index) {
		this(type,index,TrueGuard.getSingleton());
	}
	
	public Type getType() {
		return type;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean checkGuard() {
		return guardExpr.check();
	}
	
	@SuppressWarnings("unchecked")
	public <T> InputGuard<T> asInputGuard() {
		return (InputGuard<T>) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T> OutputGuard<T> asOutputGuard() {
		return (OutputGuard<T>) this;
	}
	
	public UserGuard asUserGuard() {
		return (UserGuard) this;
	}

}
