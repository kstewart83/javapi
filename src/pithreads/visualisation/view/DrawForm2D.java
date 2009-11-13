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
public class DrawForm2D implements IDrawForm{

    private GL gl;
    private GLU glu;

    public DrawForm2D(GL gl) {
        this.gl = gl;
        this.glu = new GLU();
    }



    public void drawArrow(IArrow arrow) {
        gl.glColor3f(arrow.getColor().getRed(),arrow.getColor().getGreen(),arrow.getColor().getBlue());
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(arrow.getTripletSource2D().getX(),arrow.getTripletSource2D().getY(),arrow.getTripletSource2D().getZ());
        gl.glVertex3f(arrow.getTripletDest2D().getX(),arrow.getTripletDest2D().getY(),arrow.getTripletDest2D().getZ());
        gl.glEnd();
        //code interressant pour les mouvements !!!
        float x=arrow.getTripletSource2D().getX()-arrow.getTripletDest2D().getX();
        float y= arrow.getTripletSource2D().getY()-arrow.getTripletDest2D().getY();
        float z= arrow.getTripletSource2D().getZ()-arrow.getTripletDest2D().getZ();
        float headArrowSize = 0.3f;

        //t est le point d'intersection du triangle tete de fleche et de la fleche elle meme;
        Triplet hArrow = new Triplet(x*headArrowSize+arrow.getTripletDest2D().getX(),
                                    y*headArrowSize+arrow.getTripletDest2D().getY(),
                                    z*headArrowSize+arrow.getTripletDest2D().getZ());
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(arrow.getTripletDest2D().getX(),arrow.getTripletDest2D().getY(),arrow.getTripletDest2D().getZ());
        gl.glVertex3f(hArrow.getX(),hArrow.getY(),hArrow.getZ());
        gl.glVertex3f(hArrow.getX()-0.2f,hArrow.getY(),hArrow.getZ());
        gl.glEnd();
        gl.glBegin(gl.GL_TRIANGLES);
        gl.glVertex3f(arrow.getTripletDest2D().getX(),arrow.getTripletDest2D().getY(),arrow.getTripletDest2D().getZ());
        gl.glVertex3f(hArrow.getX(),hArrow.getY(),hArrow.getZ());
        gl.glVertex3f(hArrow.getX()+0.2f,hArrow.getY(),hArrow.getZ());
        gl.glEnd();
    }

    public void drawMovement(IMovement g) {
        gl.glPushMatrix();
        gl.glTranslatef(g.getPosCourante2D().getX(), g.getPosCourante2D().getY(), g.getPosCourante2D().getZ());
        gl.glColor3f(g.getColor().getRed(),g.getColor().getGreen(),g.getColor().getBlue());
        glu.gluDisk(glu.gluNewQuadric(),0.0f,0.3f, 100, 100);
        gl.glPopMatrix();
    }

    public void drawSphere(ISphere s) {
        gl.glPushMatrix();
        gl.glTranslatef(s.getPosition2D().getX(), s.getPosition2D().getY(), s.getPosition2D().getZ());
        gl.glColor3f(s.getColor().getRed(), s.getColor().getGreen(), s.getColor().getBlue());
        glu.gluDisk(glu.gluNewQuadric(),s.getRayon()/4 ,s.getRayon(), 100, 100);
        gl.glPopMatrix();
        if(s.isOverligned()) drawCubeSelection(s);
    }

    public void drawDemiSphere(ISphere s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawCube(ICube c) {
        gl.glPushMatrix();
        gl.glTranslatef(c.getPosX2D(), c.getPosY2D(), c.getPosZ2D());
        gl.glColor3f(c.getColor().getRed(), c.getColor().getGreen(), c.getColor().getBlue());
        carre(c.getCote());
        gl.glPopMatrix();
        
        if(c.isOverligned())
            drawCubeSelection(c);
    }

    public void drawAxes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawCylindre(ICylindre c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void carre(float cote) {
        float cot = cote / 2.0f;
        gl.glBegin(gl.GL_QUADS);
        gl.glVertex3f(cot, -cot, 0.0f);
        gl.glVertex3f(cot, cot, 0.0f);
        gl.glVertex3f(-cot, cot, 0.0f);
        gl.glVertex3f(-cot, -cot, 0.0f);
        gl.glEnd();
    }
    
    private void drawCubeSelection(ISphere cs) {

        gl.glPushMatrix();
        gl.glTranslatef(cs.getPosition2D().getX(), cs.getPosition2D().getY(), cs.getPosition2D().getZ());
        gl.glColor3f(1.0f,1.0f,1.0f);
        //carre du bas (sol)
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, -1, 0);
        gl.glVertex3f(1, -1, 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, -1, 0);
        gl.glVertex3f(1, 1, 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(1, 1, 0);
        gl.glVertex3f(-1, 1, 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-1, 1, 0);
        gl.glVertex3f(-1, -1, 0);
        gl.glEnd();
        gl.glPopMatrix();
    }
private void drawCubeSelection(ICube cs) {

        gl.glPushMatrix();
        gl.glTranslatef(cs.getPosition2D().getX(), cs.getPosition2D().getY(), cs.getPosition2D().getZ());
        gl.glColor3f(1.0f,1.0f,1.0f);
        //carre du bas (sol)
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(cs.getCote()+0.1f), -(cs.getCote()+0.1f), 0);
        gl.glVertex3f((cs.getCote()+0.1f), -(cs.getCote()+0.1f), 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((cs.getCote()+0.1f), -(cs.getCote()+0.1f), 0);
        gl.glVertex3f((cs.getCote()+0.1f), (cs.getCote()+0.1f), 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f((cs.getCote()+0.1f), (cs.getCote()+0.1f), 0);
        gl.glVertex3f(-(cs.getCote()+0.1f), (cs.getCote()+0.1f), 0);
        gl.glEnd();
        gl.glBegin(gl.GL_LINES);
        gl.glVertex3f(-(cs.getCote()+0.1f), (cs.getCote()+0.1f), 0);
        gl.glVertex3f(-(cs.getCote()+0.1f), -(cs.getCote()+0.1f), 0);
        gl.glEnd();
        gl.glPopMatrix();
    }
}
