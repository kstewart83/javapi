/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.modelUpdater;

import java.util.logging.Level;
import java.util.logging.Logger;
import pithreads.visualisation.model.ICube;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.model.ISphere;
import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Renderer;

/**
 *
 * @author mathurin
 */
public class Arranger3D implements IArranger {

    private Renderer renderer;
    private IModel model;

    public Arranger3D(IModel model, Renderer renderer) {
        this.renderer = renderer;
        this.model = model;
    }

    public void reOrganizeAllSpheres() {
        try {
            for (int i = 0; i < model.getSphereList().size(); i++) {
                Triplet t = model.getSphereList().get(i).getPosition3D();

                float dist = t.distance(new Triplet(0.0f, 0.0f, 0.0f));
                while (dist <= model.getRayonOfSpherePositionSphere()) {
                    model.getSphereList().get(i).getPosition3D().mult(1.01f);
                    dist = model.getSphereList().get(i).getPosition3D().distance(new Triplet(0.0f, 0.0f, 0.0f));
                    reOrganizeAllArrow();
                    if (renderer.getMode() == 1) {
                        try {
                            Thread.sleep(model.getReOrganisationSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Arranger3D.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        renderer.getDrawable().repaint();
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void reOrganizeAllArrow() {
        try {
            for (int i = 0; i < model.getArrowList().size(); i++) {
                int idSphere = model.getArrowList().get(i).getIdSphere();
                int idCube = model.getArrowList().get(i).getIdCube();
                ISphere sphere = model.findSphere(idSphere);
                ICube cube = model.findCube(idCube);
                if (model.getArrowList().get(i).getIsAnArrowIn()) {
                    model.getArrowList().get(i).setTripletSource3D(cube.getPosition3D());
                    model.getArrowList().get(i).setTripletDest3D(sphere.getPosition3D());
                } else {
                    model.getArrowList().get(i).setTripletDest3D(cube.getPosition3D());
                    model.getArrowList().get(i).setTripletSource3D(sphere.getPosition3D());

                }
            }
        } catch (Exception e) {
            return;
        }
    }

    public void reOrganizeAllCubes() {
        try {
            for (int i = 0; i < model.getCubeList().size(); i++) {
                Triplet t = model.getCubeList().get(i).getPosition3D();

                float dist = t.distance(new Triplet(0.0f, 0.0f, 0.0f));
                while (dist <= model.getRayonOfSpherePositionCube()) {
                    model.getCubeList().get(i).getPosition3D().mult(1.01f);
                    dist = model.getCubeList().get(i).getPosition3D().distance(new Triplet(0.0f, 0.0f, 0.0f));
                    reOrganizeAllArrow();
                    if (renderer.getMode() == 1) {
                        try {
                            Thread.sleep(model.getReOrganisationSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Arranger3D.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        renderer.getDrawable().repaint();
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }
}
