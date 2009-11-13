/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.modelUpdater;

import java.util.logging.Level;
import java.util.logging.Logger;
import pithreads.visualisation.model.*;
import pithreads.visualisation.tools.Triplet;
import pithreads.visualisation.view.Color;
import pithreads.visualisation.view.Renderer;
import pithreads.visualisation.visuEvent.*;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.visualisation.controllers.Controller3D;

/**
 *
 * @author mathurin
 */
public class ModelUpdate implements IModelUpdate {

    private Renderer renderer;
    private IModel model;
    private IArranger reOrganizer2D;
    private IArranger reOrganizer3D;
    private Controller3D controller3D;

    public ModelUpdate(Renderer renderer, IModel model, Controller3D c) {
        this.controller3D = c;
        this.renderer = renderer;
        this.model = model;
        reOrganizer2D = new Arranger2D(model, renderer);
        reOrganizer3D = new Arranger3D(model, renderer);
    }

    public void changeColorReleased(VisuEvent e) {

        ChangeColorReleased ccr = e.asChangeColorReleased();
        PiThreadDebugInfo p = ccr.getPiThread();
        PiChannelDebugInfo c = ccr.getPiChannel();
        findSphere(p).setColor(new Color(Color.LIGHT_BLUE));

        if (!model.isAnyArrowForThisChannel(c.getId())) {
            model.removeCube(c.getId());
        } else {
            model.findCube(c.getId()).setColor(new Color(Color.LIGHT_BLUE));
        }

        controller3D.refreshModelAndRepaint();
        System.out.println("CHANGE_COLOR_RELEASED");
    }

    public void changeColorThreadAwake(VisuEvent e) {
        AwakeThreadEvent a = e.asAwakeThreadEvent();
        PiThreadDebugInfo p = a.getPiThread();
        //FIXME: bizarre, thread censé avoir été supprimé
        ISphere sphere = findSphere(p);
        if (sphere == null) {
            return;
        }
        sphere.setColor(new Color(Color.LIGHT_BLUE));
        controller3D.refreshModelAndRepaint();
        System.out.println("CHANGE_COLOR_THREAD_AWAKE");
    }

    public void changeColorThreadWait(VisuEvent e) {
        WaitThreadEvent w = e.asWaitThreadEvent();
        PiThreadDebugInfo p = w.getPiThread();
        ISphere sphere = findSphere(p);
        sphere.setColor(new Color(Color.GREY));
        controller3D.refreshModelAndRepaint();
        System.out.println("CHANGE_COLOR_THREAD_WAIT");
    }

    public void createArrowIn(VisuEvent e) {

        CreateArrowInEvent c = e.asCreateArrowInEvent();
        PiThreadDebugInfo pithread = c.getPiThread();
        PiChannelDebugInfo pichannel = c.getChannel();
        ISphere sphereDest = findSphere(pithread);
        ICube cubeSource = model.findCube(pichannel.getId());
        //creation de la fleche
        Color color = new Color(Color.WHITE);
        IArrow arrow = new Arrow(true, sphereDest.getId(), cubeSource.getId(), color);
        // enregistrement de la fleche dans le model
        model.addArrowToArrowList(arrow);
        controller3D.refreshModelAndRepaint();
        System.out.println("CREATE_ARROW_IN");

    }

    public void createArrowOut(VisuEvent e) {
        CreateArrowOutEvent c = e.asCreateArrowOutEvent();
        PiThreadDebugInfo pithread = c.getPiThread();
        PiChannelDebugInfo pichannel = c.getChannel();
        ISphere sphereSource = findSphere(pithread);
        ICube cubeDest = model.findCube(pichannel.getId());
        //creation de la fleche
        Color color = new Color(Color.WHITE);
        IArrow arrow = new Arrow(false, sphereSource.getId(), cubeDest.getId(), color);
        // enregistrement de la fleche dans le model
        model.addArrowToArrowList(arrow);
        controller3D.refreshModelAndRepaint();
        System.out.println("CREATE_ARROW_OUT");
    }

