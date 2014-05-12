package uctMonteCarlo;

import java.util.LinkedList;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class UCTSearchBasic implements UCTSearch {
	TreeNode treeHead;
	int totalNumberOfTrials;
	GoBoard goBoard;
	UpdateType updateType;
	
	public UCTSearchBasic(TreeNode treeHead, GoBoard goBoard,UpdateType updateType){
		this.updateType = updateType;
		this.treeHead=treeHead;
		this.treeHead.generateChildren(goBoard,updateType);
		this.goBoard = goBoard;
	}
	private long treeSearch(Colour whoseTurnStart){
		
		
		GoBoard newBoard = goBoard.clone();
		TreeNode node = treeHead;
		Colour whoseTurn = whoseTurnStart;
		Child child = null;
		while(!node.isLeaf()){			
			
			whoseTurn = node.getWhoseTurn();
			child =node.getChild(updateType,whoseTurn);
			node = child.getNode();
			newBoard.put(whoseTurn, child.move);
		}
		return node.generateChildren(newBoard,updateType);
		
	}
	public int findAMove(Colour whoseTurnStart, int trials){
		int move = 0,trialsDone=0;
		
		while(trialsDone<trials){
			treeSearch(whoseTurnStart);
			trialsDone++;
		}
		
		if(!treeHead.isLeaf()){
			
			Child child = treeHead.getChild(updateType, whoseTurnStart);
			move = child.getMove();
			this.treeHead = child.node;
			
			treeHead.detach();
			if(treeHead.isLeaf()){
				GoBoard newBoard = goBoard.clone();
				newBoard.put(whoseTurnStart, move);
				treeHead.generateChildren(newBoard,updateType);
			}
		}
		else move = 0; 
		return move; 	
	}
	@Override
	public void makeMove(int move) {
		//treeHead.childPrint();
		TreeNode candidateNode = treeHead.makeMove(move);
		
		if(candidateNode!=null){
			treeHead = candidateNode;
			treeHead.detach();
			if(treeHead.isLeaf()) treeHead.generateChildren(goBoard,UpdateType.BASIC);
		}
	}
	@Override
	public int findAMove(Colour whoseTurnStart, long timeInMillis) {
		long timeStart = System.currentTimeMillis();
		int move = 0;
		long nodesGenerated = 0;
		long treeSearches=0;
		if(treeHead.getChildren().size()>1){
			while(System.currentTimeMillis()-timeStart<timeInMillis){
				nodesGenerated+=treeSearch(whoseTurnStart);
				treeSearches++;
			}
		}
		//treeHead.childPrint();
		if(!treeHead.isLeaf()){
			
			Child child = treeHead.getChild(updateType,whoseTurnStart);
			move = child.getMove();
			this.treeHead = child.node;
			
			treeHead.detach();
			if(treeHead.isLeaf()){
				GoBoard newBoard = goBoard.clone();
				newBoard.put(whoseTurnStart, move);
				treeHead.generateChildren(newBoard,updateType);
			}
		}
		else move = 0; 
		//System.out.println("nodes generated - " + nodesGenerated);
		//System.out.println("time taken total - " + (System.currentTimeMillis() - timeStart));
		//System.out.println(treeSearches + " Tree Seaches");
		
		return move; 
	}
	@Override
	public void reset() {

		LinkedList<Child> children = new LinkedList<Child>();
		treeHead = new LinkedListTreeNode(children, Colour.BLACK, null,0);
		treeHead.generateChildren(goBoard, updateType);
		
	}
}
