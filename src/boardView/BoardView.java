package boardView;

import goGo.GoGo;
import goGo.PlayMode;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;

import javax.swing.JFrame;
import javax.swing.JPanel;
import boardRep.Colour;
import boardRep.Global;

import boardRep.GoBoard;

public class BoardView extends JPanel{
	protected final JFrame goGo;
	private final int size = Global.board_size; //determines the dimensions of the board
	protected final GoBoard goBoard;
	private final int border = 20;
	private GoMouseListener mouse;
	private int colour = 0;
	
	public BoardView(GoGo goPlayer,GoBoard goBoard){
		
		this.goGo=goPlayer;
		this.goBoard=goBoard;
		mouse = new GoMouseListener(this);
		addMouseListener(mouse);
	}
	
	public void place(Point pos){
		int arrayPos = getArrayPos(pos);	
		if(((GoGo) goGo).playMode == PlayMode.PLAYERVSBASIC||((GoGo) goGo).playMode == PlayMode.PLAYERVSAMAF){
				
			
			
			if(goBoard.put(Colour.BLACK, arrayPos)){
				this.update();
				repaint();
				((GoGo) goGo).updateSearcher(arrayPos,Colour.BLACK);
				((GoGo) goGo).computerPlay();
			}
		}
		else if(((GoGo) goGo).playMode == PlayMode.PLAYERVSPLAYER){
			if(colour==0){	
				if(goBoard.put(Colour.BLACK,arrayPos))colour = ++colour%2;			
			}
			else{
				if(goBoard.put(Colour.WHITE,arrayPos))colour = ++colour%2;
				
			}
		}
	}
	
	private int getArrayPos(Point pos) {
		int height = getHeight();
		int width = getWidth();
		int x = pos.x - border;
		int y = pos.y - border;
		int horistep = (width - (2*border))/(size-1);
		int vertstep = (height - (2*border))/(size-1);
		x+=horistep/2;
		y+=vertstep/2;
		x = x/horistep;
		y = y/vertstep;
		int arrayPos = (Global.board_size+2) + x + y*(Global.board_size+1);
		return arrayPos;
	}

	public void update(){
		repaint();
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
		int horistep = (width - (2*border))/(Global.board_size-1);
		int vertstep = (height - (2*border))/(Global.board_size-1);
		
		for(int i = 1;i<18;i++){
			g2d.drawLine(border+(horistep*i), border, border+(horistep*i), height-border);
			g2d.drawLine(border, border+(vertstep*i), width-border, border+(vertstep*i));
		}	
		
		for(int i = 0;i<Global.board_size;i++){
			for(int j = 0;j<Global.board_size;j++){
				int pos = Global.board_size+2 + i + j*(Global.board_size+1);
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
					g2d.setColor(Color.RED);
					g2d.fillOval(border+(horistep*i)-horioffset, border+(vertstep*j)-vertoffset, horistep/2, vertstep/2);
				}
			}
		}
		
		g2d.setColor(oldColor);
		g2d.setClip(oldClip);
	}
}
