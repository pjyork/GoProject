package uctMonteCarlo;

import java.io.FileNotFoundException;

import boardRep.Colour;

public interface UCTSearch {
	public int findAMove(Colour whoseTurnStart, int trials);
	public int findAMove(Colour whoseTurnStart, long timeInMillis);

	public void makeMove(int move);
	public void reset();
	public void loadTree() throws FileNotFoundException;
	public void printProfiling();
}
