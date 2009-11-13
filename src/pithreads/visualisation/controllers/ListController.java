/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.visualisation.controllers;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import pithreads.framework.debug.PiChannelDebugInfo;
import pithreads.framework.debug.PiThreadDebugInfo;
import pithreads.visualisation.ApplicationController;
import pithreads.visualisation.listView.MsgLog;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.visuEvent.AddLogInListEvent;
import pithreads.visualisation.visuEvent.ChannelInListSelectedEvent;
import pithreads.visualisation.visuEvent.CreateArrowInEvent;
import pithreads.visualisation.visuEvent.CreateArrowOutEvent;
import pithreads.visualisation.visuEvent.CreateThreadEvent;
import pithreads.visualisation.visuEvent.ThreadInListSelectedEvent;
import pithreads.visualisation.visuEvent.UnregisterThreadEvent;
import pithreads.visualisation.visuEvent.VisuEvent;
import pithreads.visualisation.visuEvent.WaitThreadEvent;

/**
 *
 * @author mathurin
 */
public class ListController extends Thread implements IController3D, MouseListener, KeyListener {

    private ArrayDeque<VisuEvent> eventList;
    private JList listOfPiThreads;
    private IModel model;
    private HashMap<Integer, Integer> hashPiThreads;
    private HashMap<Integer, Integer> hashPiChannels;
    private int indiceThreads;
    private int indiceChannels;
    private ApplicationController applicationController;
    private JList listOfPiChannels;
    private JList listOfPiChannelsInfo;
    private JList listOfPiThreadsInfo;
    private JList listOfLogs;
    private ArrayList<PiChannelDebugInfo> piChannel;
    private ArrayList<PiThreadDebugInfo> piThread;
    private HashMap<Integer, ArrayList<PiThreadDebugInfo>> linkList;

    public ListController(JList listOfPiThreads, JList listOfPiChannels, JList listOfPiChannelsInfo, JList listOfPiThreadsInfo, JList listOfLogs, IModel model) {
        this.model = model;
        this.eventList = new ArrayDeque<VisuEvent>();
        this.listOfPiThreads = listOfPiThreads;
        this.listOfPiChannels = listOfPiChannels;
        this.listOfPiChannelsInfo = listOfPiChannelsInfo;
        this.listOfPiThreadsInfo = listOfPiThreadsInfo;
        this.listOfLogs = listOfLogs;
        this.hashPiThreads = new HashMap<Integer, Integer>();
        this.hashPiChannels = new HashMap<Integer, Integer>();
        indiceThreads = 0;
        indiceChannels = 0;
        listOfPiThreads.addMouseListener(this);
        listOfPiChannels.addMouseListener(this);
        listOfPiThreads.addKeyListener(this);
        listOfPiChannels.addKeyListener(this);
        piChannel = new ArrayList<PiChannelDebugInfo>();
        piThread = new ArrayList<PiThreadDebugInfo>();
        linkList = new HashMap<Integer, ArrayList<PiThreadDebugInfo>>();
    }

