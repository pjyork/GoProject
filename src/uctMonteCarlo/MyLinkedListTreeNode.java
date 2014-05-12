package uctMonteCarlo;

import java.util.LinkedList;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class MyLinkedListTreeNode extends SuperTreeNode {
	
	MyLinkedList<Child> children;
	
	public MyLinkedListTreeNode(MyLinkedList<Child> children,Colour whoseTurn, TreeNode parent,int move) {
		super(children,whoseTurn, parent, move);
		this.children=children;
	}
	
	@Override
	public int generateChildren(GoBoard goBoard,UpdateType updateType) {
		Colour nextTurn = Colour.GREY;
		nextTurn = Global.opponent(super.whoseTurn);
		//if(super.whoseTurn==Colour.WHITE) nextTurn=Colour.BLACK;
		//else nextTurn = Colour.WHITE;
		int generated = 0;
		
		for(int i=0;i<Global.array_size;i++){
			if(goBoard.check(i,whoseTurn)){
				children.add(new Child(new MyLinkedListTreeNode(new MyLinkedList<Child>(),nextTurn, this, i),i));
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
				child.node.amafUpdate(winner, new LinkedList<Integer>());
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
	
	/*	@Override
	public Child getBlackChild() {
		Child currentMaxChild = children.get(0);
		float currentMaxValue = currentMaxChild.node.getBlackValue();
		
		ListNode<Child> listNode = children.head;
		while(listNode!=null){
			Child child = listNode.data;
			float val = child.node.getBlackValue();
			if(child.node.getBlackValue()>currentMaxValue){
				currentMaxChild = child;
				currentMaxValue = val;	
			}
			listNode = listNode.next;
		}
		return currentMaxChild;
	}

	@Override
	public Child getWhiteChild() {
		Child currentMaxChild = children.get(0);
		float currentMaxValue = currentMaxChild.node.getWhiteValue();
		
		ListNode<Child> listNode = children.head;
		while(listNode!=null){
			Child child = listNode.data;
			float val = child.node.getWhiteValue();
			if(val>currentMaxValue){
				currentMaxChild = child;
				currentMaxValue = val;	
			}
			listNode = listNode.next;
		}
		return currentMaxChild;
	}
	*/
	
	@Override
	public void  childPrint() {		
		ListNode<Child> listNode = children.head;
		String moves = "";
		while(listNode!=null){
			Child child = listNode.data;
			moves = moves +"," +Global.posString(child.move);
			listNode = listNode.next;
		}
		String whoseTurnString = "";
		if(whoseTurn==Colour.WHITE) whoseTurnString ="White";
		else whoseTurnString = "Black";
		System.out.println(whoseTurnString+" moves - "+moves);
	}
	
	
	
}
