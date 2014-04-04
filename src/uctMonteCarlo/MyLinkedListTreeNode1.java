package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class MyLinkedListTreeNode1 extends SuperTreeNode {
	
	MyLinkedList<Child> children;
	
	public MyLinkedListTreeNode1(List<Child> children, GoBoard goBoard,
			Colour whoseTurn) {
		super(children, goBoard, whoseTurn);
	}

	
	@Override
	public void generateChildren() {
		Colour nextTurn = Colour.GREY;
		if(whoseTurn==Colour.BLACK){
			nextTurn = Colour.WHITE;
		}
		else{
			nextTurn = Colour.BLACK;
		}
		for(int i=Global.board_size+2;i<Global.array_size;i++){
			if(goBoard.check(i,whoseTurn)){
				children.add(new Child(new MyLinkedListTreeNode(goBoard,nextTurn),i));
			}
		}
		playAllChildrenOnce();
	}
	private void playAllChildrenOnce(){
		ListNode<Child> child = children.getHead();
		while(child.next!=null){
			
		}
	}	
}
