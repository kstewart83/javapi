/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.visuEvent;

import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class CreateArrowInEvent extends VisuEvent {

    private Object channel;
    private PiThreadDebugInfo pithreadInfo;

    public CreateArrowInEvent(Object source, Object channel) {
        super(source, Type.CREATE_ARROW_IN);
        this.channel = channel;
        if (source instanceof PiThreadDebugInfo) {
            pithreadInfo = (PiThreadDebugInfo) source;
        } else {
            this.pithreadInfo = new PiThreadDebugInfo((PiThreadDebug) source);
        }
    }

    public PiChannelDebugInfo getChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) pithreadInfo;
    }
}
