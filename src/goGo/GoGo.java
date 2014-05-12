package goGo;
import goPlayer.*;

import java.awt.BorderLayout;

import uctMonteCarlo.*;


import java.awt.Insets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;








import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import boardRep.BoardListener;
import boardRep.Colour;
import boardRep.GoBoard;
import boardView.BoardView;


public class GoGo extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final GoBoard goBoard;
	protected final BoardView boardView;
	public PlayMode playMode;
	private GoPlayer whitePlayer,blackPlayer;
	
	
	public GoGo(GoBoard goBoard){
		this.goBoard = goBoard;
		this.boardView = new BoardView(this, goBoard);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(BorderLayout.CENTER,boardView);
		contentPane.add(BorderLayout.NORTH, getToolBar());
		contentPane.requestFocusInWindow();
		setContentPane(contentPane);
		BoardListener bl = new BoardListener(boardView);
		goBoard.addListener(bl);
		this.playMode= PlayMode.NOTPLAYING;
	}
	
	
	
	protected JToolBar getToolBar() {
		List<JButton> buttons = new ArrayList<JButton>();
		JButton cvrButton = new JButton("CompVsRandom");		
		new SelectPlayMode(this, PlayMode.COMPVSRANDOM, cvrButton);
		buttons.add(cvrButton);
		

		JButton avuButton = new JButton("AvsU");
		new SelectPlayMode(this, PlayMode.AMAFVSUCT , avuButton);
		buttons.add(avuButton);
		
		JButton pvcButton = new JButton("PlayVsBasic");
		new SelectPlayMode(this, PlayMode.PLAYERVSBASIC, pvcButton);
		buttons.add(pvcButton);
		
		JButton pvaButton = new JButton("PlayVsAMAF");
		new SelectPlayMode(this, PlayMode.PLAYERVSAMAF, pvaButton);
		buttons.add(pvaButton);
		
		JButton pvpButton = new JButton("PlayerVsPlayer");
		new SelectPlayMode(this, PlayMode.PLAYERVSPLAYER, pvpButton);
		buttons.add(pvpButton);
		
		JButton evbButton = new JButton("EvaluateBoard");
		new SelectPlayMode(this, PlayMode.EVALBOARD, evbButton);
		buttons.add(evbButton);
		
		JButton stopButton = new JButton("Stop");
		new SelectPlayMode(this, PlayMode.NOTPLAYING, stopButton);
		buttons.add(stopButton);
		
		JButton rvuButton = new JButton("RvsU");
		new SelectPlayMode(this, PlayMode.RAVEVSUCT, rvuButton);
		buttons.add(rvuButton);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(10,10,10,10));
		toolBar.add(cvrButton);
		toolBar.add(avuButton);
		toolBar.add(rvuButton);
		toolBar.add(pvcButton);
		toolBar.add(pvaButton);
		toolBar.add(pvpButton);
		toolBar.add(evbButton);
		toolBar.add(stopButton);
		return toolBar;
	}
	
	

	
	public void updateSearcher(int move,Colour colour){
		if(colour==Colour.BLACK){
			whitePlayer.notifyOpponentsMove(move);
		}
		else{
			blackPlayer.notifyOpponentsMove(move);
		}
	}
	
	public void computerPlay(){
		int move = whitePlayer.findMoveTime();
		goBoard.put(Colour.WHITE, move);
	}
	
	
	public static void main(String[] args)
	{	
		GoBoard board = new GoBoard();
		MyLinkedList<Child> children = new MyLinkedList<Child>();
		TreeNode treeHead = new MyLinkedListTreeNode(children, Colour.BLACK, null,0);
		GoGo goPlayer = new GoGo(board);
		goPlayer.setLocation(100, 100);
		goPlayer.setSize(675,675);
		goPlayer.setVisible(true);
		
		
		
		
		
	}

	private void compVsRandom() {
		int games = 0,moves=0,wins=0,passes=0,realMoves=0;
		while(games<300&&playMode==PlayMode.COMPVSRANDOM){
			while(moves<5000&&!goBoard.isFull()){
				int blackMove = blackPlayer.findMoveTime();
				if(goBoard.put(Colour.BLACK,blackMove)){	
					//System.out.println("black - " + blackMove);
					whitePlayer.notifyOpponentsMove(blackMove);
					int whiteMove = whitePlayer.findMoveTime();
					if(whiteMove==0)passes++;
					goBoard.put(Colour.WHITE, whiteMove);
					realMoves++;
				}
				moves+=1;
			}
			if(goBoard.scoreBoard()==Colour.WHITE) wins++;
			games++;
			System.out.print("passes - "+passes+" ");
			System.out.print(" moves - "+realMoves+" ");
			System.out.println(wins+" / " + games);
			goBoard.reset();
			whitePlayer.resetSearcher();
			moves = 0;
			realMoves = 0;
			passes = 0;
		}
		
		System.out.println(wins);
	}

	public void play(PlayMode playMode) {
		this.playMode = playMode;

		LinkedList<Child> children = new LinkedList<Child>();
		TreeNode treeHead = new LinkedListTreeNode(children, Colour.BLACK, null,0);
		LinkedList<Child> children1 = new LinkedList<Child>();
		TreeNode treeHead1 = new LinkedListTreeNode(children1, Colour.BLACK, null,0);
		switch(playMode){			
			case COMPVSRANDOM: goBoard.reset();
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 900, Colour.WHITE);
					blackPlayer = new RandomPlayer();
					compVsRandom();
					break;
			case PLAYERVSBASIC:	goBoard.reset();					
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 5000, 900, Colour.WHITE);
					break;
			case AMAFVSUCT: goBoard.reset(); 
							blackPlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 900, Colour.BLACK);
							whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.AMAF), (long) 500, 900, Colour.WHITE);
							compVsComp();
					break;
			case RAVEVSUCT: goBoard.reset(); 
							blackPlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 900, Colour.BLACK);
							whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.RAVE), (long) 500, 900, Colour.WHITE);
							compVsComp();
					break;
			case NOTPLAYING: goBoard.reset();
					break;
			case EVALBOARD: System.out.println(goBoard.scoreBoard()); goBoard.reset();
					break;			
			case PLAYERVSPLAYER: goBoard.reset();
					break;
			case PLAYERVSAMAF: goBoard.reset();
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.AMAF), (long) 5000, 900, Colour.WHITE);
					break;
		default:
			break;
		}
		
	}

	private void compVsComp() {
		int games = 0,moves=0,whiteWins=0,passes=0,blackWins=0;
		while(games<150&&(playMode==PlayMode.AMAFVSUCT||playMode==PlayMode.RAVEVSUCT)){
			while(moves<350&&!goBoard.isFull()){
				int blackMove = blackPlayer.findMoveTime();
				if(goBoard.put(Colour.BLACK,blackMove)){	
					//System.out.println("black - " + blackMove);
					whitePlayer.notifyOpponentsMove(blackMove);
					int whiteMove = whitePlayer.findMoveTime();
					if(whiteMove==0){
						passes++;
						if(blackMove==0){
							moves=250;
							System.out.print("game passed ");
						}
					}
					
					goBoard.put(Colour.WHITE, whiteMove);
					blackPlayer.notifyOpponentsMove(whiteMove);
				}
				moves+=1;
			}
			Colour winner = goBoard.scoreBoard();
			if(winner==Colour.WHITE)whiteWins++;
			else if(winner==Colour.BLACK)blackWins++;
			games++;
			if(playMode==PlayMode.AMAFVSUCT)System.out.print("AMAF-w BASIC-b");
			else System.out.print("RAVE-w BASIC-b");
			System.out.print("white - " +whiteWins);
			System.out.print("   black - " +blackWins);
			System.out.println("   games - " + games);
			goBoard.reset();
			whitePlayer.resetSearcher();
			blackPlayer.resetSearcher();
			moves = 0;
			passes = 0;
		}
		
		System.out.println(whiteWins);
	}
}
