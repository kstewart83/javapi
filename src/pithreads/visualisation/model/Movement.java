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
public class Movement implements IMovement {

    private static int idCpt = 0;
    private int id;
    private int milliSpeed;
    private Triplet ptSource3D;
    private Triplet ptTransition3D;
    private Triplet ptDest3D;

    private Triplet ptSource2D;
    private Triplet ptTransition2D;
    private Triplet ptDest2D;
    
    private Triplet vect13D;
    private Triplet vect23D;

    private Triplet vect12D;
    private Triplet vect22D;
    
    private float sizeVect13D;
    private float sizeVect23D;
    private float sizeVect3D;
    
    private float sizeVect12D;
    private float sizeVect22D;
    private float sizeVect2D;

    private boolean firstSegment3D;
    private boolean isFinished3D;

    private boolean firstSegment2D;
    private boolean isFinished2D;

    private Triplet positionCourante3D;
    private Triplet positionCourante2D;
    private Color color;

    public Movement(int vitesse, Triplet ptSource3D, Triplet ptTransition3D, Triplet ptDest3D,Triplet ptSource2D, Triplet ptTransition2D, Triplet ptDest2D,Color c) {
        this.color=c;
        id = idCpt;
        idCpt++;
        this.milliSpeed = vitesse;
        this.ptSource3D = ptSource3D;
        this.ptDest3D = ptDest3D;
        this.ptTransition3D = ptTransition3D;

        this.ptDest2D=ptDest2D;
        this.ptSource2D=ptSource2D;
        this.ptTransition2D=ptTransition2D;

        this.vect13D = new Triplet(ptTransition3D.getX() - ptSource3D.getX(),
                ptTransition3D.getY() - ptSource3D.getY(),
                ptTransition3D.getZ() - ptSource3D.getZ());
        this.vect23D = new Triplet(ptDest3D.getX() - ptTransition3D.getX(),
                ptDest3D.getY() - ptTransition3D.getY(),
                ptDest3D.getZ() - ptTransition3D.getZ());

        this.vect12D = new Triplet(ptTransition2D.getX() - ptSource2D.getX(),
                ptTransition2D.getY() - ptSource2D.getY(),
                ptTransition2D.getZ() - ptSource2D.getZ());
        this.vect22D = new Triplet(ptDest2D.getX() - ptTransition2D.getX(),
                ptDest2D.getY() - ptTransition2D.getY(),
                ptDest2D.getZ() - ptTransition2D.getZ());

        this.sizeVect13D = this.lenght(vect13D);
        this.sizeVect23D = this.lenght(vect23D);
        this.sizeVect3D = sizeVect13D + sizeVect23D;

        this.sizeVect12D = this.lenght(vect12D);
        this.sizeVect22D = this.lenght(vect22D);
        this.sizeVect2D = sizeVect12D + sizeVect22D;

        this.firstSegment3D = true;
        this.isFinished3D = false;

        this.firstSegment2D = true;
        this.isFinished2D = false;

        this.positionCourante3D = ptSource3D;
        this.positionCourante2D = ptSource2D;
    }

    public int getId() {
        return this.id;
    }

    private float lenght(Triplet t) {
        return (float) Math.sqrt((t.getX() * t.getX()) + (t.getY() * t.getY()) + (t.getZ() * t.getZ()));
    }

    public void updatePosCourante(int sendSpeed) {
        updatePosition3D(sendSpeed);
        updatePosition2D(sendSpeed);
    }

