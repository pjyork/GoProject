package goGo;
import goPlayer.*;

import java.awt.BorderLayout;

import uctMonteCarlo.*;


import java.awt.Insets;
import java.io.FileOutputStream;
import java.io.IOException;
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
		JButton cvrButton = new JButton("BasVsRan");		
		new SelectPlayMode(this, PlayMode.COMPVSRANDOM, cvrButton);
		buttons.add(cvrButton);
		

		JButton avuButton = new JButton("AmfVsBas");
		new SelectPlayMode(this, PlayMode.AMAFVSUCT , avuButton);
		buttons.add(avuButton);
		
		JButton pvcButton = new JButton("PlyrVsBas");
		new SelectPlayMode(this, PlayMode.PLAYERVSBASIC, pvcButton);
		buttons.add(pvcButton);
		
		JButton pvbButton = new JButton("PreVSBas");
		new SelectPlayMode(this, PlayMode.PRELOADEDVSBASIC,pvbButton);
		buttons.add(pvbButton);
		
		JButton pvaButton = new JButton("PlyrVsAMAF");
		new SelectPlayMode(this, PlayMode.PLAYERVSAMAF, pvaButton);
		buttons.add(pvaButton);
		
		JButton pvpButton = new JButton("PlyrVsPlyr");
		new SelectPlayMode(this, PlayMode.PLAYERVSPLAYER, pvpButton);
		buttons.add(pvpButton);
		
		JButton evbButton = new JButton("EvalBrd");
		new SelectPlayMode(this, PlayMode.EVALBOARD, evbButton);
		buttons.add(evbButton);
		
		JButton stopButton = new JButton("Stop");
		new SelectPlayMode(this, PlayMode.NOTPLAYING, stopButton);
		buttons.add(stopButton);
		
		JButton rvuButton = new JButton("RavVsBas");
		new SelectPlayMode(this, PlayMode.RAVEVSUCT, rvuButton);
		buttons.add(rvuButton);
		
		JButton ranButton = new JButton("playout");
		new SelectPlayMode(this, PlayMode.RANDOMPLAYOUTS,ranButton);
		buttons.add(ranButton);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setMargin(new Insets(10,10,10,10));
		toolBar.add(cvrButton);
		toolBar.add(avuButton);
		toolBar.add(rvuButton);
		toolBar.add(pvbButton);
		toolBar.add(pvcButton);
		toolBar.add(pvaButton);
		toolBar.add(pvpButton);
		toolBar.add(evbButton);
		toolBar.add(ranButton);
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
	
	
	public static void main(String[] args) throws IOException
	{	
		GoBoard board = new GoBoard();
		/*LinkedList<Child> children = new LinkedList<Child>();
		TreeNode treeHead = new LinkedListTreeNode(children, Colour.BLACK, null,0);
		UCTSearchBasic treeCreator = new UCTSearchBasic(treeHead, board, UpdateType.RAVE);
		treeCreator.saveTree();*/
		GoGo goPlayer = new GoGo(board);
		goPlayer.setLocation(100, 100);
		goPlayer.setSize(675,675);
		goPlayer.setVisible(true);
		
		
		
		
		
	}

	private void compVsRandom() {
		int games = 0,moves=0,wins=0,passes=0,realMoves=0;
		while(games<10&&playMode==PlayMode.COMPVSRANDOM){
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
		whitePlayer.printProfiling();
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
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 5000, 1000, Colour.WHITE);
					break;
			case AMAFVSUCT: goBoard.reset(); 
							blackPlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 1000, Colour.BLACK);
							whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.AMAF), (long) 500, 1000, Colour.WHITE);
							compVsComp(150);
					break;
			case RAVEVSUCT: goBoard.reset(); 
							blackPlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 1000, Colour.BLACK);
							whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.RAVE), (long) 500, 1000, Colour.WHITE);
							compVsComp(150);
					break;
			case NOTPLAYING: goBoard.reset();
					break;
			case EVALBOARD: System.out.println(goBoard.scoreBoard()); goBoard.reset();
					break;			
			case PLAYERVSPLAYER: goBoard.reset();
					break;
			case PRELOADEDVSBASIC: goBoard.reset();
									blackPlayer = new UctPlayer(new UCTSearchBasic(treeHead, goBoard,UpdateType.BASIC), (long) 500, 900, Colour.BLACK);
									whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.BASIC), (long) 500, 900, Colour.WHITE);
									whitePlayer.loadTree();
									compVsComp(300);
									break;
			case PLAYERVSAMAF: goBoard.reset();
					whitePlayer = new UctPlayer(new UCTSearchBasic(treeHead1, goBoard,UpdateType.AMAF), (long) 5000, 900, Colour.WHITE);
					break;
			case RANDOMPLAYOUTS: whitePlayer = new RandomPlayer(); blackPlayer = new RandomPlayer();
					randomPlayouts();
					break;			
		default:
			break;
		}
		
	}

	private void randomPlayouts() {
		int games = 0;
		long avgTime = 0; 
		while(games<500){
			long start = System.currentTimeMillis();
			goBoard.randomPlayout(Colour.BLACK);
			goBoard.scoreBoard();
			start = System.currentTimeMillis()-start;
			avgTime = (avgTime*games+start)/(games+1);
			
			games++;
			goBoard.reset();
			
		}
		System.out.println("average playout time - " + avgTime);
	}



	private void compVsComp(int tGames) {
		int games = 0,moves=0,whiteWins=0,passes=0,blackWins=0;
		while(games<tGames&&(playMode==PlayMode.AMAFVSUCT||playMode==PlayMode.RAVEVSUCT||playMode==PlayMode.PRELOADEDVSBASIC)){
			while(moves<350&&!goBoard.isFull()){
				int blackMove = blackPlayer.findMoveIter();
				if(goBoard.put(Colour.BLACK,blackMove)){	
					//System.out.println("black - " + blackMove);
					whitePlayer.notifyOpponentsMove(blackMove);
					int whiteMove = whitePlayer.findMoveIter();
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
			whitePlayer.resetSearcher();
			blackPlayer.resetSearcher();
			if(playMode==PlayMode.AMAFVSUCT)System.out.print("AMAF-w BASIC-b");
			else if(playMode==PlayMode.RAVEVSUCT) System.out.print("RAVE-w BASIC-b");
			else if (playMode==PlayMode.PRELOADEDVSBASIC){
				System.out.print("PRE - w BASIC - b ");
				whitePlayer.loadTree();
			}
			System.out.print("white - " +whiteWins);
			System.out.print("   black - " +blackWins);
			System.out.println("   games - " + games);
			goBoard.reset();
			moves = 0;
			passes = 0;
		}

		whitePlayer.printProfiling();
		System.out.println(whiteWins);
	}
}
