/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class CreateOrCheckChannelInGreenEvent extends VisuEvent {

 private Object channel;
    
 public CreateOrCheckChannelInGreenEvent(Object source,Object channel) {
        super(source,Type.CREATE_OR_CHECK_CHANNEL);
        this.source = source;
        this.channel = channel;
    }

    public PiChannelDebugInfo getPiChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThread() {
       return (PiThreadDebugInfo) source;
    }
}
