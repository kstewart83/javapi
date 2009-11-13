/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import pithreads.visualisation.model.IArrow;
import pithreads.visualisation.model.ICube;
import pithreads.visualisation.model.ICylindre;
import pithreads.visualisation.model.IMovement;
import pithreads.visualisation.model.ISphere;
import pithreads.visualisation.tools.Triplet;

/**
 *
 * @author mathurin
 */
public class DrawForm3D implements IDrawForm {

    private GL gl;
    private GLU glu;

    public DrawForm3D(GL gl) {
        this.gl = gl;
        this.glu = new GLU();
    }

    public void drawSphere(ISphere s) {
        gl.glPushMatrix();
        gl.glTranslatef(s.getPosition3D().getX(), s.getPosition3D().getY(), s.getPosition3D().getZ());
        gl.glColor3f(s.getColor().getRed(), s.getColor().getGreen(), s.getColor().getBlue());
        glu.gluSphere(glu.gluNewQuadric(), s.getRayon(), 100, 100);
        gl.glPopMatrix();
        if(s.isOverligned()) drawCubeSelection(s);
    }

    public void drawDemiSphere(ISphere s) {
        float omega, teta;
        float x, y, z;

        gl.glColor3f(s.getColor().getRed(), s.getColor().getGreen(), s.getColor().getBlue());
        for (omega = 0; omega < Math.PI; omega += 0.02) {
            for (teta = 0; teta < Math.PI; teta += 0.02) {
                gl.glBegin(GL.GL_QUADS);

                x = (float) (s.getRayon() * Math.sin(teta) * Math.cos(omega));
                y = (float) (s.getRayon() * Math.sin(teta) * Math.sin(omega));
                z = (float) (s.getRayon() * Math.cos(teta));
                gl.glVertex3f(x, y, z);

                x = (float) (s.getRayon() * Math.sin(teta) * Math.cos(omega + 0.02));
                y = (float) (s.getRayon() * Math.sin(teta) * Math.sin(omega + 0.02));
                z = (float) (s.getRayon() * Math.cos(teta));
                gl.glVertex3f(x, y, z);

                x = (float) (s.getRayon() * Math.sin(teta + 0.02) * Math.cos(omega + 0.02));
                y = (float) (s.getRayon() * Math.sin(teta + 0.02) * Math.sin(omega + 0.02));
                z = (float) (s.getRayon() * Math.cos(teta + 0.02));
                gl.glVertex3f(x, y, z);

                x = (float) (s.getRayon() * Math.sin(teta + 0.02) * Math.cos(omega));
                y = (float) (s.getRayon() * Math.sin(teta + 0.02) * Math.sin(omega));
                z = (float) (s.getRayon() * Math.cos(teta + 0.02));
                gl.glVertex3f(x, y, z);
            }
        }
        gl.glEnd();
    }

    private void carre(float cote) {
        float cot = cote / 2.0f;
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(cot, -cot, cot);
        gl.glVertex3f(cot, cot, cot);
        gl.glVertex3f(-cot, cot, cot);
        gl.glVertex3f(-cot, -cot, cot);
        gl.glEnd();
        
    }

    public void drawCube(ICube c) {
        gl.glPushMatrix();
        gl.glTranslatef(c.getPosX3D(), c.getPosY3D(), c.getPosZ3D());
        gl.glColor3f(c.getColor().getRed(), c.getColor().getGreen(), c.getColor().getBlue());
        carre(c.getCote());
        gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
        carre(c.getCote());
        gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
        carre(c.getCote());
        gl.glRotatef(90, 0.0f, 1.0f, 0.0f);
        carre(c.getCote());
        gl.glRotatef(90, 1.0f, 0.0f, 0.0f);
        carre(c.getCote());
        gl.glRotatef(180, 1.0f, 0.0f, 0.0f);
        carre(c.getCote());
        gl.glPopMatrix();
        if(c.isOverligned())
            this.drawCubeSelection(c);
    }

    public void drawAxes() {
        gl.glColor3f(1, 1, 1);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 5, 0);
        gl.glEnd();
        // gl.glColor3f(1,0,1);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(0, 0, 5);
        gl.glEnd();
        //gl.glColor3f(0,1,1);
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(0, 0, 0);
        gl.glVertex3f(5, 0, 0);
        gl.glEnd();
    }
