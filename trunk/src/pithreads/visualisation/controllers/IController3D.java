/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.controllers;

import pithreads.visualisation.visuEvent.VisuEvent;

/**
 *
 * @author mathurin
 */
public interface IController3D {

    public void receiveImmediatly(VisuEvent v);
    public void receive(VisuEvent v);
    
}
