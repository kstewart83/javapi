/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pithreads.framework.debug.event;

import pithreads.framework.debug.PiAgentDebug;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

/**
 *
 * @author mathurin
 */
public class EndOfAgentEvent extends ControlEvent{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8371509691469811096L;

	public EndOfAgentEvent(PiAgentDebug agent){
        super(agent,Type.END_OF_AGENT);
    }
	
	public String toString(){
		return "END_OF_AGENT\n";
	}

}
