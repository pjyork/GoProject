package uctMonteCarlo;

import boardRep.*;

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
			GoBoard newBoard = goBoard.clone();
			newBoard.put(whoseTurn, child.data.getMove());
			Colour winner = newBoard.randomPlayout(Global.opponent(whoseTurn));
			child.data.node.update(winner);
		}
	}


	@Override
	public void update(Colour winner) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Colour getWhoseTurn() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TreeNode getMaxChild() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TreeNode getMinChild() {
		// TODO Auto-generated method stub
		return null;
	}	


}
