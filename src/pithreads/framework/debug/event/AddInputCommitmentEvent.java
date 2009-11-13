package pithreads.framework.debug.event;

import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class AddInputCommitmentEvent extends ControlEvent{

    PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThread;

    public PiThreadDebugInfo getThread(){return piThread;}

	public PiChannelDebugInfo getChannel(){return channel;}
	
	public AddInputCommitmentEvent(Object source, PiChannel<?> c) {
		super(source, Type.ADD_INPUT_COMMITMENT);
		this.channel= new PiChannelDebugInfo((PiChannelDebug) c);
        this.piThread = new PiThreadDebugInfo((PiThreadDebug) source);
	}

public AddInputCommitmentEvent(PiThreadDebugInfo source, PiChannelDebugInfo c) {
		super(source, Type.ADD_INPUT_COMMITMENT);
		this.channel= c;
        this.piThread = source;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4933889536358134941L;
	
	public String toString(){
		return "ADD_INPUT_COMMITMENT\nSource :"+((PiThreadDebug)source).toString()+
						"\nChannel :"+channel.toString()+"\nend of ADD_INPUT_COMMITMENT\n";
	}
}
