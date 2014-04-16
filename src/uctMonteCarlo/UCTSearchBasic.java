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
		long timeStart = System.currentTimeMillis();
		int move = 0;
		TreeNode node = treeHead;
		long nodesGenerated = 0;
		for(int i=0; i<trials;i++){
			//System.out.println("iteration - " + i);
			long iterStart = System.currentTimeMillis();
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
		
			nodesGenerated +=node.generateChildren(newBoard);
			
			//System.out.println("iteration " + i + " time - " + (System.currentTimeMillis()-iterStart));
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
		//System.out.println("nodes generated - " + nodesGenerated);
		//System.out.println("time taken total - " + (System.currentTimeMillis() - timeStart));
		//System.out.println("move - " + move);
		return move; 	
	}
	@Override
	public void makeMove(int move) {
		if(treeHead.isLeaf()) treeHead.generateChildren(goBoard);
		TreeNode candidateNode = treeHead.makeMove(move);
		if(candidateNode!=null)treeHead = candidateNode;
	}
}
