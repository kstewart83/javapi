/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class ChangeColorFailedEvent extends VisuEvent {
    private Object channel;

 public ChangeColorFailedEvent(Object source, Object channel) {
        super(source,Type.CHANGE_COLOR_FAILED);
        this.source=source;
        this.channel=channel;
    }
  public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) source;
    }
}
