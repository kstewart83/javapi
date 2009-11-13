/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.framework.debug.event;

import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

/**
 *
 * @author mathurin
 */
public class ThreadCreateEvent extends ControlEvent{

	public ThreadCreateEvent(Object source) {
		super(source, Type.CREATE_THREAD);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4536511015602687141L;
	
	public String toString(){
		return "CREATE_THREAD\nSource :"+((PiThreadDebug)source).toString()+"\nend of CREATE_THREAD\n";
	}
}


