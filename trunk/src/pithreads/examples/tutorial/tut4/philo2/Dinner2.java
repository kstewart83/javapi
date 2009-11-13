package pithreads.examples.tutorial.tut4.philo2;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import pithreads.examples.tut4.philo1.Chair;
import pithreads.examples.tut4.philo1.Fork;
import pithreads.examples.tut4.philo1.Place;
import pithreads.examples.tut4.philo1.Plate;
import pithreads.framework.PiAgent;
import pithreads.framework.PiChannel;
import pithreads.framework.PiThread;
import pithreads.framework.debug.DebugFactory;
import pithreads.framework.debug.EventList;
import pithreads.framework.debug.PiAgentDebug;
import pithreads.framework.debug.PiFactory;
import pithreads.visualisation.ApplicationController;
import pithreads.visualisation.PopUpStart;
import pithreads.visualisation.model.IModel;
import pithreads.visualisation.model.Model;

public class Dinner2 {

    private static final int NBRES = 5;
    private static final Integer QUANTITY = 10;
    private static final int NBPHILO = 7;

    public static void main(String... args) {
        // for visualvm
        //JOptionPane.showMessageDialog(null, "Dinner 2","Ready to start program ?", JOptionPane.INFORMATION_MESSAGE);

        long startTime = System.currentTimeMillis();


        PiFactory myFactory = new DebugFactory(true);
        PiAgent agent = myFactory.newPiAgent();
        EventList eventList = new EventList();

        ((PiAgentDebug) agent).bind(eventList);
        PopUpStart popUp = new PopUpStart(eventList);
        popUp.setVisible(true);

        ArrayList<Fork> couverts = new ArrayList<Fork>();
        for (int i = 0; i < NBRES; i++) {
            PiChannel<Integer> take = myFactory.newPiChannel(agent, "take" + i);
            PiThread fork = new PiThread(agent, "fork" + i);
            Fork oneFork = new Fork(take, i);
            couverts.add(oneFork);
            fork.assignTask(oneFork);
            fork.start();
        }

        PiChannel<Place> seat = new PiChannel<Place>(agent, "seat");
        ArrayList<Plate> plates = new ArrayList<Plate>();
        ArrayList<PiChannel<Place>> pichTab = new ArrayList<PiChannel<Place>>();
        for (int i = 0; i < NBRES; i++) {
            PiChannel<Integer> eat = new PiChannel<Integer>(agent, "eat" + i);
            PiThread plate = new PiThread(agent, "plate" + i);
            Plate onePlate = new Plate(eat, QUANTITY);
            plates.add(onePlate);
            plate.assignTask(onePlate);
            plate.start();

            //PiChannel<Place> seat =  new PiChannel<Place>(agent,"seat"+i);
            PiThread chair = new PiThread(agent, "chair" + i);
            Place place = new Place(couverts.get(i), couverts.get((i + 1) % NBRES),
                    plates.get(i), new PiChannel<Boolean>(agent, "leave" + i));
            chair.assignTask(new Chair(place, seat));
            pichTab.add(seat);
            chair.start();
        }

        for (int i = 0; i < NBPHILO; i++) {
            PiThread philo = new PiThread(agent, "philo" + i);
            philo.assignTask(new ChoosingPhilosopher(seat));//pichTab.get(i)
            philo.start();
        }

        agent.detach();

        try {
            agent.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        long totalTime = endTime - startTime;

        System.out.println("Total running time = " + totalTime + " (ms)");

    }
}
