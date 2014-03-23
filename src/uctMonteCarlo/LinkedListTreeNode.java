package uctMonteCarlo;

import java.util.LinkedList;
import java.util.List;

public class LinkedListTreeNode implements TreeNode {
	List <Child> children;
	int numberOfTrials;
	float expectedWins;
	int[] moves;

	public LinkedListTreeNode(){
		children = new LinkedList<Child>();
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

	
	private void findPossibleMoves(){
		
	}

	@Override
	public void generateChildren() {
		// TODO Auto-generated method stub
		
	}
}
