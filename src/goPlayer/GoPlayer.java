package goPlayer;
import java.awt.BorderLayout;

import uctMonteCarlo.*;


import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;








import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import boardRep.BoardListener;
import boardRep.Colour;
import boardRep.GoBoard;
import boardRep.Global;
import boardView.BoardView;


public class GoPlayer extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final GoBoard goBoard;
	protected final BoardView boardView;
	protected final UCTSearch searcher;
	
	
	public GoPlayer(GoBoard goBoard, UCTSearch searcher){
		this.goBoard = goBoard;
		this.boardView = new BoardView(this, goBoard);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(BorderLayout.CENTER,boardView);
		contentPane.requestFocusInWindow();
		setContentPane(contentPane);
		BoardListener bl = new BoardListener(boardView);
		goBoard.addListener(bl);
		this.searcher=searcher;
	}
	
	private static void playDet(GoBoard board){
		int i =0;
		int j =0;
		int playcount=0;
		while(!board.isFull()&&playcount<10000){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			playcount++;
			if(i==1){
			if(board.put(Colour.WHITE, j)){
				i = ++i%2;
				if(board.isFull()){
					int k = 0;
				}
			}			
		}
		else{
			if(board.put(Colour.BLACK, j)){
				i = ++i%2;
				if(board.isFull()){
					int k = 0;
				}
			}
		}
		j=(j+1)%Global.array_size;
		
		
		}
	}

	private static void playRandom(GoBoard board){
		int i =0;
		int j =0;
		int playcount=0;
		while(!board.isFull()&&playcount<100000){
			try {
				
				Thread.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			playcount++;
			if(i==1){
			if(board.put(Colour.WHITE, (int) (Math.random()*(Global.array_size)))){
				i = ++i%2;
			}
		}
		else{
			if(board.put(Colour.BLACK, (int) (Math.random()*(Global.array_size)))){
				i = ++i%2;
			}
		}
		
		}
		
		//board.randomPlayout(Colour.BLACK);

	}
	
	public void updateSearcher(int move){
		searcher.makeMove(move);
	}
	
	public void computerPlay(){
		int move = this.searcher.findAMove(Colour.WHITE,1000);
		goBoard.put(Colour.WHITE, move);
	}
	
	
	public static void main(String[] args)
	{	
		GoBoard board = new GoBoard();
		MyLinkedList<Child> children = new MyLinkedList<Child>();
		TreeNode treeHead = new MyLinkedListTreeNode(children, Colour.BLACK, null);
		UCTSearch searcher = new UCTSearchBasic(treeHead, board);
		GoPlayer goPlayer = new GoPlayer(board, searcher);
		goPlayer.setLocation(100, 100);
		goPlayer.setSize(675,675);
		goPlayer.setVisible(true);
		//playRandom(board);
		//playDet(board);
		compVsRandom(board,searcher);
		
		
		/*Colour winner = board.scoreBoard();
		if(winner==Colour.WHITE) System.out.println("white wins");
		else if(winner==Colour.BLACK) System.out.println("black wins");
		else System.out.println("Draw");
		*/
		
	}

	private static void compVsRandom(GoBoard board, UCTSearch searcher) {
		int games = 0,moves=0,wins=0;
		while(games<50){
			MyLinkedList<Child> children = new MyLinkedList<Child>();
			TreeNode treeHead = new MyLinkedListTreeNode(children, Colour.BLACK, null);
			UCTSearch searcherr = new UCTSearchBasic(treeHead, board);
			while(moves<5000&&!board.isFull()){
				int blackMove = (int) ((Math.random()*(Global.array_size-1))+1);
				if(board.put(Colour.BLACK,blackMove )){	
					//System.out.println("black - " + blackMove);
					searcherr.makeMove(blackMove);
					int whiteMove = searcherr.findAMove(Colour.WHITE,(long) 15000);
					board.put(Colour.WHITE, whiteMove);
					//System.out.println("white - " + whiteMove);
				}
				moves+=1;
				if(moves%1000==0){
					System.out.println("move " + moves +  " taken");
				}
				if(board.isFull()){
					moves+=5000;
				}
			}
			if(board.scoreBoard()==Colour.WHITE) wins++;
			games++;
			System.out.println(wins+" / " + games);
			board.reset();
			moves = 0;
		}
		
		System.out.println(wins);
	}
}
