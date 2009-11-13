package pithreads.visualisation.model;

import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;
import pithreads.framework.debug.PiThreadDebugInfo;


/**
 *
 * @author mathurin
 */
public class Sphere implements ISphere{

    private boolean overligned;
    private float rayon;
    private Color color;
    private Triplet position3D;
    private Triplet position2D;
    private int id;
    private PiThreadDebugInfo thread;


    public Sphere(float rayon,PiThreadDebugInfo thread) {
        this(new Triplet(0.0f, 0.0f, 0.0f), new Triplet(0.0f, 0.0f, 0.0f),new Color(1.0f, 0.0f, 0.0f),rayon,thread);
    }

    public Sphere(Triplet position3D, Triplet position2D, Color c,float r,PiThreadDebugInfo thread) {
        this.rayon = r;
        this.color=c;
        this.position3D=position3D;
        this.position2D=position2D;
        this.thread=thread;
        this.id=thread.getId();
        this.overligned=false;
    }

    // draw a sphere composed of multiple quads
    

    public void setColor(float red, float green, float blue) {
        color.setColor(red, green, blue);
    }
public Color getColor(){
    return color;
    }
/*    public float getPosX() {
        return position3D.getX();
    }

    public float getPosY() {
        return position3D.getY();
    }

    public float getPosZ() {
        return position3D.getZ();
    }
*/
    public float getRayon(){
        return rayon;
    }

    public int getId() {
        return this.id;
    }

    public void setColor(Color c) {
        this.color=c;
    }

    public Triplet getPosition3D() {
       return position3D;
    }
    
    public Triplet getPosition2D() {
       return position2D;
    }
    
    public void setPosition3D(float x, float y, float z) {
       this.position3D.setTriplet(x, y, z);

    }

    public void setPosition3D(Triplet t) {
        this.position3D=t;
    }

    public void setPosition2D(float x, float y, float z) {
       this.position2D.setTriplet(x, y, z);

    }

    public void setPosition2D(Triplet t) {
        this.position2D=t;
    }

    public String toString(){

    return "nom: "+thread.getName();
    }

    public PiThreadDebugInfo getThread() {
        return thread;
    }

    public void overligne() {
        this.overligned=true;
    }

    public void overligne(boolean b) {
       this.overligned=b;
    }

    public boolean isOverligned() {
        return overligned;
    }
    
}
