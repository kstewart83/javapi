/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.visualisation.visuEvent.VisuEvent.Type;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class SendMessageOutEvent extends VisuEvent {
    private PiChannelDebugInfo channel;
    private PiThreadDebugInfo piThreadDest;
    private PiThreadDebugInfo piThreadSource;

    public SendMessageOutEvent(Object source, Object piThreadDest,Object channel) {
        super(source,Type.SEND_MESSAGE_OUT);
        this.source=source;
        this.channel=(PiChannelDebugInfo) channel;
        this.piThreadDest = (PiThreadDebugInfo) piThreadDest;
       if(source instanceof PiThreadDebugInfo) this.piThreadSource=(PiThreadDebugInfo) source;
           else this.piThreadSource= new PiThreadDebugInfo((PiThreadDebug)source);
    }

    public PiChannelDebugInfo getPiChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThreadDest() {
     return (PiThreadDebugInfo) piThreadDest;
    }

    public PiThreadDebugInfo getPiThreadSource() {
        return (PiThreadDebugInfo) piThreadSource;
    }

}
