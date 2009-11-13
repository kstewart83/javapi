package pithreads.framework;

/**
 * 
 * A specialized task that does ... nothing
 * (may be useful for guaded constructs such as choices)
 * 
 * @author Frederic Peschanski
 *
 */
public class EmptyTask extends Task {

	public EmptyTask() {		
	}
	
	@Override
	public void body() throws RunException {
		// do nothing
	}

}
