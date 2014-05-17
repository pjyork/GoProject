package goPlayer;

public interface GoPlayer {
	public int findMoveTime();
	public int findMoveIter();
	public void notifyOpponentsMove(int move);
	public void resetSearcher();
	public void loadTree();
	public void printProfiling();
}
