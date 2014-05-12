package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;

public class LinkedListTreeNode extends SuperTreeNode {

	public LinkedListTreeNode(List<Child> children,
			Colour whoseTurn, TreeNode parent,int move) {
		super(children, whoseTurn, parent,move);
	}

}
