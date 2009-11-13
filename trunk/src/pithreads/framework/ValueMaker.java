package pithreads.framework;

/**
 * A value maker creates a value on demand.
 * It is mainly used for output guards in guarded choices.
 * 
 * @author Frederic Peschanski
 *
 * @param <T> the type of the value to make
 */
public interface ValueMaker<T> {
	/**
	 * Make a value on-demand
	 * @return the value made
	 */
	public T make();
}
