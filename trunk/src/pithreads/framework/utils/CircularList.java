package pithreads.framework.utils;

import java.util.ArrayList;
import java.util.List;

public class CircularList<T> {
	private Node current;
	private int size;
	
	public CircularList() {
		current = new Node();		
	}
	
	public int getSize() {
		return size;
	}
	
	public List<T> getElements() {
		List<T> list = new ArrayList<T>();
		if(current.getContent()==null) {
			return list;
		}
		list.add(current.getContent());
		Node node = current.getNext();
		while(node!=current) {
			list.add(node.getContent());
			node = node.getNext();
		}
		return list;
	}
	
	public boolean isEmpty() {
		return current.getContent()==null;
	}
	
	public boolean next() {
		Node next = current.getNext();
		if(next!=null) {
			boolean ret = (current != next);
			current = next;
			return ret;
		} else {
			throw new IllegalStateException("No next element (empty circular list)");
		}
	}
	
	public boolean prev() {
		Node prev = current.getPrev();
		if(prev!=null) {
			boolean ret = (current != prev);
			current = prev;
			return ret;
		} else {
			throw new IllegalStateException("No next element (empty circular list)");
		}
	}
	
	public T getElement() {
		return current.getContent();
	}
	
	public void insertBefore(T element) {
		if(element==null) {
			throw new IllegalArgumentException("Cannot insert <null> in a circular list");
		}
		
		if(current.getContent()==null) {
			// empty list
			current.setContent(element);
			current.setNext(current);
			current.setPrev(current);
		} else if(current.getPrev()==current) {
			// one element
			if(current.getNext()!=current) {
				throw new IllegalStateException("Circular list with one element is not correct (please report)");
			}
			Node nprev = new Node();
			current.setPrev(nprev);
			current.setNext(nprev);
			nprev.setPrev(current);
			nprev.setNext(current);			
		} else { // general case : n elements with n>1
			Node nprev = new Node();
			nprev.setContent(element);
			nprev.setNext(current);
			nprev.setPrev(current.getPrev());
			current.getPrev().setNext(nprev);
			current.setPrev(nprev);
		}
		size++;
	}
	
	public void insertAfter(T element) {
		if(element==null) {
			throw new IllegalArgumentException("Cannot insert <null> in a circular list");
		}

		if(current.getContent()==null) {
			// empty list
			current.setContent(element);
			current.setNext(current);
			current.setPrev(current);
		} else if(current.getPrev()==current) {
			// one element
			if(current.getNext()!=current) {
				throw new IllegalStateException("Circular list with one element is not correct (please report)");
			}
			Node nnext = new Node();
			current.setNext(nnext);
			current.setPrev(nnext);
			nnext.setPrev(current);
			nnext.setNext(current);			
		} else { // general case : n elements with n>1
			Node nnext = new Node();
			nnext.setContent(element);
			nnext.setPrev(current);
			nnext.setNext(current.getNext());
			current.getNext().setPrev(nnext);
			current.setNext(nnext);
		}
		size++;
	}
	
	public void remove() {
		if(current.getContent()==null) {
			throw new IllegalStateException("Cannot remove from empty circular list");
		} else if(current.getPrev()==current) {
			current.setContent(null);
		} else { // general case
			Node old = current;
			current = current.getNext();
			current.setPrev(old.getPrev());
			old.setNext(current);
		}
		size--;
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if(isEmpty()) {
			return "CList[]";
		}
		
		buf.append("CList[*<->");
		buf.append("{");
		buf.append(current.getContent());
		buf.append("}");
		if(current.getNext()==current) {
			buf.append("<->*");
		} else {			
			Node node = current.getNext();
			while(node!=current) {
				buf.append("<->");
				buf.append(node.getContent());
				node = node.getNext();
			}
			buf.append("<->*");
		}
		
		buf.append("]");
		return buf.toString();				
	}
	
	private class Node {
		private T content;
		private Node prev;
		private Node next;
		
		public Node() {
			content = null;
			prev = null;
			next = null;
		}
		
		public T getContent() {
			return content;
		}
		
		public void setContent(T content) {
			this.content = content;
		}
		
		public Node getPrev() {
			return prev;
		}
		
		public void setPrev(Node prev) {
			this.prev = prev;
		}
		
		public Node getNext() {
			return next;
		}
		
		public void setNext(Node next) {
			this.next = next;
		}
		
	}
}


