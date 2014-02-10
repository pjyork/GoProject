package boardView;

import goPlayer.GoPlayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import boardRep.Colour;

import boardRep.GoBoard;

public class BoardView extends JPanel{
	protected final JFrame goPlayer;
	private final int size = 19; //determines the dimensions of the board
	protected final GoBoard goBoard;
	private final int border = 20;
	
	public BoardView(GoPlayer goPlayer,GoBoard goBoard){
		this.goPlayer=goPlayer;
		this.goBoard=goBoard;
	}
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g; 
		
		
		// The state of the Graphics object should stay the same after the painting is finished.
		Color oldColor = g2d.getColor();
		Shape oldClip = g2d.getClip();
		//set the background
		g2d.setColor(Color.BLACK);

		int height = getHeight();
		int width = getWidth();
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.GREEN);
		g2d.drawRect(border, border, width-(2*border), height-(2*border));
		int horistep = (width - (2*border))/18;
		int vertstep = (height - (2*border))/18;
		
		for(int i = 1;i<18;i++){
			g2d.drawLine(border+(horistep*i), border, border+(horistep*i), height-border);
			g2d.drawLine(border, border+(vertstep*i), width-border, border+(vertstep*i));
		}	
		
		for(int i = 0;i<19;i++){
			for(int j = 0;j<19;j++){
				int pos = 21 + i + j*20;
				Colour c = goBoard.getColour(pos);
				int horioffset = horistep/4;
				int vertoffset = vertstep/4;
				if(c==Colour.WHITE){
					g2d.setColor(Color.WHITE);
					g2d.drawOval(border+(horistep*i)-horioffset, border+(vertstep*j)-vertoffset, horistep/2, vertstep/2);
					g2d.fillOval(border+(horistep*i)-horioffset, border+(vertstep*j)-vertoffset, horistep/2, vertstep/2);
				}
				else if(c==Colour.BLACK){
					g2d.setColor(Color.WHITE);
					g2d.drawOval(border+(horistep*i)-horioffset, border+(vertstep*j)-vertoffset, horistep/2, vertstep/2);
					g2d.setColor(Color.BLACK);
					g2d.fillOval(border+(horistep*i)-horioffset, border+(vertstep*j)-vertoffset, horistep/2, vertstep/2);
				}
			}
		}
		
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
	}
}
