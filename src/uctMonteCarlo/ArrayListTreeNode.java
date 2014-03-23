package uctMonteCarlo;

import java.util.List;

public class ArrayListTreeNode implements TreeNode {
	List<Child> children;
	int numberOfTrials;
	float expectedWins;	
	
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
		// TODO Auto-generated method stub
		
	}



}
