/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.visualisation.model;

import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;



/**
 *
 * @author mathurin
 */
public interface ICube {

    public boolean isOverligned();

    public void setOverligne(boolean b);

    public Color getColor();

    public void setColor(Color color);
    public void setColor(float red, float green, float blue);
    public float getPosX3D();
    public float getPosY3D();
    public float getPosZ3D();
    public float getPosX2D();
    public float getPosY2D();
    public float getPosZ2D();
    public float getCote();
    public int getId();
    public Triplet getPosition3D();
    public void setPos3D(float x, float y, float z);
    public void setPos3D(Triplet t);

    public Triplet getPosition2D();
    public void setPos2D(float x, float y, float z);
    public void setPos2D(Triplet t);

    public PiChannelDebugInfo getChannel();
}
