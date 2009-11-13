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
public class ChangeColorReleased extends VisuEvent{
  private Object channel;
  public ChangeColorReleased(Object source,Object channel) {
        super(source,Type.CHANGE_COLOR_RELEASED);
        this.source=source;
        this.channel=channel;
    }

    public PiChannelDebugInfo getPiChannel() {
        return (PiChannelDebugInfo) channel;
    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) source;
    }
    
}
