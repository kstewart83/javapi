/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

/**
 *
 * @author mathurin
 */
public class Color {

    public final static int WHITE = 0;
    public final static int RED = 1;
    public final static int GREEN = 2;
    public final static int BLUE = 3;
    public final static int YELLOW = 4;
    public final static int PURPLE = 5;
    public final static int GREY = 6;
    public final static int LIGHT_BLUE = 7;
    private float r,  g,  b;

    public Color(int color) {
        if (color == WHITE) {
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
        }
        if (color == RED) {
            this.r = 1.0f;
            this.g = 0.0f;
            this.b = 0.0f;

        }
        if (color == GREEN) {
            this.r = 0.0f;
            this.g = 1.0f;
            this.b = 0.0f;

        }
        if (color == BLUE) {
            this.r = 0.0f;
            this.g = 0.0f;
            this.b = 1.0f;

        }
        if (color == YELLOW) {
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 0.0f;

        }
        if (color == PURPLE) {
            this.r = 1.0f;
            this.g = 0.0f;
            this.b = 1.0f;

        }
        if (color == GREY) {
            this.r = 0.3f;
            this.g = 0.3f;
            this.b = 0.3f;

        }
        if (color == LIGHT_BLUE) {
            this.r = 0.5f;
            this.g = 0.5f;
            this.b = 1.0f;

        }
    }

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public float getRed() {
        return r;
    }

    public float getGreen() {
        return g;
    }

    public float getBlue() {
        return b;
    }

    public void setColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    public void setColor(int color) {
        if (color == WHITE) {
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 1.0f;
        }
        if (color == RED) {
            this.r = 1.0f;
            this.g = 0.0f;
            this.b = 0.0f;

        }
        if (color == GREEN) {
            this.r = 0.0f;
            this.g = 1.0f;
            this.b = 0.0f;

        }
        if (color == BLUE) {
            this.r = 0.0f;
            this.g = 0.0f;
            this.b = 1.0f;

        }
        if (color == YELLOW) {
            this.r = 1.0f;
            this.g = 1.0f;
            this.b = 0.0f;

        }
        if (color == PURPLE) {
            this.r = 1.0f;
            this.g = 0.0f;
            this.b = 1.0f;

        }
        if (color == GREY) {
            this.r = 0.3f;
            this.g = 0.3f;
            this.b = 0.3f;

        }
        if (color == LIGHT_BLUE) {
            this.r = 0.5f;
            this.g = 0.5f;
            this.b = 1.0f;

        }
    }
}
