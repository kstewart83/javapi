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
public class Arrow implements IArrow {

    private boolean isAnArrowIn;
    private int idSphere;
    private int idCube;
    private String idArrow;
    private Triplet source3D;
    private Triplet dest3D;
    private Triplet source2D;
    private Triplet dest2D;

    private Color color;

    public Arrow(boolean isAnArrowIn, int idSphere, int idCube, Color color) {
        this.color=color;
        this.idCube = idCube;
        this.idSphere = idSphere;
        this.isAnArrowIn = isAnArrowIn;
        if (isAnArrowIn) {
            this.idArrow = "in : " + idCube + " -> " + idSphere;
        } else {
            this.idArrow = "out : " + idSphere + " -> " + idCube;
        }
    }

    public String getId() {
        return this.idArrow;
    }

    public int getIdSphere() {
        return this.idSphere;
    }

    public int getIdCube() {
        return this.idCube;
    }

    public boolean getIsAnArrowIn() {
        return this.isAnArrowIn;
    }

    public void setTripletSource3D(float x, float y, float z) {
        this.source3D = new Triplet(x, y, z);
    }

    public void setTripletDest3D(float x, float y, float z) {
        this.dest3D = new Triplet(x, y, z);
    }

    public void setColor(float r, float g, float b) {
        this.color = new Color(r, g, b);
    }

    public Color getColor() {
        return this.color;
    }

    public Triplet getTripletDest3D() {
    return this.dest3D;
    }

    public Triplet getTripletSource3D() {
        return this.source3D;
    }

    public void setTripletDest3D(Triplet t) {
        dest3D=t;
    }

    public void setTripletSource3D(Triplet t) {
        source3D=t;
    }

    public Triplet getTripletDest2D() {
        return this.dest2D;
    }

    public Triplet getTripletSource2D() {
        return this.source2D;
    }

    public void setTripletSource2D(float x, float y, float z) {
        this.source2D = new Triplet(x, y, z);
    }

    public void setTripletDest2D(float x, float y, float z) {
        this.dest2D= new Triplet(x, y, z);
    }

    public void setTripletDest2D(Triplet t) {
        this.dest2D=t;
    }

    public void setTripletSource2D(Triplet t) {
        this.source2D=t;
    }
}
