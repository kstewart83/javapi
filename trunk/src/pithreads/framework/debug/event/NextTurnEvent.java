package pithreads.framework.debug.event;

import pithreads.framework.debug.PiThreadDebug;
import pithreads.framework.event.ControlEvent;
//import pithreads.framework.event.ControlEvent.Type;

public class NextTurnEvent extends ControlEvent{

	public NextTurnEvent(Object source) {
		super(source, Type.NEXT_TURN);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4536511015602687141L;
	
	public String toString(){
		return "NEXT_TURN\nSource :"+((PiThreadDebug)source).toString()+
						"\nTurn :"+((PiThreadDebug)source).getTurn()+"\nend of NEXT_TURN\n";
	}
}
