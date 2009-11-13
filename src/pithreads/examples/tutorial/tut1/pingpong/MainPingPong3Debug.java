/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.examples.tut1.pingpong;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.debug.PiFactory;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.debug.EventList;
import pithreads.framework.utils.Pair;
import pithreads.visualisation.ApplicationController;
import pithreads.visualisation.PopUpStart;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.model.Model;

/**
 *
 * @author mathurin
 */
public class MainPingPong3Debug {

    public static void main(String... args) {
System.setProperty("java.library.path","dist/lib");
        // Run this in the AWT event thread to prevent deadlocks and race conditions
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                PiFactory myFactory = new DebugFactory(true);
                PiAgent agent = myFactory.newPiAgent();
                EventList eventList = new EventList();
                // switch to system l&f for native font rendering etc.
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "can not enable system look and feel", ex);
                }
                PopUpStart popUp = new PopUpStart(eventList);
                popUp.setVisible(true);

                final PiChannel<Pair<String, Integer>> ping = myFactory.newPiChannel(agent, "ping");
                PiChannel<Pair<String, Integer>> pong = myFactory.newPiChannel(agent, "pong");

                PiThread pinger = myFactory.newPiThread(agent, "pinger", new PingPong3(ping, pong, "<<PING>>"));
                pinger.start();

                PiThread ponger = myFactory.newPiThread(agent, "ponger", new PingPong3(pong, ping, "<<PONG>>"));
                ponger.start();

                PiThread init = myFactory.newPiThread(agent, "init", new Task() {

                    @Override
                    public void body() throws RunException {
                        send(ping, new Pair<String, Integer>("<<INIT>>", 20));
                    }
                });
                init.start();

                agent.detach();


            }
        });




    }
}
