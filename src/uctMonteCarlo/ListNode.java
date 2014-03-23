package uctMonteCarlo;

public class ListNode<E> {
	public E data;
	public ListNode<E> prev,next;
	public ListNode(E data, ListNode<E> prev, ListNode<E> next){
		this.data=data;
		this.prev=prev;
		this.next=next;
	}
}
