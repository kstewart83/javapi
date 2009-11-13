package pithreads.framework.debug.event;

import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class ChannelReleasedEvent extends ControlEvent{

	PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThread=null;

    public PiThreadDebugInfo getThread(){return piThread;}
	public PiChannelDebugInfo getChannel(){return channel;}
	
	
	public ChannelReleasedEvent(Object source, PiChannel<?> c) {
		super(source, Type.RELEASE_CHANNEL);
		this.channel=new PiChannelDebugInfo((PiChannelDebug) c);
       if(source instanceof PiThreadDebug) this.piThread = new PiThreadDebugInfo((PiThreadDebug) source);
	}
public ChannelReleasedEvent(PiThreadDebugInfo source, PiChannelDebugInfo c) {
		super(source, Type.RELEASE_CHANNEL);
		this.channel=c;
       this.piThread =  source;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -3378156125508921363L;

	public String toString(){
		return "CHANNEL_RELEASED\nSource :"+((PiThreadDebug)source).toString()+
						"\nChannel :"+channel.toString()+"\nend of CHANNEL_RELEASED\n";
	}
	
}
