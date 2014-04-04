package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class SuperTreeNode implements TreeNode {
	List<Child> children;
	int numberOfTrials;
	float expectedWins;
	GoBoard goBoard;
	Colour whoseTurn;
	
	public SuperTreeNode(List<Child> children, GoBoard goBoard, Colour whoseTurn){
		this.children = children;
		this.whoseTurn=whoseTurn;
		this.goBoard = goBoard;
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
		return children.size()==0;
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
		for(int i=0;i<children.size();i++){
			GoBoard newBoard = goBoard.clone();
			Child child = children.get(i);
			newBoard.put(whoseTurn, child.getMove());
			Colour winner = newBoard.randomPlayout(Global.opponent(whoseTurn));
			child.node.update(winner);
		}
		
	}

	@Override
	public void update(Colour winner) {
				
	}	
}
