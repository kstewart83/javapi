package test.pithreads.framework;

import static org.junit.Assert.*;

import org.junit.Test;

import pithreads.framework.utils.CircularList;

public class CircularListTest {

	@Test
	public void testInsertBefore() {
		CircularList<Integer> list = new CircularList<Integer>();
		assertTrue(list.isEmpty());
		assertTrue(list.getSize()==0);
		try {
			@SuppressWarnings("unused")
			int elem = list.getElement();
			assertTrue(false);
		} catch(IllegalStateException e) {
			assertTrue(true);
		}
		
		list.insertBefore(1);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==1);
		assertTrue(list.getElement().equals(1));
		assertFalse(list.next());
		assertTrue(list.getElement().equals(1));
		
		list.insertBefore(2);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==2);
		assertTrue(list.getElement().equals(1));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(2));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(1));
		
		list.insertBefore(3);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==3);
		assertTrue(list.getElement().equals(1));
		//System.out.println(list);
		assertTrue(list.next());
		//System.out.println(list);
		assertTrue(list.getElement().equals(2));
		assertTrue(list.next());
		//System.out.println(list);
		assertTrue(list.getElement().equals(3));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(1));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(3));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(2));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(1));		
	}

	@Test
	public void testInsertAfter() {
		CircularList<Integer> list = new CircularList<Integer>();
		assertTrue(list.isEmpty());
		assertTrue(list.getSize()==0);
		try {
			@SuppressWarnings("unused")
			int elem = list.getElement();
			assertTrue(false);
		} catch(IllegalStateException e) {
			assertTrue(true);
		}
		
		list.insertAfter(1);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==1);
		assertTrue(list.getElement().equals(1));
		assertFalse(list.next());
		assertTrue(list.getElement().equals(1));
		
		list.insertAfter(2);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==2);
		assertTrue(list.getElement().equals(1));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(2));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(1));
		
		list.insertAfter(3);
		assertTrue(!list.isEmpty());
		assertTrue(list.getSize()==3);
		assertTrue(list.getElement().equals(1));
		//System.out.println(list);
		assertTrue(list.next());
		//System.out.println(list);
		assertTrue(list.getElement().equals(3));
		assertTrue(list.next());
		//System.out.println(list);
		assertTrue(list.getElement().equals(2));
		assertTrue(list.next());
		assertTrue(list.getElement().equals(1));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(2));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(3));
		assertTrue(list.prev());
		assertTrue(list.getElement().equals(1));
	}

	@Test
	public void testRemove() {
		CircularList<Integer> list = new CircularList<Integer>();
		list.insertBefore(1);
		list.insertBefore(2);
		list.insertBefore(3);
		list.insertBefore(4);
		list.next();
		assertTrue(list.getElement().equals(2));
		assertTrue(list.getSize()==4);
		list.remove();
		assertTrue(list.getElement().equals(3));
		assertTrue(list.getSize()==3);
		list.remove();
		assertTrue(list.getElement().equals(4));
		assertTrue(list.getSize()==2);
		list.remove();
		assertTrue(list.getElement().equals(1));
		assertTrue(list.getSize()==1);
		list.remove();		
		assertTrue(list.isEmpty());
		assertTrue(list.getSize()==0);
	}

}
