package uctMonteCarlo;

import java.util.List;

public interface TreeNode {
	List<Child> getChildren();// get a list of the node's children
	int getNumberOfTrials();// get the number of times this node has been played out
	float getValue();//get the % of the time which you win from this position
	boolean isLeaf();//returns whether the node is a leaf (does it have any children yet)
	void generateChildren();
}