    public void bind(ApplicationController aThis) {
        applicationController = aThis;
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
                        //modelUpdater.createArrowIn(e);
                        CreateArrowInEvent c = e.asCreateArrowInEvent();
                        PiThreadDebugInfo pithread = c.getPiThread();
                        PiChannelDebugInfo pichannel = c.getChannel();
                        ArrayList<PiThreadDebugInfo> liste = linkList.get(pichannel.getId());
                        linkList.remove(pichannel.getId());
                        liste.add(pithread);
                        linkList.put(pichannel.getId(),liste);
                        sendSelected();
                        break;
                    }
                    case CREATE_ARROW_OUT: {
                        //modelUpdater.createArrowOut(e);
                        CreateArrowOutEvent c = e.asCreateArrowOutEvent();
                        PiThreadDebugInfo pithread = c.getPiThread();
                        PiChannelDebugInfo pichannel = c.getChannel();
                        ArrayList<PiThreadDebugInfo> liste = linkList.get(pichannel.getId());
                        linkList.remove(pichannel.getId());
                        liste.add(pithread);
                        linkList.put(pichannel.getId(),liste);
                        sendSelected();
                        break;
                    }
                    case SEND_MESSAGE_IN: {

                        PiThreadDebugInfo p1 = e.asSendMessageInEvent().getPiThreadSource();
                        PiThreadDebugInfo p2 = e.asSendMessageInEvent().getPiThreadDest();
                        PiChannelDebugInfo c = e.asSendMessageInEvent().getPiChannel();
                        int selected = listOfPiThreads.getSelectedIndex();
                        int k = hashPiThreads.get(p1.getId());
                        MsgLog m1 = (MsgLog) ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).get(k);
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k,
                                new MsgLog("Thread \"" + p1.getName() + "\" id = " + p1.getId() +
                                " envoi un message via le channel \"" + c.getId(), Color.RED));

                        int k2 = hashPiThreads.get(p2.getId());
                        MsgLog m2 = (MsgLog) ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).get(k2);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k2);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k2,
                                new MsgLog("Thread \"" + p2.getName() + "\" id = " + p2.getId() +
                                " recoit un message", Color.RED));

                        int selectedChannel = listOfPiChannels.getSelectedIndex();
                        int kChannel = hashPiChannels.get(c.getId());
                        MsgLog mChannel = (MsgLog) ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).get(kChannel);

                        ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).removeElementAt(kChannel);

                        ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(kChannel,
                                new MsgLog("Channel id = " + c.getId() + " fait passer un message \"input\"", Color.RED));

                        try {
                            this.sleep(model.getMovementSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k2);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k2, m2);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k, m1);

                        listOfPiThreads.setSelectedIndex(selected);


                        if (!linkList.get(c.getId()).isEmpty()) {
                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).removeElementAt(kChannel);

                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(kChannel, mChannel);

                            listOfPiChannels.setSelectedIndex(selectedChannel);
                        } else {
                            this.indiceChannels--;
                            linkList.remove(c.getId());
                        }
                        sendSelected();
                        break;
                    }

                    case SEND_MESSAGE_OUT: {

                        PiThreadDebugInfo p1 = e.asSendMessageOutEvent().getPiThreadSource();
                        PiThreadDebugInfo p2 = e.asSendMessageOutEvent().getPiThreadDest();
                        PiChannelDebugInfo c = e.asSendMessageOutEvent().getPiChannel();

                        int selectedThread = listOfPiThreads.getSelectedIndex();
                        int kThread = hashPiThreads.get(p1.getId());
                        MsgLog m1Thread = (MsgLog) ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).get(kThread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(kThread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(kThread,
                                new MsgLog("Thread \"" + p1.getName() + "\" id = " + p1.getId() +
                                " envoi un message via le channel \"" + c.getId(), Color.RED));

                        int k2Thread = hashPiThreads.get(p2.getId());
                        MsgLog m2Thread = (MsgLog) ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).get(k2Thread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k2Thread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k2Thread,
                                new MsgLog("Thread \"" + p2.getName() + "\" id = " + p2.getId() +
                                " recoit un message", Color.RED));

                        int selectedChannel = listOfPiChannels.getSelectedIndex();
                        int kChannel = hashPiChannels.get(c.getId());
                        MsgLog mChannel = (MsgLog) ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).get(kChannel);

                        ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).removeElementAt(kChannel);

                        ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(kChannel,
                                new MsgLog("Channel id = " + c.getId() + " fait passer un message \"output\"", Color.RED));

                        try {
                            this.sleep(model.getMovementSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k2Thread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k2Thread, m2Thread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(kThread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(kThread, m1Thread);


                        listOfPiThreads.setSelectedIndex(selectedThread);

                        if (!linkList.get(c.getId()).isEmpty()) {
                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).removeElementAt(kChannel);

                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(kChannel, mChannel);

                            listOfPiChannels.setSelectedIndex(selectedChannel);
                        } else {
                            linkList.remove(c.getId());
                            this.indiceChannels--;
                        }
                        sendSelected();
                        break;
                    }
                    case CREATE_OR_CHECK_CHANNEL: {

                        PiThreadDebugInfo p = e.asCreateOrCheckChannelInGreenEvent().getPiThread();
                        PiChannelDebugInfo c = e.asCreateOrCheckChannelInGreenEvent().getPiChannel();

                        ArrayList<PiThreadDebugInfo> liste;
                        if (!piChannel.contains(c)) {
                            piChannel.add(c);
                            liste = new ArrayList<PiThreadDebugInfo>();
                            liste.add(p);
                            linkList.put(c.getId(), liste);
                        } else {
                            liste = linkList.get(c.getId());
                            liste.add(p);
                            linkList.remove(c.getId());
                            linkList.put(c.getId(), liste);

                        }

                        int kThread = hashPiThreads.get(p.getId());
                        int selectedThread = listOfPiThreads.getSelectedIndex();
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(kThread);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(kThread,
                                new MsgLog("Thread \"" + p.getName() + "\" id = " + p.getId() +
                                " check le channel \"" + c.getId(), Color.GREEN));
                        listOfPiThreads.setSelectedIndex(selectedThread);


                        if (hashPiChannels.get(c.getId()) == null) {
                            hashPiChannels.put(c.getId(), indiceChannels);
                            int selected = listOfPiChannels.getSelectedIndex();
                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(indiceChannels,
                                    new MsgLog("création du Channel d'id " + c.getId(), Color.BLACK));
                            indiceChannels++;
                            listOfPiChannels.setSelectedIndex(selected);

                        } else {
                            int kChannel = hashPiChannels.get(c.getId());

                            int selectedChannel = listOfPiChannels.getSelectedIndex();
                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).removeElementAt(kChannel);

                            ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).add(kChannel,
                                    new MsgLog("Channel id = " + c.getId() +
                                    " se fait checker par le PiThread d'id " + p.getId(), Color.GREEN));
                            listOfPiChannels.setSelectedIndex(selectedChannel);
                        }
                        sendSelected();
                        break;
                    }
                    case CHANGE_COLOR_RELEASED: {
                        //modelUpdater.changeColorReleased(e);
                        sendSelected();
                        break;
                    }
                    case CHANGE_COLOR_FAILED: {
                        //
                        sendSelected();
                        break;
                    }
                    case CREATE_THREAD: {
                        //modelUpdater.createThread(e);
                        PiThreadDebugInfo p = e.asCreateThreadEvent().getPiThread();
                        hashPiThreads.put(p.getId(), indiceThreads);
                        piThread.add(p);
                        int selected = listOfPiThreads.getSelectedIndex();
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(indiceThreads,
                                new MsgLog("création du Thread \"" + p.getName() + "\" id = " + p.getId(), Color.BLACK));
                        indiceThreads++;
                        listOfPiThreads.setSelectedIndex(selected);

                        sendSelected();
                        break;
                    }
                    case CHANGE_COLOR_THREAD_AWAKE: {
                        PiThreadDebugInfo p = e.asAwakeThreadEvent().getPiThread();
                        int k = hashPiThreads.get(p.getId());
                        int selected = listOfPiThreads.getSelectedIndex();
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k,
                                new MsgLog("Thread \"" + p.getName() + "\" id = " + p.getId() + " se reveille", Color.CYAN));
                        listOfPiThreads.setSelectedIndex(selected);
                        break;
                    }

                    case CHANGE_COLOR_THREAD_WAIT: {
                        PiThreadDebugInfo p = e.asWaitThreadEvent().getPiThread();

                        int selected = listOfPiThreads.getSelectedIndex();

                        int k = hashPiThreads.get(p.getId());
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k,
                                new MsgLog("Thread \"" + p.getName() + "\" id = " + p.getId() + " s'endort", Color.GRAY));
                        listOfPiThreads.setSelectedIndex(selected);
                        sendSelected();
                        break;
                    }
                    case UNREGISTER_THREAD: {
                        //modelUpdater.unregisterThread(e);
                        PiThreadDebugInfo p = e.asUnregisterThreadEvent().getPiThread();
                        int k = hashPiThreads.get(p.getId());
                        piThread.remove(p);
                        int selected = listOfPiThreads.getSelectedIndex();
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);

                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).add(k,
                                new MsgLog("Thread \"" + p.getName() + "\" id = " + p.getId() + " est mort", Color.RED));

                        try {
                            this.sleep(model.getMovementSpeed());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).removeElementAt(k);
                        indiceThreads--;
                        listOfPiThreads.setSelectedIndex(selected);
                        sendSelected();
                        break;
                    }
                    case ADD_LOG_IN_LIST: {

                        String msg = e.asAddLogInList().getMessage();
                        // System.out.println("relai dans listController");
                        ((javax.swing.DefaultListModel) listOfLogs.getModel()).addElement(new MsgLog(msg, Color.BLACK));

                        break;
                    }
                }
            }
            try {
                this.sleep(model.getMilliSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        //((MsgLog)((javax.swing.DefaultListModel) listOfPiThreads.getModel()).getElementAt(0)).setSelected(true);
    }

    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
        int selection = listOfPiThreads.getSelectedIndex();
        //System.out.println("cliké sur liste : "+selection);
        int selection2 = listOfPiChannels.getSelectedIndex();

        for (Integer cle : hashPiThreads.keySet()) {
            if (hashPiThreads.get(cle) == selection) {
                send(new ThreadInListSelectedEvent(e, cle));
                break;
            }
        }
        for (Integer cle2 : hashPiChannels.keySet()) {
            if (hashPiChannels.get(cle2) == selection2) {
                send(new ChannelInListSelectedEvent(e, cle2));
                break;
            }

        }
    }

    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void send(VisuEvent v) {
        applicationController.receive(v);
    }

    public void receiveImmediatly(VisuEvent e) {
        switch (e.getType()) {
            case OVERLIGNE_THE_THREAD: {
                
                int id = e.asOverligneTheThread().getThreadId();
                int indice = hashPiThreads.get(id);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).clear();
                MsgLog msg0 = (MsgLog) ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).elementAt(indice);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg0);
                PiThreadDebugInfo c = findThread(id);
                MsgLog msg1 = new MsgLog("name : \"" + c.getName() + "\"", Color.BLACK);
                MsgLog msg2 = new MsgLog("id : " + c.getId(), Color.BLACK);
                //MsgLog msg3 = new MsgLog("immediateLock : "+c.immediateLock(),Color.BLACK);
                MsgLog msg4 = new MsgLog("awakeLock : " + c.awakeLock() + "\"", Color.BLACK);
                MsgLog msg5 = new MsgLog("enableGuardIndex : " + c.enabledGuardIndex(), Color.BLACK);
                MsgLog msg6 = new MsgLog("terminateFlag : " + c.getTerminateFlag(), Color.BLACK);
                MsgLog msg7 = new MsgLog("turn : " + c.getTurn(), Color.BLACK);
