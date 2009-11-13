/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.visuEvent;

/**
 *
 * @author mathurin
 */
public class ClearAllEvent extends VisuEvent {

    public ClearAllEvent(Object source) {
        super(source, Type.CLEAR_ALL);
    }
}