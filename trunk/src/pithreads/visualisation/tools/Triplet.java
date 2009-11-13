/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.tools;


/**
 *
 * @author mathurin
 */
public class Triplet {

    private float x,  y,  z;

    public Triplet(Triplet position) {
        this.x=position.x;
        this.y=position.y;
        this.z=position.z;
    }

    public Triplet(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void mult(float f) {
        this.x*=f;
        this.y*=f;
        this.z*=f;
    }

    public float norm() {
        return (float) Math.sqrt(x*x + y*y +z*z);
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setTriplet(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

 
    public String toString(){
    return "x="+x+" y="+y+" z="+z;
    }
    public float distance(Triplet t){
    return (float) Math.sqrt((t.x-this.x)*(t.x-this.x)+(t.y-this.y)*(t.y-this.y)+(t.z-this.z)*(t.z-this.z));
    }
}
