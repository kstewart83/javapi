package pithreads.framework.debug.event;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class TakeOutputCommitmentEvent extends ControlEvent{


   private PiChannelDebugInfo channel;
   private PiThreadDebugInfo sender;
    private PiThreadDebugInfo receiver;
	public PiChannelDebugInfo getChannel(){return channel;}
	public PiThreadDebugInfo getThreadDest(){return sender;}
	public PiThreadDebugInfo getThreadSource(){return receiver;}

	public TakeOutputCommitmentEvent(Object source, PiChannel<?> c, PiThread s) {
		super(source, Type.TAKE_OUTPUT_COMMITMENT);
		this.channel=new PiChannelDebugInfo((PiChannelDebug) c);
        this.sender=new PiThreadDebugInfo((PiThreadDebug) s);
        this.receiver = new PiThreadDebugInfo((PiThreadDebug) source);
    }

    public TakeOutputCommitmentEvent(PiThreadDebugInfo source, PiChannelDebugInfo c, PiThreadDebugInfo s) {
		super(source, Type.TAKE_OUTPUT_COMMITMENT);
		this.channel=c;
        this.sender= s;
        this.receiver = source;
    }
	/**
	 * 
	 */
	private static final long serialVersionUID = -4018085591120399051L;

	public String toString(){
		return "TAKE_OUTPUT_COMMITMENT\nSource :"+((PiThreadDebug)source).toString()+
						"\nChannel :"+channel.toString()+
						"\nSender :"+sender.toString()+
						"\nend of TAKE_OUTPUT_COMMITMENT\n";
	}
}
