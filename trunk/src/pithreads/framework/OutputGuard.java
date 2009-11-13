package pithreads.framework;

/**
 * 
 * An output guard protects an execution branch so that it
 * is only executed after a synchronization/output to a specified channel.
 * 
 * @author Frederic Peschanski
 *
 * @param <T> the type of the value passed through the guard
 */
public class OutputGuard<T> extends Guard {
	private PiChannel<T> chan;
	private ValueMaker<T> maker;
	
	public OutputGuard(PiChannel<T> chan, ValueMaker<T> maker, int index) {
		super(Type.OUTPUT,index);
		this.chan = chan;
		this.maker = maker;
	}
	
	public PiChannel<T> getChannel() {
		return chan;
	}
	
	public T getValue() {
		return maker.make();
	}
			
}
