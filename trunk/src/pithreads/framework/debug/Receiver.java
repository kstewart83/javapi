package pithreads.framework.debug;

import java.util.ArrayDeque;

import pithreads.framework.event.ControlEvent;

public interface Receiver {
	
	public ArrayDeque<ControlEvent> getList();
	public void receiveEvent(ControlEvent e);
		
}
