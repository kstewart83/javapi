/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.framework.debug;

import java.util.ArrayDeque;
import pithreads.framework.event.ControlEvent;

/**
 *
 * @author mathurin
 */
public class EventList implements Receiver{
    private ArrayDeque<ControlEvent> list;

    public EventList(){
        list = new ArrayDeque<ControlEvent>();
    }
    public ArrayDeque<ControlEvent> getList(){
    return list;
    }

    public void receiveEvent(ControlEvent e) {

        this.list.addFirst(e);
    }

}
