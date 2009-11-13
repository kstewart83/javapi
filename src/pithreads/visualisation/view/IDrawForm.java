/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.view;

import pithreads.visualisation.model.IArrow;
import pithreads.visualisation.model.ICube;
import pithreads.visualisation.model.ICylindre;
import pithreads.visualisation.model.IMovement;
import pithreads.visualisation.model.ISphere;

/**
 *
 * @author mathurin
 */
public interface IDrawForm {

    public void drawArrow(IArrow ia);

    public void drawMovement(IMovement get);

    public void drawSphere(ISphere s);

    public void drawDemiSphere(ISphere s);

    public void drawCube(ICube c);

    public void drawAxes();

    public void drawCylindre(ICylindre c);
}