    public void createThread(VisuEvent e) {
        CreateThreadEvent c = e.asCreateThreadEvent();
        PiThreadDebugInfo p = c.getPiThread();
        ISphere sphere = createSphere(p);
        this.model.addSphereToSphereList(sphere);
        System.out.println("CREATE_THREAD id = " + model.getSphereList().get(model.getSphereList().size() - 1).getId());
        controller3D.refreshModelAndRepaint();
    }

    public void sendMessageIn(VisuEvent e) {

        SendMessageInEvent s = e.asSendMessageInEvent();
        PiThreadDebugInfo piThreadSource = s.getPiThreadSource();
        PiThreadDebugInfo piThreadDest = s.getPiThreadDest();
        PiChannelDebugInfo piChannel = s.getPiChannel();


        //ajout de la fleche : lors du takeInput
        model.addArrowToArrowList(new Arrow(false, piThreadSource.getId(), piChannel.getId(), new Color(Color.WHITE)));
        //creation du movement
        // couleur du mouvement
        Color movementColor = new Color(Color.WHITE);
        //TODO : relayer cette couleur en champs privé de la classe afin de faire des choix dans les menus

        model.addMovementToMovementList(model.getMilliSpeed(), piThreadSource.getId(), piChannel.getId(), piThreadDest.getId(), movementColor);
        controller3D.refreshModelAndRepaint();

        try {
            while (!(model.getMovementList().get(0).isFinished3D() && (model.getMovementList().get(0).isFinished2D()))) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ModelUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }
                model.getMovementList().get(0).updatePosCourante(model.getMovementSpeed());
                controller3D.refreshModelAndRepaint();
            }

            model.removeLastMove();
            String idArrow1 = "out : " + piThreadSource.getId() + " -> " + piChannel.getId();
            String idArrow2 = "in : " + piChannel.getId() + " -> " + piThreadDest.getId();
            model.removeArrow(idArrow2);
            model.removeArrow(idArrow1);
            controller3D.refreshModelAndRepaint();
            System.out.println("SEND_MESSAGE_IN");
        } catch (Exception e1) {
            return;
        }
    }

    public void sendMessageOut(VisuEvent e) {
        SendMessageOutEvent s = e.asSendMessageOutEvent();
        PiThreadDebugInfo piThreadSource = s.getPiThreadSource();
        PiThreadDebugInfo piThreadDest = s.getPiThreadDest();
        PiChannelDebugInfo piChannel = s.getPiChannel();

        Color color = new Color(Color.WHITE);
        IArrow arrow = new Arrow(true, piThreadSource.getId(), piChannel.getId(), color);
        // enregistrement de la fleche dans le model
        model.addArrowToArrowList(arrow);
        // couleur du mouvement
        Color movementColor = new Color(Color.WHITE);
        //enregistrement du mouvement
        model.addMovementToMovementList(model.getMilliSpeed(), piThreadDest.getId(), piChannel.getId(), piThreadSource.getId(), movementColor);

        controller3D.refreshModelAndRepaint();
        try {
            while (!(model.getMovementList().get(0).isFinished3D() && model.getMovementList().get(0).isFinished2D())) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ModelUpdate.class.getName()).log(Level.SEVERE, null, ex);
                }

                model.getMovementList().get(0).updatePosCourante(model.getMovementSpeed());
                controller3D.refreshModelAndRepaint();

            }

            model.removeLastMove();
            String idArrow1 = "out : " + piThreadDest.getId() + " -> " + piChannel.getId();
            String idArrow2 = "in : " + piChannel.getId() + " -> " + piThreadSource.getId();