public void drawArrow(IArrow arrow) {
        gl.glColor3f(arrow.getColor().getRed(),arrow.getColor().getGreen(),arrow.getColor().getBlue());
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(arrow.getTripletSource3D().getX(),arrow.getTripletSource3D().getY(),arrow.getTripletSource3D().getZ());
        gl.glVertex3f(arrow.getTripletDest3D().getX(),arrow.getTripletDest3D().getY(),arrow.getTripletDest3D().getZ());
        gl.glEnd();
        //code interressant pour les mouvements !!!
        float x=arrow.getTripletSource3D().getX()-arrow.getTripletDest3D().getX();
        float y= arrow.getTripletSource3D().getY()-arrow.getTripletDest3D().getY();
        float z= arrow.getTripletSource3D().getZ()-arrow.getTripletDest3D().getZ();
        float headArrowSize = 0.3f;

        //t est le point d'intersection du triangle tete de fleche et de la fleche elle meme;
        Triplet hArrow = new Triplet(x*headArrowSize+arrow.getTripletDest3D().getX(),
                                    y*headArrowSize+arrow.getTripletDest3D().getY(),
                                    z*headArrowSize+arrow.getTripletDest3D().getZ());
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(arrow.getTripletDest3D().getX(),arrow.getTripletDest3D().getY(),arrow.getTripletDest3D().getZ());
        gl.glVertex3f(hArrow.getX(),hArrow.getY(),hArrow.getZ());
        gl.glVertex3f(hArrow.getX()-0.2f,hArrow.getY(),hArrow.getZ());
        gl.glEnd();
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(arrow.getTripletDest3D().getX(),arrow.getTripletDest3D().getY(),arrow.getTripletDest3D().getZ());
        gl.glVertex3f(hArrow.getX(),hArrow.getY(),hArrow.getZ());
        gl.glVertex3f(hArrow.getX()+0.2f,hArrow.getY(),hArrow.getZ());
        gl.glEnd();
    }

    public void drawCylindre(ICylindre c) {

        gl.glColor3f(c.getColor().getRed(), c.getColor().getGreen(), c.getColor().getBlue());
        glu.gluCylinder(glu.gluNewQuadric(), c.getRayon(), c.getRayon(), c.getLongueur(), 100, 100);

    }

    public void drawMovement(IMovement g) {
        
        gl.glPushMatrix();
        gl.glTranslatef(g.getPosCourante3D().getX(), g.getPosCourante3D().getY(), g.getPosCourante3D().getZ());
        gl.glColor3f(1.0f,1.0f,1.0f);
        glu.gluSphere(glu.gluNewQuadric(),0.3f, 100, 100);
        gl.glPopMatrix();
}

    private void drawCubeSelection(ICube c) {
        gl.glPushMatrix();
        gl.glTranslatef(c.getPosition3D().getX(), c.getPosition3D().getY(), c.getPosition3D().getZ());
        gl.glColor3f(1.0f,1.0f,1.0f);
        //carre du bas (sol)
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();

        //vertical
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f), (c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f),(c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f), (c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f), (c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f), (c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();

        //plafond
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), (c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f),(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f), (c.getCote()/2+0.1f),(c.getCote()/2+0.1f));
        gl.glVertex3f((c.getCote()/2+0.1f), (c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((c.getCote()/2+0.1f),(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f),(c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(c.getCote()/2+0.1f), (c.getCote()/2+0.1f), -(c.getCote()/2+0.1f));
        gl.glVertex3f(-(c.getCote()/2+0.1f),(c.getCote()/2+0.1f), (c.getCote()/2+0.1f));
        gl.glEnd();
        gl.glPopMatrix();
    }


     private void drawCubeSelection(ISphere cs) {

        gl.glPushMatrix();
        gl.glTranslatef(cs.getPosition3D().getX(), cs.getPosition3D().getY(), cs.getPosition3D().getZ());
        gl.glColor3f(1.0f,1.0f,1.0f);
        //carre du bas (sol)
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, -1, 1);
        gl.glVertex3f(1, -1, 1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, -1, 1);
        gl.glVertex3f(1, -1, -1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, -1, -1);
        gl.glVertex3f(-1, -1, -1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, -1, -1);
        gl.glVertex3f(-1, -1, 1);
        gl.glEnd();

        //vertical
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, -1, 1);
        gl.glVertex3f(-1, 1, 1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, -1, 1);
        gl.glVertex3f(1, 1, 1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, -1, -1);
        gl.glVertex3f(1, 1, -1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, -1, -1);
        gl.glVertex3f(-1, 1, -1);
        gl.glEnd();

        //plafond
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, 1, 1);
        gl.glVertex3f(1, 1, 1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, 1, 1);
        gl.glVertex3f(1, 1, -1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, 1, -1);
        gl.glVertex3f(-1, 1, -1);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, 1, -1);
        gl.glVertex3f(-1, 1, 1);
        gl.glEnd();
        gl.glPopMatrix();
    }
}
