/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class NothingEvent extends VisuEvent{

    public NothingEvent(Object source){
        super(source,Type.NOTHING);
        this.source=source;
    }
}
