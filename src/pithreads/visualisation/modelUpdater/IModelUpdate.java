/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.modelUpdater;

import pithreads.visualisation.visuEvent.VisuEvent;

/**
 *
 * @author mathurin
 */
public interface IModelUpdate {

    public IArranger getReorganisation3D();

    public IArranger getReorganisation2D();

    public void overligneChannel(VisuEvent e);

    public void overligneThread(VisuEvent e);

    public void unregisterThread(VisuEvent e);
    
    public void changeColorReleased(VisuEvent e) ;

    public void changeColorThreadAwake(VisuEvent e) ;

    public void changeColorThreadWait(VisuEvent e) ;

    public void createArrowIn(VisuEvent e) ;

    public void createArrowOut(VisuEvent e) ;
    
    public void createThread(VisuEvent e) ;

    public void sendMessageIn(VisuEvent e) ;

    public void sendMessageOut(VisuEvent e) ;

    public void createOrCheckChannel(VisuEvent e) ;

}
