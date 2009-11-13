/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.model;

import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;
import pithreads.framework.debug.PiThreadDebugInfo;


/**
 *
 * @author mathurin
 */
public interface ISphere {
    
    public boolean isOverligned();
    public void overligne();
    public void overligne(boolean b);
    public String toString();
    public void setColor(Color c);
    public void setColor(float red, float green, float blue);
    public Color getColor();
   /* public float getPosX();
    public float getPosY();
    public float getPosZ();*/
    public float getRayon();
    public int getId();
    public Triplet getPosition3D();
    public void setPosition3D(float x, float y,float z);
    public void setPosition3D(Triplet t);
    public Triplet getPosition2D();
    public void setPosition2D(float x, float y, float z);
    public void setPosition2D(Triplet t);
    public PiThreadDebugInfo getThread();
}