    private void updatePosition3D(int sendSpeed){
        float step = sizeVect3D / (float) (sendSpeed);
        // System.out.println("taille vect = " + sizeVect + " speed = " + milliSpeed + " one step = " + step);
        if (firstSegment3D) {

            float avancementX = step * vect13D.getX();
            float avancementY = step * vect13D.getY();
            float avancementZ = step * vect13D.getZ();

            float avancementLongueur = this.lenght(new Triplet(avancementX, avancementY, avancementZ));

            float proxX = ptTransition3D.getX() - positionCourante3D.getX();
            float proxY = ptTransition3D.getY() - positionCourante3D.getY();
            float proxZ = ptTransition3D.getZ() - positionCourante3D.getZ();


            float longueurRestante = this.lenght(new Triplet(proxX, proxY, proxZ));
        //    System.out.println("longueur restante =" + longueurRestante + " longueur pas =" + avancementLongueur);

            if (longueurRestante > avancementLongueur) {
                float newX = positionCourante3D.getX() + avancementX;
                float newY = positionCourante3D.getY() + avancementY;
                float newZ = positionCourante3D.getZ() + avancementZ;
                positionCourante3D.setX(newX);
                positionCourante3D.setY(newY);
                positionCourante3D.setZ(newZ);
            } else {
               // System.out.println("first segment finished ");
                firstSegment3D = false;
                positionCourante3D.setX(ptTransition3D.getX());
                positionCourante3D.setY(ptTransition3D.getY());
                positionCourante3D.setZ(ptTransition3D.getZ());
            }
        } else {
            float avancementX = step * vect23D.getX();
            float avancementY = step * vect23D.getY();
            float avancementZ = step * vect23D.getZ();


            float avancementLongueur = lenght(new Triplet(avancementX, avancementY, avancementZ));

            float proxX = ptDest3D.getX() - positionCourante3D.getX();
            float proxY = ptDest3D.getY() - positionCourante3D.getY();
            float proxZ = ptDest3D.getZ() - positionCourante3D.getZ();

            float longueurRestante = lenght(new Triplet(proxX,proxY,proxZ));

            if (longueurRestante > avancementLongueur) {

            float newX = positionCourante3D.getX() + avancementX;
            float newY = positionCourante3D.getY() + avancementY;
            float newZ = positionCourante3D.getZ() + avancementZ;

                positionCourante3D.setX(newX);
                positionCourante3D.setY(newY);
                positionCourante3D.setZ(newZ);
            } else {
                isFinished3D = true;
            }
        }
    }

    private void updatePosition2D(int sendSpeed){
      float step = sizeVect2D / (float) (sendSpeed);
        // System.out.println("taille vect = " + sizeVect + " speed = " + milliSpeed + " one step = " + step);
        if (firstSegment2D) {

            float avancementX = step * vect12D.getX();
            float avancementY = step * vect12D.getY();
            float avancementZ = step * vect12D.getZ();

            float avancementLongueur = this.lenght(new Triplet(avancementX, avancementY, avancementZ));

            float proxX = ptTransition2D.getX() - positionCourante2D.getX();
            float proxY = ptTransition2D.getY() - positionCourante2D.getY();
            float proxZ = ptTransition2D.getZ() - positionCourante2D.getZ();


            float longueurRestante = this.lenght(new Triplet(proxX, proxY, proxZ));
        //    System.out.println("longueur restante =" + longueurRestante + " longueur pas =" + avancementLongueur);

            if (longueurRestante > avancementLongueur) {
                float newX = positionCourante2D.getX() + avancementX;
                float newY = positionCourante2D.getY() + avancementY;
                float newZ = positionCourante2D.getZ() + avancementZ;
                positionCourante2D.setX(newX);
                positionCourante2D.setY(newY);
                positionCourante2D.setZ(newZ);
            } else {
               // System.out.println("first segment finished ");
                firstSegment2D = false;
                positionCourante2D.setX(ptTransition2D.getX());
                positionCourante2D.setY(ptTransition2D.getY());
                positionCourante2D.setZ(ptTransition2D.getZ());
            }
        } else {
            float avancementX = step * vect22D.getX();
            float avancementY = step * vect22D.getY();
            float avancementZ = step * vect22D.getZ();


            float avancementLongueur = lenght(new Triplet(avancementX, avancementY, avancementZ));

            float proxX = ptDest2D.getX() - positionCourante2D.getX();
            float proxY = ptDest2D.getY() - positionCourante2D.getY();
            float proxZ = ptDest2D.getZ() - positionCourante2D.getZ();

            float longueurRestante = lenght(new Triplet(proxX,proxY,proxZ));

            if (longueurRestante > avancementLongueur) {

            float newX = positionCourante2D.getX() + avancementX;
            float newY = positionCourante2D.getY() + avancementY;
            float newZ = positionCourante2D.getZ() + avancementZ;

                positionCourante2D.setX(newX);
                positionCourante2D.setY(newY);
                positionCourante2D.setZ(newZ);
            } else {
                isFinished2D = true;
            }
        }
    }

    public Triplet getPosCourante3D() {
        return this.positionCourante3D;
    }

    public boolean isFinished3D() {
        return isFinished3D;
    }

    public Color getColor() {
        return color;
    }

    public Triplet getPosCourante2D() {
        return this.positionCourante2D;
    }

    public boolean isFinished2D() {
        return isFinished2D;
    }
}
