package pithreads.framework.debug.event;

import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class TakeInputCommitmentEvent extends ControlEvent{

	
	private PiChannelDebugInfo channel;
    private PiThreadDebugInfo receiver;
    private PiThreadDebugInfo sender;
    
	public PiChannelDebugInfo getChannel(){return channel;}
	public PiThreadDebugInfo getThreadReceiver(){return receiver;}
    public PiThreadDebugInfo getThreadSender(){return sender;}
    
	public TakeInputCommitmentEvent(Object source, PiChannel<?> c, PiThread r) {
		super(source, Type.TAKE_INPUT_COMMITMENT);
		this.channel=new PiChannelDebugInfo((PiChannelDebug) c);
        this.receiver=new PiThreadDebugInfo((PiThreadDebug) r);
        this.sender = new PiThreadDebugInfo((PiThreadDebug) source);
    }

    public TakeInputCommitmentEvent(PiThreadDebugInfo source, PiChannelDebugInfo c, PiThreadDebugInfo r) {
		super(source, Type.TAKE_INPUT_COMMITMENT);
		this.channel= c;
        this.receiver= r;
        this.sender = source;
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = -6059816152090170941L;
	
	public String toString(){
		return "TAKE_INPUT_COMMITMENT\nSource :"+((PiThreadDebug)source).toString()+
						"\nChannel :"+channel.toString()+
						"\nReceiver :"+receiver.toString()+
						"\nend of TAKE_INPUT_COMMITMENT\n";
	}
}
