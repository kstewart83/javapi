/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.model;

import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;


/**
 *
 * @author lucas
 */
public interface IMovement {

    public Color getColor();

    public int getId();
    public Triplet getPosCourante3D();
    public Triplet getPosCourante2D();

    public boolean isFinished3D();
    public boolean isFinished2D();
    public void updatePosCourante(int sendSpeed);
   
}
