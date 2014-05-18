package uctMonteCarlo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

import boardRep.Colour;
import boardRep.Global;
import boardRep.GoBoard;

public class UCTSearchBasic implements UCTSearch {
	TreeNode treeHead;
	int totalNumberOfTrials;
	GoBoard goBoard;
	UpdateType updateType;
	long avgSearchTime = 0;
	int searches = 0;
	
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
		long start = System.currentTimeMillis();
		while(!node.isLeaf()){	
			whoseTurn = node.getWhoseTurn();
			child =node.getChild(updateType,whoseTurn);
			node = child.getNode();
			newBoard.put(whoseTurn, child.move);
		}
		start = System.currentTimeMillis()-start;
		avgSearchTime = (avgSearchTime*searches+start)/(searches+1);
		searches++;
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
		if(move==0){
			move++;
			move--;
		}
		return move; 
	}
	@Override
	public void reset() {

		LinkedList<Child> children = new LinkedList<Child>();
		treeHead = new LinkedListTreeNode(children, Colour.BLACK, null,0);
		treeHead.generateChildren(goBoard, updateType);
		
	}
	
	public void saveTree() throws IOException{
		long timeStart = System.currentTimeMillis();
		while(System.currentTimeMillis()-timeStart<420000)treeSearch(Colour.BLACK);
		try {
            /* Create the output stream */
			FileOutputStream ostream = new FileOutputStream("C:\\Users\\Peter\\git\\go\\MC-UDT Go\\src\\tree.sav");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            SuperTreeNode treeHead1 = (SuperTreeNode) treeHead;
            p.writeObject(treeHead1); // Write the tree to the stream.
            p.flush();
            ostream.close();    // close the file.
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
	@Override
	public void loadTree() throws FileNotFoundException {
		FileInputStream istream = new FileInputStream("C:\\Users\\Peter\\git\\go\\MC-UDT Go\\src\\tree.sav");
		try {
            ObjectInputStream in = new ObjectInputStream(istream);
            treeHead = (SuperTreeNode) in.readObject();
            istream.close();    // close the file.
        } catch (Exception ex) {
            ex.printStackTrace();
        }
		
	}
	@Override
	public void printProfiling() {
		System.out.println("average treeSearch - " + avgSearchTime);
		treeHead.printProfiling();
		
	}
}
