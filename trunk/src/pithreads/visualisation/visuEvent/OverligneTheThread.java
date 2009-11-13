/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class OverligneTheThread extends VisuEvent {

    private Object idThread;

    public OverligneTheThread(Object source,Object idThread){
    super(source,Type.OVERLIGNE_THE_THREAD);
    this.idThread = idThread;
    }

    public int getThreadId() {
        return (Integer) idThread;
    }
}
