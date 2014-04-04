package uctMonteCarlo;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MyLinkedList<E> implements List<E> {//
	ListNode<E> head;
	ListNode<E> tail;
	int size = 0;
	@Override
	public boolean add(E arg0) {
		if(head!=null){
			ListNode<E> node = head;
			tail.next = new ListNode<E>(arg0,node,null);
		}
		else{
			head = new ListNode<E>(arg0,null,null);
			tail = head;
		}
		size++;
		return true;
	}

	@Override
	public void add(int arg0, E arg1) {
		if(head!=null){
			ListNode<E> nextNode = head;
			int i = 0;
			ListNode<E> prevNode = null;
			while(nextNode.next!=null && i<arg0) prevNode = nextNode; nextNode = nextNode.next;
			ListNode<E> newNode = new ListNode<E>(arg1,prevNode,nextNode);
			prevNode.next = newNode;
			nextNode.prev = newNode;
		}
		else{
			head = new ListNode<E>(arg1,null,null);
			tail = head;
		}
		size++;
		
	}

	@Override
	public boolean addAll(Collection<? extends E> arg0) {
		Iterator<? extends E >iter = arg0.iterator();
		while(iter.hasNext()){
			this.add(iter.next());
		}
		return true;
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends E> arg1) {
		int i = 0;
		Iterator<? extends E >iter = arg1.iterator();
		while(iter.hasNext()){
			this.add(i, iter.next());
			i++;
		}
		return true;
	}

	@Override
	public void clear() {
		head=null;
		size=0;
	}

	@Override
	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E get(int arg0) {
		ListNode<E> node = head;
		int i = 0;
		while(node.next!=null&&i<arg0)node=node.next;
		return node.data;
	}

	@Override
	public int indexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int lastIndexOf(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E remove(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public E set(int arg0, E arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<E> subList(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ListNode<E> getHead(){
		return head;
	}

}
