package pithreads.framework;

/**
 * 
 * An input guard protects an execution branch so that it
 * is only executed after a synchronization/input from a specified channel.
 * 
 * @author Frederic Peschanski
 *
 * @param <T> the type of the value passed through the guard
 */
public class InputGuard<T> extends Guard {
	private PiChannel<T> chan;
	
	public InputGuard(PiChannel<T> chan, int index) {
		super(Type.INPUT,index);
		this.chan = chan;
	}
	
	public PiChannel<T> getChannel() {
		return chan;
	}
			
}
