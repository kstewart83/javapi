package pithreads.framework.debug.event;

import java.util.concurrent.atomic.AtomicBoolean;
import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiAgentDebug;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class ChannelAquiredEvent extends ControlEvent{

	private PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThread=null;

    public PiThreadDebugInfo getThread(){return piThread;}
	public PiChannelDebugInfo getChannel(){return channel;}
	
	public ChannelAquiredEvent(Object source, PiChannel<?> c) {
		super(source, Type.CHANNEL_AQUIRED);
		this.channel=new PiChannelDebugInfo((PiChannelDebug) c);
        if(source instanceof PiThreadDebug) this.piThread = new PiThreadDebugInfo((PiThreadDebug) source);
        //if(source instanceof PiAgentDebug) this.piThread = new PiThreadDebugInfo(9999, false, 0,new AtomicBoolean (false), 10,"agent");
	}

    public ChannelAquiredEvent(PiThreadDebugInfo p , PiChannelDebugInfo c) {
		super(p, Type.CHANNEL_AQUIRED);
		this.channel=c;
        this.piThread = p;
        //if(source instanceof PiAgentDebug) this.piThread = new PiThreadDebugInfo(9999, false, 0,new AtomicBoolean (false), 10,"agent");
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -8684565393132115547L;
	
	public String toString(){
		return "CHANNEL_AQUIRED\nSource :"+(/*(PiThreadDebug)*/source).toString()+
						"\nChannel :"+channel.toString()+"\nend of CHANNEL_AQUIRED\n";
	}
	
}