//                MsgLog msg8 = new MsgLog("plan : "+c.plan().toString(),Color.BLACK);
                MsgLog msg9 = new MsgLog("receivedValue : " + c.receivedValue(), Color.BLACK);

                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg1);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg2);
                //((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg3);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg4);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg5);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg6);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg7);
                //              ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg8);
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).addElement(msg9);
                break;
            }
            case OVERLIGNE_THE_CHANNEL: {
                int id = e.asOverligneTheChannel().getChannelId();
                int indice = hashPiChannels.get(id);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).clear();
                MsgLog msg0 = (MsgLog) ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).elementAt(indice);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg0);
                PiChannelDebugInfo c = findCube(id);
                MsgLog msg1 = new MsgLog("name : \"" + c.getName() + "\"", Color.BLACK);
                MsgLog msg2 = new MsgLog("id : " + c.getId(), Color.BLACK);
                MsgLog msg3 = new MsgLog("nbOwners : " + c.nbOwners(), Color.BLACK);
                MsgLog msg4 = new MsgLog("owner : \"" + c.getOwner().getName() + "\"", Color.BLACK);
                MsgLog msg5 = new MsgLog("size InCommits : " + c.getInCommits().size(), Color.BLACK);
                MsgLog msg6 = new MsgLog("size OutCommits : " + c.getOutCommits().size(), Color.BLACK);

                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg1);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg2);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg3);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg4);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg5);
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).addElement(msg6);
                break;
            }
            case CLEAR_ALL: {
                this.eventList.clear();
                this.hashPiChannels.clear();
                this.hashPiThreads.clear();
                this.indiceChannels = 0;
                this.indiceThreads = 0;
                ((javax.swing.DefaultListModel) listOfPiChannelsInfo.getModel()).clear();
                ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).clear();
                ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).clear();
                ((javax.swing.DefaultListModel) listOfPiThreadsInfo.getModel()).clear();
                //TODO : list of LOG
                // ((javax.swing.DefaultListModel) list.getModel()).clear();

                break;
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {

        int selection = listOfPiThreads.getSelectedIndex();
        int selection2 = listOfPiChannels.getSelectedIndex();

        if (e.getKeyCode() == e.VK_UP && e.getComponent().equals(listOfPiThreads) && selection > 0) {
            selection--;
        }
        if (e.getKeyCode() == e.VK_UP && e.getComponent().equals(listOfPiChannels) && selection2 > 0) {
            selection2--;
        }

        if (e.getKeyCode() == e.VK_DOWN && e.getComponent().equals(listOfPiThreads) &&
                ((javax.swing.DefaultListModel) listOfPiThreads.getModel()).getSize() - 1 != selection) {
            selection++;
        }
        if (e.getKeyCode() == e.VK_DOWN && e.getComponent().equals(listOfPiChannels) &&
                ((javax.swing.DefaultListModel) listOfPiChannels.getModel()).getSize() - 1 != selection2) {
            selection2++;
        }

        if (e.getKeyCode() == e.VK_DOWN || e.getKeyCode() == e.VK_UP) {
            for (Integer cle : hashPiThreads.keySet()) {
                if (hashPiThreads.get(cle) == selection) {
                    send(new ThreadInListSelectedEvent(e, cle));
                    break;
                }
            }
            for (Integer cle2 : hashPiChannels.keySet()) {
                if (hashPiChannels.get(cle2) == selection2) {
                    send(new ChannelInListSelectedEvent(e, cle2));
                    break;
                }

            }

        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void sendSelected() {

        int selection = listOfPiThreads.getSelectedIndex();
        int selection2 = listOfPiChannels.getSelectedIndex();
        for (Integer cle : hashPiThreads.keySet()) {
            if (hashPiThreads.get(cle) == selection) {
                send(new ThreadInListSelectedEvent(this, cle));
                break;
            }
        }
        for (Integer cle2 : hashPiChannels.keySet()) {
            if (hashPiChannels.get(cle2) == selection2) {
                send(new ChannelInListSelectedEvent(this, cle2));
                break;
            }

        }
    }

    private PiChannelDebugInfo findCube(int id) {
        for(int i=0;i<piChannel.size();i++){
        if(id==piChannel.get(i).getId()) return piChannel.get(i);
        }
        return null;
    }

    private PiThreadDebugInfo findThread(int id) {
        for(int i=0;i<piThread.size();i++){
        if(id==piThread.get(i).getId()) return piThread.get(i);
        }
        return null;
    }
}