//suppression des fleches
            model.removeArrow(idArrow2);
            model.removeArrow(idArrow1);
            controller3D.refreshModelAndRepaint();
            System.out.println("SEND_MESSAGE_OUT");
        } catch (Exception e1) {
            return;
        }
    }

    public void overligneThread(VisuEvent e) {
        OverligneTheThread c = e.asOverligneTheThread();
        model.resetOverlignedSphere();
        int id = c.getThreadId();
        if (model.findSphere(id) != null) {
            model.findSphere(id).overligne();
        }
        controller3D.refreshModelAndRepaint();
    }

    public void overligneChannel(VisuEvent e) {
        OverligneTheChannel c = e.asOverligneTheChannel();
        model.resetOverlignedChannel();
        int id = c.getChannelId();
        if (model.findCube(id) != null) {
            model.findCube(id).setOverligne(true);
        }
        controller3D.refreshModelAndRepaint();
    }

    public void createOrCheckChannel(VisuEvent e) {
        //System.out.println("CREATE_OR_CHECK_CHANNEL");
        CreateOrCheckChannelInGreenEvent c = e.asCreateOrCheckChannelInGreenEvent();
        PiThreadDebugInfo p = c.getPiThread();

        if (!(p instanceof PiThreadDebugInfo) || (p == null)) {
            return;
        }

        PiChannelDebugInfo pc = c.getPiChannel();


        int idChannel = pc.getId();
        int idThread = p.getId();
        if (model.findCube(idChannel) == null) {
            ICube cube = createCube(pc);
            model.addCubeToCubeList(cube);
            model.findSphere(idThread).setColor(new Color(Color.GREEN));
        } else {
            model.findCube(idChannel).setColor(new Color(Color.GREEN));
            model.findSphere(p.getId()).setColor(new Color(Color.GREEN));
        }

        controller3D.refreshModelAndRepaint();
        System.out.println("CREATE_OR_CHECK_CHANNEL");
    // System.out.println("CREATE_OR_CHECK_CHANNEL num: " + idChannel + " by " + idThread);
    }

    public void unregisterThread(VisuEvent e) {
        UnregisterThreadEvent u = e.asUnregisterThreadEvent();
        int idSphere = u.getPiThread().getId();
        model.removeSphere(idSphere);

    }

    private ICube createCube(PiChannelDebugInfo pc) {

        //position 3D
        float rayon3D = model.getRayonOfSpherePositionCube();
        float theta3D = (float) ((Math.random() * Math.PI) - Math.PI / 2.0);
        float phi = (float) ((Math.random() * Math.PI * 2) - Math.PI);

        //ici le rayon3D
        Triplet position3d = tripletPositionSphere3D(rayon3D, theta3D, phi);

        //position2D
        float rayon2D = model.getRayonOfCirclePositionCube();
        float theta2D = (float) (Math.random() * Math.PI * 2);
        Triplet position2d = tripletPositionSphere2D(rayon2D, theta2D);

        int cptTest = 0;
        if (model.getCubeList().size() == 0) {
            return new Cube(position2d, position3d, 0.3f, new Color(Color.LIGHT_BLUE), pc);
        }

        while ((isNotFarEnouthToAnOtherCubeIn3D(position3d) ||
                isNotFarEnouthToAnOtherCubeIn2D(position2d)) &&
                cptTest < (model.getCubeList().size() * 10)) {

            theta3D = (float) ((Math.random() * Math.PI) - Math.PI / 2.0);
            phi = (float) ((Math.random() * Math.PI * 2) - Math.PI);
            position3d = tripletPositionSphere3D(rayon3D, theta3D, phi);
            theta2D = (float) (Math.random() * Math.PI * 2);
            position2d = tripletPositionSphere2D(rayon2D, theta2D);
            cptTest++;
        }

        if (cptTest == (model.getCubeList().size()) * 10) {
            model.setRayonOfSpherePositionCube(model.getRayonOfSpherePositionCube() + (0.3f * model.getCubeList().size()));
            reOrganizer3D.reOrganizeAllCubes();
            model.setRayonOfCirclePositionCube(model.getRayonOfCirclePositionCube() + (0.3f * model.getCubeList().size()));
            reOrganizer2D.reOrganizeAllCubes();
            return createCube(pc);
        } else {

            return new Cube(position2d, position3d, 0.3f, new Color(Color.LIGHT_BLUE), pc);
        }
    }

    private ISphere createSphere(PiThreadDebugInfo thread) {

        //position 3D
        float rayon3D = model.getRayonOfSpherePositionSphere();
        float theta3D = (float) ((Math.random() * Math.PI) - Math.PI / 2.0);
        float phi = (float) ((Math.random() * Math.PI * 2) - Math.PI);

        //ici le rayon3D est le premier parametre
        Triplet position3d = tripletPositionSphere3D(rayon3D, theta3D, phi);

        //position2D
        float rayon2D = model.getRayonOfCirclePositionSphere();
        float theta2D = (float) (Math.random() * Math.PI * 2);
        Triplet position2d = tripletPositionSphere2D(rayon2D, theta2D);

        int cptTest = 0;
        if (model.getSphereList().size() == 0) {
            return new Sphere(position3d, position2d, new Color(Color.LIGHT_BLUE), 1.0f, thread);
        }
        while ((isNotFarEnouthToAnOtherSphereIn3D(position3d) ||
                isNotFarEnouthToAnOtherSphereIn2D(position2d)) &&
                cptTest < (model.getSphereList().size() * 10)) {
            theta3D = (float) ((Math.random() * Math.PI) - Math.PI / 2.0);
            phi = (float) ((Math.random() * Math.PI * 2) - Math.PI);
            position3d = tripletPositionSphere3D(rayon3D, theta3D, phi);
            theta2D = (float) (Math.random() * Math.PI * 2);
            position2d = tripletPositionSphere2D(rayon2D, theta2D);
            cptTest++;
        }

        if (cptTest == (model.getSphereList().size() * 10)) {
            model.setRayonOfSpherePositionSphere(model.getRayonOfSpherePositionSphere() + (0.3f * model.getSphereList().size()));
            reOrganizer3D.reOrganizeAllSpheres();
            model.setRayonOfCirclePositionSphere(model.getRayonOfCirclePositionSphere() + (0.3f * model.getSphereList().size()));
            reOrganizer2D.reOrganizeAllSpheres();
            return createSphere(thread);
        } else {

            return new Sphere(position3d, position2d, new Color(Color.LIGHT_BLUE), 1.0f, thread);
        }
    }

    private ISphere findSphere(PiThreadDebugInfo p) {
        for (int i = 0; i < model.getSphereList().size(); i++) {
            if (p.getId() == model.getSphereList().get(i).getId()) {
                return model.getSphereList().get(i);
            }
        }
        return null;
    }

    private boolean isNotFarEnouthToAnOtherSphereIn3D(Triplet t) {
        boolean result = false;
        for (int i = 0; i < model.getSphereList().size(); i++) {
            if (t.distance(model.getSphereList().get(i).getPosition3D()) <= model.getSphereList().get(i).getRayon() * 4) {
                result = true;
            }
        }
        return result;
    }

    private boolean isNotFarEnouthToAnOtherSphereIn2D(Triplet t) {
        boolean result = false;
        for (int i = 0; i < model.getSphereList().size(); i++) {
            if (t.distance(model.getSphereList().get(i).getPosition2D()) <= model.getSphereList().get(i).getRayon() * 4) {
                result = true;
            }
        }
        return result;
    }

    private boolean isNotFarEnouthToAnOtherCubeIn3D(Triplet t) {
        boolean result = false;
        for (int i = 0; i < model.getCubeList().size(); i++) {
            if (t.distance(model.getCubeList().get(i).getPosition3D()) <= model.getCubeList().get(i).getCote() * 4) {
                result = true;
            }
        }
        return result;
    }

    private boolean isNotFarEnouthToAnOtherCubeIn2D(Triplet t) {
        boolean result = false;
        for (int i = 0; i < model.getCubeList().size(); i++) {
            if (t.distance(model.getCubeList().get(i).getPosition2D()) <= model.getCubeList().get(i).getCote() * 4) {
                result = true;
            }
        }
        return result;
    }

    private Triplet tripletPositionSphere2D(float rayon2D, float theta2D) {
        float x = (float) (rayon2D * Math.cos(theta2D));
        float y = (float) (rayon2D * Math.sin(theta2D));
        float z = 0.0f;
        return new Triplet(x, y, z);
    }

    private Triplet tripletPositionSphere3D(float rayon, float theta, float phi) {
        float x = (float) (rayon * Math.cos(theta) * Math.cos(phi));
        float y = (float) (rayon * Math.cos(theta) * Math.sin(phi));
        float z = (float) (rayon * Math.sin(theta));
        return new Triplet(x, y, z);
    }

    public IArranger getReorganisation3D() {
        return this.reOrganizer3D;
    }

    public IArranger getReorganisation2D() {
        return this.reOrganizer2D;
    }
}
