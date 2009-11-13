/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pithreads.examples.cs;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import pithreads.framework.debug.*;
import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.visualisation.ApplicationController;
import pithreads.visualisation.PopUpStart;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.model.Model;

/**
 *
 * @author mathurin
 */
public class CriticalSectionDebug {

    public static void main(final String... args) {
        System.setProperty("java.library.path", "dist/lib");
        // Run this in the AWT event thread to prevent deadlocks and race conditions
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                PiFactory myFactory = new DebugFactory(true);
                PiAgent agent = myFactory.newPiAgent();
                EventList eventList = new EventList();
                ((PiAgentDebug) agent).bind(eventList);
                // switch to system l&f for native font rendering etc.
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "can not enable system look and feel", ex);
                }

                
                PopUpStart popUp = new PopUpStart(eventList);
                popUp.setVisible(true);
   
                

                int nb_procs = 15;

                if (args.length > 0) {
                    nb_procs = Integer.parseInt(args[0]);
                }



                // create the observer thread
                final PiChannel<Integer> obs = myFactory.newPiChannel(agent, "obs");
                PiThread observer = myFactory.newPiThread(agent, "Observer", new ObserverTask(obs));


                observer.start();

                // create the critical section threads
                final PiChannel<PiChannel<Integer>> lock = myFactory.newPiChannel(agent, "lock");

                for (int i = 1; i <= nb_procs; i++) {
                    PiThread cs = myFactory.newPiThread(agent, "cs" + i, new CSTask(i, lock));
                    cs.start();
                }

                // create the init process
                PiThread init = myFactory.newPiThread(agent, "init", new Task() {

                    @Override
                    public void body() throws RunException {
                        send(lock, obs);
                    }
                });

                init.start();

                agent.detach();


            }
        });
    }
}