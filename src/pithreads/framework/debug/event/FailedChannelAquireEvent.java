package pithreads.framework.debug.event;

import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class FailedChannelAquireEvent extends ControlEvent{

	private PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThread;

	public PiChannelDebugInfo getChannel(){return channel;}
	public PiThreadDebugInfo getThread(){return piThread;}
	
	public FailedChannelAquireEvent(Object source, PiChannel<?> c) {
		super(source, Type.FAILED_AQUIRE_CHANNEL);
		this.channel=new PiChannelDebugInfo((PiChannelDebug) c);
        this.piThread = new PiThreadDebugInfo((PiThreadDebug) source);
	}

    public FailedChannelAquireEvent(PiThreadDebugInfo source, PiChannelDebugInfo c) {
		super(source, Type.FAILED_AQUIRE_CHANNEL);
		this.channel=c;
        this.piThread =  source;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -4274240113570992785L;

}
