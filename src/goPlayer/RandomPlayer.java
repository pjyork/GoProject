package goPlayer;

import boardRep.Global;

public class RandomPlayer implements GoPlayer {
	
	
	
	@Override
	public int findMoveTime() {
		return (int) ((Math.random()*(Global.array_size-1))+1);
	}

	@Override
	public int findMoveIter() {
		return (int) ((Math.random()*(Global.array_size-1))+1);
	}

	@Override
	public void notifyOpponentsMove(int move) {		
	}

	@Override
	public void resetSearcher() {
	}
	
}
