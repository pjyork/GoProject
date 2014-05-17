package uctMonteCarlo;

import java.io.Serializable;

public class Child implements Serializable{
	TreeNode node;
	int move;
	
	public Child(TreeNode node, int move){
		this.node = node;
		this.move = move;
	}
	
	public TreeNode getNode(){
		return node;
	}
	public int getMove(){
		return move;
	}
	
	public float getValue(){
		return node.getValue();
	}
}
