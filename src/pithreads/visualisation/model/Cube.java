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
public class Cube implements ICube{

    private boolean overligned;
    private float cote;
    private Color color;
    private Triplet position2D;
    private Triplet position3D;
    private int id;
    private PiChannelDebugInfo channelInfo;

    public Cube(float cote, PiChannelDebugInfo channelInfo) {
        this(new Triplet(0.0f, 0.0f, 0.0f),new Triplet(0.0f, 0.0f, 0.0f), cote, new Color(1.0f, 0.0f, 0.0f),channelInfo);
    }

    public Cube(Triplet position2D , Triplet position3D, float cote, Color c,PiChannelDebugInfo channel) {
        this.cote = cote;
        this.color = c;
        this.position2D = position2D;
        this.position3D = position3D;
        this.channelInfo = channel;
        this.id = channel.getId();
        this.overligned=false;
    }

    public void setColor(float red, float green, float blue) {
        color.setColor(red, green, blue);
    }

    public float getPosX3D() {
        return position3D.getX();
    }

    public float getPosY3D() {
        return position3D.getY();
    }

    public float getPosZ3D() {
        return position3D.getZ();
    }

    public float getCote() {
        return cote;
    }

    public int getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color=color;
    }

    public Triplet getPosition3D() {
        return position3D;
    }
    
    public void setPos3D(float x, float y, float z) {
       this.position3D.setTriplet(x, y, z);

    }

    public void setPos3D(Triplet t) {
        this.position3D=t;
    }

    public PiChannelDebugInfo getChannel() {
        return this.channelInfo;
    }

    public float getPosX2D() {
        return this.position2D.getX();
    }

    public float getPosY2D() {
        return this.position2D.getY();
    }

    public float getPosZ2D() {
       return this.position2D.getZ();
    }

    public Triplet getPosition2D() {
        return this.position2D;
    }

    public void setPos2D(float x, float y, float z) {
        this.position2D.setTriplet(x, y, z);
            }

    public void setPos2D(Triplet t) {
        this.position2D.setTriplet(t.getX(),t.getY(),t.getZ());
    }

    public void setOverligne(boolean b) {
        this.overligned=b;
    }

    public boolean isOverligned() {
        return overligned;
    }
}
