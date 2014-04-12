package uctMonteCarlo;

import boardRep.Colour;

public class UCTSearch {
	TreeNode treeHead;
	int totalNumberOfTrials;
	public UCTSearch(TreeNode treeHead){
		this.treeHead=treeHead;
	}
	public int findAMove(Colour whoseTurnStart){
		
		int move = 0;
		TreeNode node = treeHead;
		for(int i=0; i<1000000;i++){
			node = treeHead;
			Colour whoseTurn = whoseTurnStart;
			while(!node.isLeaf()){
				whoseTurn = node.getWhoseTurn();
				if(whoseTurn==Colour.BLACK){
					node=node.getMaxChild().node;
				}
				else{
					node=node.getMinChild().node;
				}
			}
			node.generateChildren();			
		}
		
		if(whoseTurnStart==Colour.BLACK){
			Child child = treeHead.getMaxChild();
			this.treeHead = child.node;
			move = child.getMove();
		}
		else{
			Child child = treeHead.getMinChild();
			this.treeHead = child.node;
			move = child.getMove();
		}
		return move;
	}
}
