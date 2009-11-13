/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.debug.PiChannelDebug;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class SendMessageInEvent extends VisuEvent{
    private PiChannelDebugInfo channel;
    private PiThreadDebugInfo receiver;
    private PiThreadDebugInfo s;

 public SendMessageInEvent(Object source,Object receive, Object channel) {
        super(source,Type.SEND_MESSAGE_IN);
        this.source=source;

        this.channel = (PiChannelDebugInfo) channel;
        this.receiver = (PiThreadDebugInfo) receive;
       if(source instanceof PiThreadDebugInfo) this.s = (PiThreadDebugInfo) source;
        else this.s= new PiThreadDebugInfo((PiThreadDebug)source);
       //FIXME : bizard ici
        // System.out.println("\nle receiver = "+receive);
    }

    public PiChannelDebugInfo getPiChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThreadDest() {
        return (PiThreadDebugInfo) receiver;
    }

    public PiThreadDebugInfo getPiThreadSource() {
        return (PiThreadDebugInfo) s;
    }
}
