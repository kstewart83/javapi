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
public class WaitThreadEvent extends VisuEvent {
    private PiThreadDebugInfo thread;
    private PiThreadDebugInfo s;

    public WaitThreadEvent(Object source,Object thread) {
        super(source,Type.CHANGE_COLOR_THREAD_WAIT);
        this.source=source;
        this.thread=new PiThreadDebugInfo((PiThreadDebug)thread);
        this.s=new PiThreadDebugInfo((PiThreadDebug)source);
    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) this.s;
    }

}
