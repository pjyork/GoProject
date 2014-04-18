package uctMonteCarlo;

import boardRep.Colour;

public interface UCTSearch {
	public int findAMove(Colour whoseTurnStart, int trials);
	public int findAMove(Colour whoseTurnStart, long timeInMillis);

	public void makeMove(int move);
}
