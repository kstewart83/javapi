/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

/**
 *
 * @author mathurin
 */
public interface ICamera {

    public void setPosX(float x);

    public float getPosX();

    public float getPosY();

    public void setPosY(float f);

    public float getPosZ();

    public void setPosZ(float f);

    public float getAngleY();

    public void setAngleY(float f);

    public float getAngleX();

    public void setAngleX(float f);

    public void resetCamera();
}
