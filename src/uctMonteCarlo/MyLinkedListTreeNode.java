package uctMonteCarlo;

import boardRep.*;
import boardRep.Global;
import java.util.List;

public class MyLinkedListTreeNode implements TreeNode {
	MyLinkedList<Child> children;
	int numberOfTrials;
	float expectedWins;
	GoBoard goBoard;
	Colour whoseTurn;
	
	public MyLinkedListTreeNode(GoBoard gb, Colour whoseTurn){
		children = new MyLinkedList<Child>();
		this.whoseTurn=whoseTurn;
		goBoard = gb;
	}
		
	
	@Override
	public List<Child> getChildren() {
		return children;
	}

	@Override
	public int getNumberOfTrials() {
		return numberOfTrials;
	}

	@Override
	public float getValue() {
		return expectedWins;
	}

	@Override
	public boolean isLeaf() {
		return children.isEmpty();
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
