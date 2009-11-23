package pithreads.examples.tutorial.tut3.async;

import java.util.ArrayDeque;
import java.util.Deque;

import pithreads.framework.Choice;
import pithreads.framework.InputTask;
import pithreads.framework.PiChannel;
import pithreads.framework.RunException;
import pithreads.framework.Task;
import pithreads.framework.ValueMaker;

public class MsgQueue<T> extends Task {
	private PiChannel<T> put;
	private PiChannel<T> take;
	private final int capacity;
	private Deque<T> queue;
	
	
	public MsgQueue(PiChannel<T> put, PiChannel<T> take, int capacity) {
		this.put = put;
		this.take = take;
		this.capacity = capacity;
		queue = new ArrayDeque<T>(capacity);		
	}
	
	private Choice makeChoice() {
		Choice choice = new Choice();
		choice.addInput(put, new InputTask<T>() {
			@Override
			public void body(T receivedValue) {
				queue.addFirst(receivedValue);
			}
		});
		choice.addOutput(take, new ValueMaker<T>() {
			//@Override
			public T make() {
				return queue.removeLast();
			}		
		}, new Task() {
			@Override
			public void body() throws RunException {
				// Do nothing				
			}			
		});
		
		return choice;
	}
	
	@Override
	public void body() throws RunException {
		Choice choice = makeChoice();
		
		while(true) {
			log("Queue size = "+queue.size());
			if(queue.isEmpty()) {
				log("Queue is empty");
				T msg = receive(put);
				queue.addFirst(msg);
			} else if(queue.size()==capacity) {
				log("Queue is full");
				T msg = queue.removeLast();
				send(take,msg);
			} else {
				// queue is neither empty nor full
				choose(choice);
			}
		}
	}
}
