package goPlayer;
import java.awt.BorderLayout;
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

import boardRep.Colour;
import boardRep.GoBoard;
import boardView.BoardView;


public class GoPlayer extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final GoBoard goBoard;
	protected final BoardView boardView;
	
	public GoPlayer(GoBoard goBoard){
		this.goBoard = goBoard;
		this.boardView = new BoardView(this, goBoard);
		
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(BorderLayout.CENTER,boardView);
		contentPane.requestFocusInWindow();
		setContentPane(contentPane);
	}

	public static void main(String[] args)
	{
		GoBoard board = new GoBoard();
		GoPlayer goPlayer = new GoPlayer(board);
		goPlayer.setLocation(100, 100);
		goPlayer.setSize(675,675);
		goPlayer.setVisible(true);
		int i =0;
		
		while(!board.isFull()){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(i==1){
				if(board.put(Colour.WHITE, (int) Math.random())){
					i = ++i%2;
				}
			}
			else{
				if(board.put(Colour.BLACK, (int) Math.random())){
					i = ++i%2;
				}
			}
		}
	}
}
