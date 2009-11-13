/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.listView;

import java.awt.Color;

/**
 *
 * @author mathurin
 */
public class MsgLog {

    private String message;
    private Color color;
    private boolean selected;

    public MsgLog(String message, Color color) {
        this.message = message;
        this.color = color;
        this.selected=false;
    }

    public Color getColor() {
        return color;
    }

    public String getMessage() {
        return message;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
       selected=b;
    }
}

