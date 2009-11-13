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
public class CreateThreadEvent extends VisuEvent {
    
    private PiThreadDebugInfo piThread;

 public CreateThreadEvent(Object source) {
        super(source,Type.CREATE_THREAD);
       if(source instanceof PiThreadDebugInfo) this.piThread=(PiThreadDebugInfo) source;
        else this.piThread = new PiThreadDebugInfo((PiThreadDebug)source);
    }

    public PiThreadDebugInfo getPiThread() {
        return (PiThreadDebugInfo) piThread;
    }

}