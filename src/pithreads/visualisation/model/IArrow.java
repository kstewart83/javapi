/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.model;

import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;

/**
 *
 * @author mathurin
 */
public interface IArrow {

    public Color getColor();

    public String getId();

    public int getIdSphere();

    public int getIdCube();

    public boolean getIsAnArrowIn();

    public Triplet getTripletDest3D();

    public Triplet getTripletSource3D();

    public Triplet getTripletDest2D();

    public Triplet getTripletSource2D();

    public void setTripletSource3D(float x, float y, float z);

    public void setTripletDest3D(float x, float y, float z);

    public void setTripletSource2D(float x, float y, float z);

    public void setTripletDest2D(float x, float y, float z);

    public void setColor(float r, float g, float b);
    
    public void setTripletDest3D(Triplet t);

    public void setTripletSource3D(Triplet t);

    public void setTripletDest2D(Triplet t);

    public void setTripletSource2D(Triplet t);
}
