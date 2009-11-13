package pithreads.visualisation.model;

import pithreads.visualisation.view.Color;

/**
 *
 * @author lucas
 */
public class Cylindre implements ICylindre{
    private int id;
    private Color color;
    private float rayon;
    private float longueur;

    public Cylindre(int id){
        this(id,new Color(1.0f, 0.0f, 0.0f),0.2f,0.7f);
    }

    public Cylindre(int id, Color c, float r, float l){
        this.id=id;
        this.color=c;
        this.rayon=r;
        this.longueur=l;
    }

    public int getId() {
       return this.id;
    }

    public Color getColor() {
      return this.color;
    }

    public void setColor(float r, float g, float b) {
       this.color=new Color(r, g, b);
    }

    public float getRayon() {
     return this.rayon;
    }

    public void setRayon(float r) {
       this.rayon=r;
    }

    public float getLongueur() {
        return this.longueur;
    }

    public void setLongueur(float l) {
       this.longueur=l;
    }


}
