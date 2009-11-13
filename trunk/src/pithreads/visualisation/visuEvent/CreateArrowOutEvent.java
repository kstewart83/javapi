/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class CreateArrowOutEvent extends VisuEvent{
    private Object channel;
    private PiThreadDebugInfo pithreadInfo;
 public CreateArrowOutEvent(Object source,Object channel) {
        super(source,Type.CREATE_ARROW_OUT);
        this.channel=channel;
       if(source instanceof PiThreadDebugInfo) this.pithreadInfo = (PiThreadDebugInfo) source;
        else this.pithreadInfo=new PiThreadDebugInfo((PiThreadDebug)source);
    }

    public PiChannelDebugInfo getChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) pithreadInfo;
    }
}
