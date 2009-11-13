/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class ThreadInListSelectedEvent extends VisuEvent {

    private Object idThread;

    public ThreadInListSelectedEvent(Object source, Object idThead) {
        super(source,Type.THREAD_IN_LIST_SELECTED);
        this.source=source;
        this.idThread=idThead;
    }

    public int getPiThreadId() {
        return (Integer) idThread;
    }

}
