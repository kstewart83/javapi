/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

import javax.media.opengl.GL;
import pithreads.visualisation.tools.Triplet;

/**
 *
 * @author mathurin
 */
public class Light {

    private GL gl;
    private float[] lightAmbient = {0.2f, 0.2f, 0.2f, 1.0f};
    private float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
    private float[] lightPosition0;
    //  private float[] lightPosition1 = {posXSave-1.0f, posYSave-2.0f, posZSave+8.0f, 1.0f};

    public Light(GL gl, Triplet t) {
        this.gl = gl;
        lightPosition0 = new float[4];
        lightPosition0[0] =t.getX() + 1.0f;
        lightPosition0[1] = t.getY() + 2.0f;
        lightPosition0[2] = t.getZ() + 4.0f;
        lightPosition0[3] = 1.0f;
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, this.lightDiffuse, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, this.lightPosition0, 0);
         gl.glLightfv(GL.GL_LIGHT2, GL.GL_AMBIENT, this.lightAmbient, 0);
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHT2);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glEnable(GL.GL_DEPTH_TEST);
    }
}
