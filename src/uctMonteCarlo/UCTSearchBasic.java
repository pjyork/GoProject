package uctMonteCarlo;

import boardRep.Colour;
import boardRep.GoBoard;

public class UCTSearchBasic implements UCTSearch {
	TreeNode treeHead;
	int totalNumberOfTrials;
	GoBoard goBoard;
	public UCTSearchBasic(TreeNode treeHead, GoBoard goBoard){
		this.treeHead=treeHead;
		this.goBoard = goBoard;
	}
	public int findAMove(Colour whoseTurnStart, int trials){
		
		int move = 0;
		TreeNode node = treeHead;
		int nodesGenerated = 0;
		for(int i=0; i<trials;i++){
			GoBoard newBoard = goBoard.clone();
			node = treeHead;
			Colour whoseTurn = whoseTurnStart;
			Child child = null;
			while(!node.isLeaf()){				
				whoseTurn = node.getWhoseTurn();
				if(whoseTurn==Colour.BLACK){
					child =node.getMaxChild();
				}
				else{
					child = node.getMinChild();					
				}
				node = child.getNode();
				newBoard.put(whoseTurn, child.move);
			}

						
			node.generateChildren(newBoard);
			nodesGenerated += node.getChildren().size();
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
		System.out.println("nodes generated - " + nodesGenerated);
		return move;
	}
	@Override
	public void makeMove(int move) {
		if(treeHead.isLeaf()) treeHead.generateChildren(goBoard);
		treeHead = treeHead.makeMove(move);
	}
}
