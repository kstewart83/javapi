/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class AwakeThreadEvent  extends VisuEvent {
    private Object piThread;
    private Object s;
 public AwakeThreadEvent(Object source,Object piThread) {
        super(source,Type.CHANGE_COLOR_THREAD_AWAKE);
        this.piThread=new PiThreadDebugInfo((PiThreadDebug)piThread);
        this.s=new PiThreadDebugInfo((PiThreadDebug)source);

    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) s;
    }

}
