package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class MyLinkedListTreeNode extends SuperTreeNode {
	
	MyLinkedList<Child> children;
	
	public MyLinkedListTreeNode(MyLinkedList<Child> children,Colour whoseTurn, TreeNode parent) {
		super(children,whoseTurn, parent);
		this.children=children;
	}
	
	@Override
	public int generateChildren(GoBoard goBoard) {
		Colour nextTurn = Colour.GREY;
		//nextTurn = Global.opponent(super.whoseTurn);
		if(super.whoseTurn==Colour.WHITE) nextTurn=Colour.BLACK;
		else nextTurn = Colour.WHITE;
		int generated = 0;
		
		
		for(int i=Global.board_size+2;i<Global.array_size;i++){
			if(goBoard.check(i,whoseTurn)){
				children.add(new Child(new MyLinkedListTreeNode(new MyLinkedList<Child>(),nextTurn, this),i));
				generated++;
			}
		}
		playAllChildrenOnce(goBoard);
		return generated;
	}
	private void playAllChildrenOnce(GoBoard goBoard){
		if(!children.isEmpty()){
		ListNode<Child> listNode = children.getHead();
			while(listNode.next!=null){
				Child child = listNode.data;
				GoBoard newBoard = goBoard.clone();
				newBoard.put(whoseTurn, child.getMove());
				Colour winner = newBoard.randomPlayout(Global.opponent(whoseTurn));
				child.node.update(winner);			
				listNode=listNode.next;
			}
		}
	}	
	
	@Override
	public TreeNode makeMove(int move) {
		TreeNode node = null;
		ListNode<Child> listNode = children.head;
		while(listNode!=null){
			Child child = listNode.data;
			if(child.move==move){
				node = child.node;			
			}
			listNode = listNode.next;
		}
		return node;
	}	
	
	@Override
	public Child getMaxChild() {
		Child currentMaxChild = children.get(0);
		float currentMaxValue = currentMaxChild.node.getMaxValue();
		
		ListNode<Child> listNode = children.head;
		while(listNode!=null){
			Child child = listNode.data;
			float val = child.node.getMaxValue();
			if(child.node.getMaxValue()>currentMaxValue){
				currentMaxChild = child;
				currentMaxValue = val;	
			}
			listNode = listNode.next;
		}
		return currentMaxChild;
	}

	@Override
	public Child getMinChild() {
		Child currentMinChild = children.get(0);
		float currentMinValue = currentMinChild.node.getMinValue();
		
		ListNode<Child> listNode = children.head;
		while(listNode!=null){
			Child child = listNode.data;
			float val = child.node.getMinValue();
			if(child.node.getMinValue()<currentMinValue){
				currentMinChild = child;
				currentMinValue = val;	
			}
			listNode = listNode.next;
		}
		return currentMinChild;
	}
	
}
