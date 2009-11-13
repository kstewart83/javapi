/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.model;

import pithreads.visualisation.view.Color;

/**
 *
 * @author mathurin
 */
public interface ICylindre {

    public int getId();
    public Color getColor();
    public void setColor(float r,float g,float b);
    public float getRayon();
    public void setRayon(float r);
    public float getLongueur();
    public void setLongueur(float l);

}
