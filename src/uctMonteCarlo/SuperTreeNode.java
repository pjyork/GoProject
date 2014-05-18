package uctMonteCarlo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class SuperTreeNode implements TreeNode, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6460876416781555954L;
	protected TreeNode parent;
	List<Child> children;
	
	private int numberOfTrials;
	private float expectedWins;
	private float uncertainty;
	private float expectedWinsSquaredSum;
	
	private int amafNumberOfTrials;
	private float amafExpectedWins;
	private float amafUncertainty;
	private float amafExpectedWinsSquaredSum;
	
	private int moveMadeToGetHere;
	
	long averageUpdateTime = 0,averagePlayoutTime=0;
	int updates=0,playouts=0;
	
	public int raveParam=10000;
	Colour whoseTurn;	

	
	public SuperTreeNode(List<Child> children, Colour whoseTurn, TreeNode parent, int move){
		this.children = children;
		this.whoseTurn = whoseTurn;
		this.parent = parent;
		this.moveMadeToGetHere=move;
	}
	public SuperTreeNode(Colour whoseTurn){
		this.whoseTurn=whoseTurn;
	}
	
	
	public float getBlackValue(){
		return expectedWins+uncertainty;
	}
	
	public float getWhiteValue(){
		return (1-expectedWins)+uncertainty;
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
	public int generateChildren(GoBoard goBoard, UpdateType updateType) {
		Colour nextTurn = Colour.GREY;
		if(whoseTurn==Colour.BLACK){
			nextTurn = Colour.WHITE;
		}
		else{
			nextTurn = Colour.BLACK;
		}
		
		
		children.add(new Child(new LinkedListTreeNode(new LinkedList<Child>(),nextTurn, this,0),0));
		int generated = 1;
		
		for(int i=Global.board_size+2;i<Global.array_size;i++){
			if(goBoard.check(i,whoseTurn)){
				children.add(new Child(new LinkedListTreeNode(new LinkedList<Child>(), nextTurn, this,i),i));
				generated++;
			}
		}
		playAllChildrenOnce(goBoard, updateType);
		return generated;
	}
	private void playAllChildrenOnce(GoBoard goBoard, UpdateType updateType){
		for(int i=0;i<children.size();i++){
			GoBoard newBoard = goBoard.clone();
			Child child = children.get(i);
			newBoard.put(whoseTurn, child.getMove());
			long randomStart = 0;
			randomStart = System.currentTimeMillis()-randomStart;
			Colour winner = newBoard.randomPlayout(Global.opponent(whoseTurn));
			averagePlayoutTime= (averagePlayoutTime*playouts+randomStart)/(playouts+1);
			playouts++;
			long start = System.currentTimeMillis();
			switch(updateType){
				case BASIC: child.node.update(winner); break;
				case AMAF: child.node.amafUpdate(winner, new TreeSet<Integer>(),new TreeSet<Integer>()); break;
				case RAVE: child.node.amafUpdate(winner, new TreeSet<Integer>(),new TreeSet<Integer>());
							child.node.update(winner); break;
			}
			start = System.currentTimeMillis()-start;
			averageUpdateTime = (averageUpdateTime*updates+start)/(updates+1);
			updates++;
		}
		
	}
	
	@Override
	public void update(Colour winner) {
				if(parent!=null){ 
					parent.update(winner);		
					if(winner==Colour.BLACK)expectedWins=((expectedWins*numberOfTrials)+1)/(numberOfTrials+1);
					else expectedWins = (expectedWins*numberOfTrials)/(numberOfTrials+1);
	
					expectedWinsSquaredSum = ((expectedWinsSquaredSum*numberOfTrials)+expectedWins*expectedWins)/(numberOfTrials+1);
					numberOfTrials++;
					int n = parent.getNumberOfTrials()-1;
					
					float logN = (float) Math.log(n);
					float v =(float) (expectedWinsSquaredSum - (expectedWins*expectedWins) + Math.sqrt(logN/numberOfTrials));
					float multiplier = Math.min((float)(0.25), v);
					uncertainty = (float) Math.sqrt((logN/numberOfTrials)*multiplier);	
				}
				else numberOfTrials++;
	}

	@Override
	public Colour getWhoseTurn() { 
		return whoseTurn;
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
	@Override
	public void detach() {
		parent = null;		
	}
	@Override
	public void  childPrint() {
		for(int i=0;i<children.size();i++){
			Child child = children.get(i);
			System.out.println("Move - "+Global.posString(child.move)+" ");
			
		}
	}
	@Override
	public void amafUpdate(Colour winner, TreeSet<Integer> whiteMoves, TreeSet<Integer> blackMoves) {
		if(parent!=null){
			if(whoseTurn==Colour.WHITE)blackMoves.add(moveMadeToGetHere);
			else whiteMoves.add(moveMadeToGetHere);
			parent.amafUpdate(winner,whiteMoves,blackMoves);	
			if(winner==Colour.BLACK)amafExpectedWins=((amafExpectedWins*amafNumberOfTrials)+1)/(amafNumberOfTrials+1);
			else amafExpectedWins = (amafExpectedWins*amafNumberOfTrials)/(amafNumberOfTrials+1);

			expectedWinsSquaredSum = ((amafExpectedWinsSquaredSum*amafNumberOfTrials)+amafExpectedWins*amafExpectedWins)/(amafNumberOfTrials+1);
			amafNumberOfTrials++;
			int n = parent.getNumberOfAMAFTrials()-1;
			
			float logN = (float) Math.log(n);
			float v =(float) (amafExpectedWinsSquaredSum - (amafExpectedWins*amafExpectedWins) + Math.sqrt(logN/amafNumberOfTrials));
			float multiplier = Math.min((float)(0.25), v);
			amafUncertainty = (float) Math.sqrt((logN/amafNumberOfTrials)*multiplier);
			
		}
		else numberOfTrials++;
		for(int i=0;i<children.size();i++){
			if(whoseTurn==Colour.WHITE){
				Child child = children.get(i);
				if(whiteMoves.contains(child.getMove())){
					child.getNode().singleUpdate(winner);
				}
			}
			else{
				Child child = children.get(i);
				if(blackMoves.contains(child.getMove())){
					child.getNode().singleUpdate(winner);
				}
			
			}
			
		}
		
	}
	@Override
	public void singleUpdate(Colour winner) {
		if(winner==Colour.BLACK)amafExpectedWins=((amafExpectedWins*amafNumberOfTrials)+1)/(amafNumberOfTrials+1);
		else amafExpectedWins = (amafExpectedWins*amafNumberOfTrials)/(amafNumberOfTrials+1);

		expectedWinsSquaredSum = ((amafExpectedWinsSquaredSum*amafNumberOfTrials)+amafExpectedWins*amafExpectedWins)/(amafNumberOfTrials+1);
		amafNumberOfTrials++;
		int n = parent.getNumberOfAMAFTrials();
		
		float logN = (float) Math.log(n);
		float v =(float) (amafExpectedWinsSquaredSum - (amafExpectedWins*amafExpectedWins) + Math.sqrt(logN/amafNumberOfTrials));
		float multiplier = Math.min((float)(0.25), v);
		amafUncertainty = (float) Math.sqrt((logN/amafNumberOfTrials)*multiplier);
	}


	

	
	public Child getChild(UpdateType updateType, Colour colour){
		Child currentMaxChild = children.get(0);	
		float currentMaxValue = 0;
		
	
		
		for(int i=0;i<children.size();i++){
			Child child = children.get(i);
			float val = child.node.getValue(updateType,colour);
			if(val >currentMaxValue){
				currentMaxChild = child;
				currentMaxValue = val;				
			}
		}
		return currentMaxChild;
	}
	
	@Override
	public float getValue(UpdateType updateType, Colour colour) {
		float v1=0,u1=0,v2=0,u2=0,alpha = 0;
		switch (updateType){
		case AMAF: v1 = amafExpectedWins; u1 = amafUncertainty;
			break;
		case BASIC: v1 = expectedWins; u1 = uncertainty;v2=0;u2=0;
			break;
		case RAVE: v1 = expectedWins; u1 = uncertainty;v2 = amafExpectedWins; u2 = amafUncertainty; 
					alpha = ((float)(raveParam-numberOfTrials))/(float)raveParam;
			break;
		default:
			break;
		
		}
		if(colour==Colour.WHITE){
			return (1-alpha)*((1-v1)+u1) + alpha*((1-v2)+u2); 
		}
		else{
			return (1-alpha)*((v1)+u1) + alpha*((v2)+u2); 
		}
	}
	@Override
	public void printProfiling() {
		System.out.println("average update time - " + averageUpdateTime);
	}
	@Override
	public int getNumberOfAMAFTrials() {
		return amafNumberOfTrials;
	}
}
