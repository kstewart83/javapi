package pithreads.framework.debug.event;

import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class AddOutputCommitmentEvent extends ControlEvent{

	private PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThread;
	public PiChannelDebugInfo getChannel(){return channel;}
	public PiThreadDebugInfo getThread(){return piThread;}
	
	public AddOutputCommitmentEvent(Object source, PiChannel<?> c) {
		super(source, Type.ADD_OUTPUT_COMMITMENT);
		this.channel = new PiChannelDebugInfo((PiChannelDebug) c);
        this.piThread = new PiThreadDebugInfo((PiThreadDebug) source);
	}

    public AddOutputCommitmentEvent(PiThreadDebugInfo source, PiChannelDebugInfo c) {
		super(source, Type.ADD_OUTPUT_COMMITMENT);
		this.channel = c;
        this.piThread = source;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1736885227948250005L;

	public String toString(){
		return "ADD_OUTPUT_COMMITMENT\nSource :"+((PiThreadDebug)source).toString()+
						"\nChannel :"+channel.toString()+"\nend of ADD_OUTPUT_COMMITMENT\n";
	}
	
}
