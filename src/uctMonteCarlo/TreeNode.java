package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.GoBoard;

public interface TreeNode {
	TreeNode parent = null;
	List<Child> getChildren();// get a list of the node's children
	int getNumberOfTrials();// get the number of times this node has been played out
	float getValue();//get the % of the time which you win from this position
	boolean isLeaf();//returns whether the node is a leaf (does it have any children yet)
	void generateChildren(GoBoard goBoard);
	void update(Colour winner);
	Colour getWhoseTurn();
	Child getMaxChild();
	Child getMinChild();
	float getMaxValue();
	float getMinValue();
	TreeNode makeMove(int move);
}
