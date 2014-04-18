package uctMonteCarlo;

import boardRep.Colour;
import boardRep.GoBoard;

public class UCTSearchBasic implements UCTSearch {
	TreeNode treeHead;
	int totalNumberOfTrials;
	GoBoard goBoard;
	public UCTSearchBasic(TreeNode treeHead, GoBoard goBoard){
		this.treeHead=treeHead;
		this.treeHead.generateChildren(goBoard);
		this.goBoard = goBoard;
	}
	private long treeSearch(Colour whoseTurnStart){
		
		//System.out.println("iteration - " + i);
		long iterStart = System.currentTimeMillis();
		GoBoard newBoard = goBoard.clone();
		TreeNode node = treeHead;
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
		return node.generateChildren(newBoard);
		
		//System.out.println("iteration " + i + " time - " + (System.currentTimeMillis()-iterStart));
	}
	public int findAMove(Colour whoseTurnStart, int trials){
		long timeStart = System.currentTimeMillis();
		int move = 0;
		TreeNode node = treeHead;
		long nodesGenerated = 0;
		
		for(int i=0; i<trials;i++){
			nodesGenerated+=treeSearch(whoseTurnStart);
		}
		if(!treeHead.isLeaf()){
			if(whoseTurnStart==Colour.BLACK){
				Child child = treeHead.getMaxChild();
				this.treeHead = child.node;
				move = child.getMove();
			}
			else{
				Child child = treeHead.getMinChild();
				move = child.getMove();
				this.treeHead = child.node;
			}
			treeHead.detach();
			if(treeHead.isLeaf()){
				GoBoard newBoard = goBoard.clone();
				newBoard.put(whoseTurnStart, move);
				treeHead.generateChildren(newBoard);
			}
		}
		else move = 0; 
		//System.out.println("nodes generated - " + nodesGenerated);
		//System.out.println("time taken total - " + (System.currentTimeMillis() - timeStart));
		return move; 	
	}
	@Override
	public void makeMove(int move) {
		TreeNode candidateNode = treeHead.makeMove(move);
		
		if(candidateNode!=null){
			treeHead = candidateNode;
			treeHead.detach();
			if(treeHead.isLeaf()) treeHead.generateChildren(goBoard);
		}
	}
	@Override
	public int findAMove(Colour whoseTurnStart, long timeInMillis) {
		long timeStart = System.currentTimeMillis();
		int move = 0;
		TreeNode node = treeHead;
		long nodesGenerated = 0;
		long treeSearches=0;
		while(System.currentTimeMillis()-timeStart<timeInMillis){
			nodesGenerated+=treeSearch(whoseTurnStart);
			treeSearches++;
		}
		if(!treeHead.isLeaf()){
			if(whoseTurnStart==Colour.BLACK){
				Child child = treeHead.getMaxChild();
				this.treeHead = child.node;
				move = child.getMove();
			}
			else{
				Child child = treeHead.getMinChild();
				move = child.getMove();
				this.treeHead = child.node;
			}
			treeHead.detach();
			if(treeHead.isLeaf()){
				GoBoard newBoard = goBoard.clone();
				newBoard.put(whoseTurnStart, move);
				treeHead.generateChildren(newBoard);
			}
		}
		else move = 0; 
		//System.out.println("nodes generated - " + nodesGenerated);
		//System.out.println("time taken total - " + (System.currentTimeMillis() - timeStart));
		//System.out.println(treeSearches + " Tree Seaches");
		return move; 
	}
}
