/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

/**
 *
 * @author mathurin
 */
public class Camera implements ICamera {

    final static private float posXSave = 0.0f;
    final static private float posYSave = 0.0f;
    final static private float posZSave = -30.0f;
    private float posSceneX;
    private float posSceneY;
    private float posSceneZ;
    private float angleViewY;
    private float angleViewX;

    public Camera() {
        this(posXSave, posYSave, posZSave);
    }

    public Camera(float x, float y, float z) {
        posSceneX = x;
        posSceneY = y;
        posSceneZ = z;
    }

    public void setPosX(float x) {
        this.posSceneX = x;
    }

    public float getPosX() {
        return this.posSceneX;
    }

    public float getPosY() {
        return this.posSceneY;
    }

    public void setPosY(float f) {
        this.posSceneY = f;
    }

    public float getPosZ() {
        return this.posSceneZ;
    }

    public void setPosZ(float f) {
        this.posSceneZ = f;
    }

    public float getAngleY() {
        return angleViewY;
    }

    public void setAngleY(float f) {
        this.angleViewY = f;
    }

    public float getAngleX() {
        return angleViewX;
    }

    public void setAngleX(float f) {
        this.angleViewX = f;
    }

    public void resetCamera() {
        this.posSceneX = posXSave;
        this.posSceneY = posYSave;
        this.posSceneZ = posZSave;
        this.angleViewX = 0.0f;
        this.angleViewY = 0.0f;
    }
}
