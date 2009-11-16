package pithreads.framework;

/**
 * A value maker creates a value on demand.
 * It is mainly used for output guards in guarded choices.
 * 
 * @author Frederic Peschanski
 *
 * @param <T> the type of the value to make
 */
public interface GuardExpression {
	/**
	 * A boolean expression used as a guard
	 * @return the result of evaluating the guard expression
	 */
	public boolean check();
}
