package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.GoBoard;

public class LinkedListTreeNode extends SuperTreeNode {

	public LinkedListTreeNode(List<Child> children, GoBoard goBoard,
			Colour whoseTurn, TreeNode parent) {
		super(children, whoseTurn, parent);
	}

}
