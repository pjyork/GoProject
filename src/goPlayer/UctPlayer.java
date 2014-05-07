package goPlayer;
import uctMonteCarlo.*;
import boardRep.Colour;

public class UctPlayer implements GoPlayer{
	private UCTSearch searcher;
	private long searchTime;//amount of time spent searching per move if searching by time
	private int numberOfIterations;//number of iterations of UCT if searching by number of iterations
	private Colour player;
	
	
	public UctPlayer (UCTSearch searcher, long searchTime, int numberOfIterations, Colour player){
		this.searcher=searcher;
		this.searchTime = searchTime;
		this.numberOfIterations = numberOfIterations;
		this.player=player;
	}
	
	public int findMoveTime(){//find a move using the alotted time
		return searcher.findAMove(player, searchTime);
	}
	
	public int findMoveIter(){//find a move using the alotted number of iterations
		return searcher.findAMove(player, numberOfIterations);
	}
	
	public void notifyOpponentsMove(int move){//update the searcher with the opponent's move
		searcher.makeMove(move);
	}
	
	public void resetSearcher(){
		searcher.reset();
	}
	
}
