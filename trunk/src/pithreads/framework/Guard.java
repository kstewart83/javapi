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
	
	protected Guard(Type type, int index) {
		this.type = type;
		this.index = index;
	}
	
	public Type getType() {
		return type;
	}
	
	public int getIndex() {
		return index;
	}
	
	public <T> InputGuard<T> asInputGuard() {
		return (InputGuard<T>) this;
	}
	
	public <T> OutputGuard<T> asOutputGuard() {
		return (OutputGuard<T>) this;
	}
	
	public UserGuard asUserGuard() {
		return (UserGuard) this;
	}

}
