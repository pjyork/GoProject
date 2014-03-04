package boardRep;

import boardView.BoardView;

public class BoardListener {
	BoardView boardView;
	public BoardListener(BoardView boardView){
		this.boardView=boardView;
	}
	
	public void boardChanged(){
		boardView.update();
	}
}
