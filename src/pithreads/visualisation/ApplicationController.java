/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation;

/**
 *
 * @author mathurin
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import pithreads.framework.debug.DebugParser;
import pithreads.framework.debug.PiAgentDebug;
import pithreads.framework.debug.Receiver;
import pithreads.framework.debug.event.*;
import pithreads.framework.event.AwakeEvent;
import pithreads.framework.event.ControlEvent;
import pithreads.framework.event.LogEvent;
import pithreads.framework.event.UnregisterEvent;
import pithreads.framework.event.WaitEvent;

import pithreads.visualisation.visuEvent.*;
import pithreads.visualisation.controllers.Controller3D;
import pithreads.visualisation.controllers.IController3D;
import pithreads.visualisation.controllers.ListController;
import pithreads.visualisation.model.IModel;

/**
 *
 * @author mathurin
 */
public class ApplicationController extends Thread {

    private Receiver list;
    private ArrayList<Object> abonne;
    private IModel model;
    private MainFrame frame;

    public ApplicationController(Receiver elist,IModel model) {
        this.list = elist;
        this.abonne = new ArrayList<Object>();
        this.model = model;
        frame = new MainFrame(this);
        frame.setVisible(true);
        frame.getCanvas().addMouseMotionListener(frame);
        frame.getCanvas().addMouseListener(frame);
        frame.getCanvas().addMouseWheelListener(frame);
        frame.getCanvas().addKeyListener(frame);
        frame.setVisible(true);

        
        //on set le model du renderer
        frame.getRenderer().setModel(model);

        Controller3D contoller3D = new Controller3D(frame.getRenderer(), model);
        ListController listController = new ListController(frame.getListOfPiThreads(), frame.getListOfPiChannels(),
                frame.getListOfPiChannelsInformations(), frame.getListOfPiThreadsInformations(), frame.getListOfLogs(), model);

        contoller3D.start();
        listController.start();
        listController.bind(this);

        abonne.add(contoller3D);
        abonne.add(listController);
    }

    public void receive(VisuEvent v) {
        switch (v.getType()) {
            case THREAD_IN_LIST_SELECTED: {
                ThreadInListSelectedEvent ac = v.asThreadInListSelectedEvent();
                for (int i = 0; i < abonne.size(); i++) {
                    ((IController3D) (abonne.get(i))).receiveImmediatly(new OverligneTheThread(ac.getSource(), ac.getPiThreadId()));
                }
                break;
            }
            case CHANNEL_IN_LIST_SELECTED: {
                ChannelInListSelectedEvent ac = v.asChannelInListSelectedEvent();
                for (int i = 0; i < abonne.size(); i++) {
                    ((IController3D) (abonne.get(i))).receiveImmediatly(new OverligneTheChannel(ac.getSource(), ac.getPiChannelId()));
                }
                break;
            }
            case NEW_FILE_TO_PARSE_ENTERED: {
            try {
                NewFileToParseEnteredEvent n = v.asNewFileToParseEntered();
                //System.out.println("nom fic = " + n.getFichier().getName());
                for (int i = 0; i < abonne.size(); i++) {
                    ((IController3D) (abonne.get(i))).receiveImmediatly(new ClearAllEvent(this));
                    ((Thread) (abonne.get(i))).suspend();
                }
                this.model.clearAll();
                this.list.getList().clear();
                this.list = new DebugParser(n.getFichier().getAbsolutePath());
                ((DebugParser) (this.list)).parseFile();

                for (int i = 0; i < abonne.size(); i++) {
                    ((Thread) (abonne.get(i))).resume();
                }

            } catch (IOException ex) {
                Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;

        }
    }
    }

    public void send(VisuEvent v) {
        for (int i = 0; i < abonne.size(); i++) {
            ((IController3D) (abonne.get(i))).receive(v);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!list.getList().isEmpty()) {
                ControlEvent ce = list.getList().removeLast();
                VisuEvent v = null;
                if (!(ce.getSource() instanceof PiAgentDebug)) {
                    switch (ce.getType()) {
                        case ADD_INPUT_COMMITMENT: {
                            AddInputCommitmentEvent ac = ce.asAddInputCommitment();
                            v = new CreateArrowInEvent(ac.getSource(), ac.getChannel());
                            break;
                        }

                        case ADD_OUTPUT_COMMITMENT: {
                            AddOutputCommitmentEvent ac = ce.asAddOutputCommitment();
                            v = new CreateArrowOutEvent(ac.getSource(), ac.getChannel());
                            break;
                        }
                        case CHANNEL_AQUIRED: {
                            ChannelAquiredEvent ac = ce.asChannelAquiredEvent();
                            if (ac.getThread() != null) {
                                v = new CreateOrCheckChannelInGreenEvent(ac.getThread(), ac.getChannel());
                            }
                            break;
                        }
                        case RELEASE_CHANNEL: {
                            ChannelReleasedEvent ac = ce.asChannelReleasedEvent();
                            v = new ChangeColorReleased(ac.getThread(), ac.getChannel());
                            break;
                        }
                        case FAILED_AQUIRE_CHANNEL: {
                            // FailedChannelAquireEvent ac = ce.asFailedChannelAquireEvent();
                            // v = new ChangeColorFailedEvent(ac.getSource(), ac.getChannel());
                            v = new NothingEvent(ce.getSource());
                            break;
                        }
                        case TAKE_INPUT_COMMITMENT: {
                            TakeInputCommitmentEvent ac = ce.asTakeInputCommitmentEvent();
                            v = new SendMessageInEvent(ac.getSource(), ac.getThreadReceiver(), ac.getChannel());
                            break;
                        }
                        case TAKE_OUTPUT_COMMITMENT: {
                            TakeOutputCommitmentEvent ac = ce.asTakeOutputCommitment();
                            v = new SendMessageOutEvent(ac.getSource(), ac.getThreadDest(), ac.getChannel());
                            break;
                        }
                        case CREATE_THREAD: {
                            ThreadCreateEvent ac = ce.asThreadCreateEvent();
                            v = new CreateThreadEvent(ac.getSource());
                            break;
                        }
                        case AWAKE_THREAD: {
                            AwakeEvent ac = ce.asAwakeEvent();
                            v = new AwakeThreadEvent(ac.getSource(), ac.getPiThread());
                            break;
                        }
                        case WAIT_THREAD: {
                            WaitEvent ac = ce.asWaitEvent();
                            v = new WaitThreadEvent(ac.getSource(), ac.getPiThread());
                            break;
                        }
                        case UNREGISTER_THREAD: {
                            UnregisterEvent u = ce.asUnregisterEvent();
                            v = new UnregisterThreadEvent(ce.getSource());
                            System.out.println("UNREGISTER_THREAD");
                            break;
                        }
                        case END_OF_AGENT: {
                            v = new NothingEvent(ce.getSource());
                            System.out.println("END_OF_AGENT");
                            break;
                        }

                        case LOG : {
                        LogEvent e = ce.asLogEvent();
                        v = new AddLogInListEvent(e.getSource(),e.getMessage());
                        System.out.println("LOG_EVENT");
                        break;
                    }

                        default: {
                            v = new NothingEvent(ce.getSource());
                            break;
                        }
                    }
                    send(v);

                }
                if (list.getList().isEmpty() || !(list.getList().peekLast() instanceof LogEvent)) {
                    try {
                        this.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }


        }
    }

    public IModel getModel() {
        return model;
    }
}
