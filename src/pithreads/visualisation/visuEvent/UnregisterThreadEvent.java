/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;
import pithreads.visualisation.visuEvent.VisuEvent.Type;
import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.debug.PiThreadDebugInfo;

/**
 *
 * @author mathurin
 */
public class UnregisterThreadEvent extends VisuEvent {
 private Object piThread;

 public UnregisterThreadEvent(Object source) {
        super(source,Type.UNREGISTER_THREAD);
        this.piThread=new PiThreadDebugInfo((PiThreadDebug)source);
    }

 public PiThreadDebugInfo getPiThread() {
     return (PiThreadDebugInfo) piThread;
    }

}
