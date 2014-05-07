package goGo;
import goPlayer.*;

import java.awt.BorderLayout;

import uctMonteCarlo.*;


import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;








import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import boardRep.BoardListener;
import boardRep.Colour;
import boardRep.GoBoard;
import boardRep.Global;
import boardView.BoardView;


public class GoGo extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final GoBoard goBoard;
	protected final BoardView boardView;
	protected final UCTSearch searcher;
	public PlayMode playMode;
	private GoPlayer whitePlayer,blackPlayer;
	
	
	public GoGo(GoBoard goBoard, UCTSearch searcher){
		this.goBoard = goBoard;
		this.boardView = new BoardView(this, goBoard);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(BorderLayout.CENTER,boardView);
		contentPane.add(BorderLayout.NORTH, getToolBar());
		contentPane.requestFocusInWindow();
		setContentPane(contentPane);
		BoardListener bl = new BoardListener(boardView);
		goBoard.addListener(bl);
		this.searcher=searcher;
		this.playMode= PlayMode.NOTPLAYING;
	}
	
	private void playDet(GoBoard board){
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
			}			
		}
		else{
			if(board.put(Colour.BLACK, j)){
				i = ++i%2;
			}
		}
		j=(j+1)%Global.array_size;
		
		
		}
	}
	
	
	protected JToolBar getToolBar() {
		List<JButton> buttons = new ArrayList<JButton>();
		JButton cvrButton = new JButton("CompVsRandom");		
		new SelectPlayMode(this, PlayMode.COMPVSRANDOM, cvrButton);
		buttons.add(cvrButton);
		

		JButton avuButton = new JButton("AMAFvsUCT");
		new SelectPlayMode(this, PlayMode.AMAFVSUCT , avuButton);
		buttons.add(avuButton);
		
		JButton pvcButton = new JButton("PlayVsComp");
		new SelectPlayMode(this, PlayMode.PLAYERVSCOMP, pvcButton);
		buttons.add(pvcButton);
		
		JButton pvpButton = new JButton("PlayerVsPlayer");
		new SelectPlayMode(this, PlayMode.PLAYERVSPLAYER, pvpButton);
		buttons.add(pvpButton);
		
		JButton evbButton = new JButton("EvaluateBoard");
		new SelectPlayMode(this, PlayMode.EVALBOARD, evbButton);
		buttons.add(evbButton);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(10,10,10,10));
		toolBar.add(cvrButton);
		toolBar.add(avuButton);
		toolBar.add(pvcButton);
		toolBar.add(pvpButton);
		toolBar.add(evbButton);
		return toolBar;
	}
	
	

	private void playRandom(GoBoard board, int turns){
		int i =0;
		int j =0;
		int playcount=0;
		while(!board.isFull()&&playcount<turns){
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
		GoBoard newBoard = board.clone();
		newBoard.check(50, Colour.WHITE);
		
		//board.randomPlayout(Colour.BLACK);

	}
	
	public void updateSearcher(int move){
		searcher.makeMove(move);
	}
	
	public void computerPlay(){
		int move = this.searcher.findAMove(Colour.WHITE,(long) 5000);
		goBoard.put(Colour.WHITE, move);
	}
	
	
	public static void main(String[] args)
	{	
		GoBoard board = new GoBoard();
		MyLinkedList<Child> children = new MyLinkedList<Child>();
		TreeNode treeHead = new MyLinkedListTreeNode(children, Colour.BLACK, null);
		UCTSearch searcher = new UCTSearchBasic(treeHead, board);
		GoGo goPlayer = new GoGo(board, searcher);
		goPlayer.setLocation(100, 100);
		goPlayer.setSize(675,675);
		goPlayer.setVisible(true);
		
		
		
		//playRandom(board, 60);
		//playDet(board);
		//compVsRandom(board,searcher);
		
		
		/*Colour winner = board.scoreBoard();
		if(winner==Colour.WHITE) System.out.println("white wins");
		else if(winner==Colour.BLACK) System.out.println("black wins");
		else System.out.println("Draw");
		*/
		
	}

	private void compVsRandom( UCTSearch searcher) {
		int games = 0,moves=0,wins=0,passes=0;
		while(games<50&&playMode==PlayMode.COMPVSRANDOM){
			while(moves<5000&&!goBoard.isFull()){
				int blackMove = blackPlayer.findMoveTime();
				if(goBoard.put(Colour.BLACK,blackMove)){	
					//System.out.println("black - " + blackMove);
					whitePlayer.notifyOpponentsMove(blackMove);
					int whiteMove = whitePlayer.findMoveTime();
					if(whiteMove==0)passes++;
					goBoard.put(Colour.WHITE, whiteMove);
				}
				moves+=1;
			}
			if(goBoard.scoreBoard()==Colour.WHITE) wins++;
			games++;
			System.out.print("passes - "+passes+" ");
			System.out.println(wins+" / " + games);
			goBoard.reset();
			whitePlayer.resetSearcher();
			moves = 0;
			passes = 0;
		}
		
		System.out.println(wins);
	}

	public void play(PlayMode playMode) {
		this.playMode = playMode;

		MyLinkedList<Child> children = new MyLinkedList<Child>();
		TreeNode treeHead = new MyLinkedListTreeNode(children, Colour.BLACK, null);
		switch(playMode){			
			case COMPVSRANDOM: goBoard.reset();
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard), (long) 500, 900, Colour.WHITE);
					blackPlayer = new RandomPlayer();
					compVsRandom(searcher);
					break;
			case PLAYERVSCOMP:	goBoard.reset();					
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard), (long) 500, 900, Colour.WHITE);
					
					break;
			case AMAFVSUCT: goBoard.reset(); amafVsUCT();
					break;
			case NOTPLAYING: goBoard.reset();
					break;
			case EVALBOARD: System.out.println(goBoard.scoreBoard()); goBoard.reset();
					break;	
			case PLAYERVSPLAYER: goBoard.reset();
					break;
		}
		
	}

	private void amafVsUCT() {
		// TODO Auto-generated method stub
		
	}
}
