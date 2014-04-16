package uctMonteCarlo;

import java.util.List;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class SuperTreeNode implements TreeNode {
	protected TreeNode parent;
	List<Child> children;
	private int numberOfTrials;
	private float expectedWins;
	private float uncertainty;
	private float expectedWinsSquaredSum;
	Colour whoseTurn;	

	
	public SuperTreeNode(List<Child> children, Colour whoseTurn, TreeNode parent){
		this.children = children;
		this.whoseTurn = whoseTurn;
		this.parent = parent;
	}
	public SuperTreeNode(Colour whoseTurn){
		this.whoseTurn=whoseTurn;
	}
	
	
	public float getMaxValue(){
		return expectedWins+uncertainty;
	}
	
	public float getMinValue(){
		return expectedWins-uncertainty;
	}
	
	@Override
	public List<Child> getChildren() {
		return children;
	}

	@Override
	public int getNumberOfTrials() {
		return numberOfTrials;
	}

	@Override
	public float getValue() {
		return expectedWins;
	}

	@Override
	public boolean isLeaf() {
		return children.size()==0;
	}

	@Override
	public int generateChildren(GoBoard goBoard) {
		Colour nextTurn = Colour.GREY;
		if(whoseTurn==Colour.BLACK){
			nextTurn = Colour.WHITE;
		}
		else{
			nextTurn = Colour.BLACK;
		}
		
		int generated = 0;
		for(int i=Global.board_size+2;i<Global.array_size;i++){
			if(goBoard.check(i,whoseTurn)){
				children.add(new Child(new MyLinkedListTreeNode(new MyLinkedList<Child>(), nextTurn, parent),i));
				generated++;
			}
		}
		playAllChildrenOnce(goBoard);
		return generated;
	}
	private void playAllChildrenOnce(GoBoard goBoard){
		for(int i=0;i<children.size();i++){
			GoBoard newBoard = goBoard.clone();
			Child child = children.get(i);
			newBoard.put(whoseTurn, child.getMove());
			Colour winner = newBoard.randomPlayout(Global.opponent(whoseTurn));
			child.node.update(winner);
		}
		
	}

	@Override
	public void update(Colour winner) {
				if(winner==Colour.BLACK)expectedWins=((expectedWins*numberOfTrials)+1)/(numberOfTrials+1);
				else expectedWins = (expectedWins*numberOfTrials)/(numberOfTrials+1);

				expectedWinsSquaredSum = ((expectedWinsSquaredSum*numberOfTrials)+expectedWins*expectedWins)/(numberOfTrials+1);
				numberOfTrials++;
				TreeNode treeHead = getTreeHead();
				int n = treeHead.getNumberOfTrials();//total number of trials done
				float logN = (float) Math.log(n);
				float v =(float) (expectedWinsSquaredSum - (expectedWins*expectedWins) + Math.sqrt(logN/numberOfTrials));
				float multiplier = Math.min(1/4, v);
				uncertainty = (float) Math.sqrt((logN/numberOfTrials)*multiplier);
				if(parent!=null) parent.update(winner);				
	}

	private TreeNode getTreeHead() {
		TreeNode node = this;
		while(node.parent!=null){
			node = node.parent;
		}
		return node;
	}
	@Override
	public Colour getWhoseTurn() { 
		return whoseTurn;
	}

	@Override
	public Child getMaxChild() {
		Child currentMaxChild = children.get(0);
		float currentMaxValue = currentMaxChild.node.getMaxValue();
		for(int i=0;i<children.size();i++){
			Child child = children.get(i);
			float val = child.node.getMaxValue();
			if(val >currentMaxValue){
				currentMaxChild = child;
				currentMaxValue = val;				
			}
		}
		return currentMaxChild;
	}

	@Override
	public Child getMinChild() {
		Child currentMinChild = children.get(0);
		float currentMinValue = currentMinChild.node.getMinValue();
		for(int i=0;i<children.size();i++){
			Child child = children.get(i);
			float val = child.node.getMinValue();
			if(val<currentMinValue){
				currentMinChild = child;
				currentMinValue = val;				
			}
		}
		return currentMinChild;
	}

	@Override
	public TreeNode makeMove(int move) {
		TreeNode node = null;
		for(int i=0;i<children.size();i++){
			Child child = children.get(i);
			if(child.move==move){
				node = child.node;			
			}
		}
		return node;
	}	
}
