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
	
	void update(Colour winner);//updates a node and propagates the update back to the tree head
	void amafUpdate(Colour winner, List<Integer> moves);//updates a node and propagates the update using AMAF to the head
	void singleUpdate(Colour winner);//updates just the node, and doesn't propagate. used in amafupdate
	
	Colour getWhoseTurn();
	
	Child getChild(UpdateType updateType, Colour colour);
	
	float getValue(UpdateType updateType, Colour colour);
	
	TreeNode makeMove(int move);
	void detach();
	void childPrint();
	int generateChildren(GoBoard goBoard, UpdateType updateType);
}
