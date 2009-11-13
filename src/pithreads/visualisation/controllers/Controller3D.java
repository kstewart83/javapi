/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.controllers;

import java.util.ArrayDeque;
import java.util.logging.Level;
import java.util.logging.Logger;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.modelUpdater.IModelUpdate;
import pithreads.visualisation.modelUpdater.ModelUpdate;
import pithreads.visualisation.view.Renderer;
import pithreads.visualisation.visuEvent.VisuEvent;

/**
 *
 * @author mathurin
 */
public class Controller3D extends Thread implements IController3D {

    private ArrayDeque<VisuEvent> eventList;
    private Renderer renderer;
    private IModelUpdate modelUpdater;
    private IModel model;

    public Controller3D(Renderer renderer, IModel model) {

        this.renderer = renderer;
        this.model = model;
        this.modelUpdater = new ModelUpdate(renderer, model, this);
        eventList = new ArrayDeque<VisuEvent>();
    }

    public void receive(VisuEvent v) {
        this.eventList.addFirst(v);
    }

    @Override
    public void run() {
        while (true) {
            if (!eventList.isEmpty()) {
                VisuEvent e = this.eventList.getLast();
                this.eventList.removeLast();
                switch (e.getType()) {
                    case CREATE_ARROW_IN: {
                        modelUpdater.createArrowIn(e);
                        break;
                    }
                    case CREATE_ARROW_OUT: {
                        modelUpdater.createArrowOut(e);
                        break;
                    }
                    case SEND_MESSAGE_IN: {
                        modelUpdater.sendMessageIn(e);
                        break;
                    }
                    case SEND_MESSAGE_OUT: {
                        modelUpdater.sendMessageOut(e);
                        break;
                    }
                    case CREATE_OR_CHECK_CHANNEL: {
                        modelUpdater.createOrCheckChannel(e);
                        break;
                    }
                    case CHANGE_COLOR_RELEASED: {
                        modelUpdater.changeColorReleased(e);
                        break;
                    }
                    case CHANGE_COLOR_FAILED: {
                        //TODO : le failed Aquired niveau graphique
                        System.out.println("CHANGE_COLOR_FAILED");
                        break;
                    }
                    case CREATE_THREAD: {
                        modelUpdater.createThread(e);
                        break;
                    }
                    case CHANGE_COLOR_THREAD_AWAKE: {
                        modelUpdater.changeColorThreadAwake(e);
                        break;
                    }
                    case CHANGE_COLOR_THREAD_WAIT: {
                        modelUpdater.changeColorThreadWait(e);
                        break;
                    }
                    case UNREGISTER_THREAD: {

                        modelUpdater.unregisterThread(e);
                        try {
                            this.sleep(model.getMovementSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Controller3D.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    }

                }
            }
            try {
                this.sleep(model.getMilliSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void receiveImmediatly(VisuEvent e) {
        switch (e.getType()) {
            case OVERLIGNE_THE_THREAD: {
                modelUpdater.overligneThread(e);
                break;
            }
            case OVERLIGNE_THE_CHANNEL: {
                modelUpdater.overligneChannel(e);
                break;
            }
            case CLEAR_ALL: {
                this.eventList.clear();
                break;
            }
        }
    }

    public void refreshModelAndRepaint() {
        while (renderer == null || renderer.getDrawable() == null) {
            try {
                this.sleep(1);
                System.out.println("renderer non instancié");
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller3D.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        model.setChanged(true);
        renderer.setModel(model);
        renderer.getDrawable().repaint();
        model.setChanged(false);
        renderer.setModel(model);
        renderer.getDrawable().repaint();
    }
}

