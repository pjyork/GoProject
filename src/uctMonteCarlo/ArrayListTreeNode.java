package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.GoBoard;

public class ArrayListTreeNode extends SuperTreeNode {

	public ArrayListTreeNode(List<Child> children, GoBoard goBoard,
			Colour whoseTurn) {
		super(children, goBoard, whoseTurn);
	}

}
