/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.tools.Triplet;

/**
 *
 * @author mathurin
 */
public class Renderer implements GLEventListener {

    private GLU glu;
    private GL gl;
    private IDrawForm drawForm;
    private GLAutoDrawable drawable;
    private int width;
    private int height;
    private ICamera camera;
    private IModel model;
    //  mode   0 : 2D et 1 : 3D
    private int mode;

    public ICamera getCamera() {
        return this.camera;
    }

    public GLAutoDrawable getDrawable() {
        return this.drawable;
    }

    public int getMode() {
        return this.mode;
    }


    public void init(GLAutoDrawable drawable) {
        // Use debug pipeline
        camera = new Camera();
        gl = drawable.getGL();
        // Enable VSync
        gl.setSwapInterval(1);
        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glShadeModel(GL.GL_SMOOTH); // try setting this to GL_FLAT and see what happens.
        gl.glHint(gl.GL_PERSPECTIVE_CORRECTION_HINT, gl.GL_NICEST);
        new Light(gl, new Triplet(0.0f, 0.0f, 0.0f));
       // new Light(gl, new Triplet(0.0f, 0.0f, 10.0f));
       // new Light(gl, new Triplet(0.0f, -10.0f, 0.0f));
       // new Light(gl, new Triplet(0.0f, 10.0f, 0.0f));
        drawForm = new DrawForm2D(gl);
        mode=0;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        this.drawable = drawable;
        gl = drawable.getGL();
        glu = new GLU();
        this.width = width;
        this.height = height;
        if (height <= 0) { // avoid a divide by zero error!
            height = 1;
        }
        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45.0f, h, 1.0, 1000.0);
        gl.glMatrixMode(GL.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void display(GLAutoDrawable drawable) {
        
        gl = drawable.getGL();
        // Clear the drawing area
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        // Reset the current matrix to the "identity"
        gl.glLoadIdentity();
        // Move the "drawing cursor" around
        if(mode==0){
        gl.glTranslatef(camera.getPosX(), camera.getPosY(), camera.getPosZ()); 

        }
        if(mode==1){
        gl.glTranslatef(camera.getPosX(), camera.getPosY(), camera.getPosZ());
        gl.glRotatef(camera.getAngleY(), 0.0f, 1.0f, 0.0f);
        gl.glRotatef(-camera.getAngleX(), 1.0f, 0.0f, 0.0f);
        }
        
        for (int i = 0; i < model.getSphereList().size(); i++) {
            drawForm.drawSphere(model.getSphereList().get(i));

        }
        for (int i = 0; i < model.getCubeList().size(); i++) {
            drawForm.drawCube(model.getCubeList().get(i));
        }
        for (int i = 0; i < model.getArrowList().size(); i++) {
            drawForm.drawArrow(model.getArrowList().get(i));
        }
        for (int i=0 ; i< model.getMovementList().size();i++){
        
            drawForm.drawMovement(model.getMovementList().get(i));
        }

        gl.glFlush();
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void setMode(int i) {
        this.mode=i;
        if(i==0) {
            drawForm = new DrawForm2D(gl);}
        if(i==1) {
            drawForm = new DrawForm3D(gl);}
    }

    public void setModel(IModel model) {
        this.model = model;
    }
}