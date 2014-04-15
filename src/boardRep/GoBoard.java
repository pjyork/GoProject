package boardRep;

import java.util.LinkedList;

public class GoBoard {
	private int board_size=Global.board_size;
	private int max_strings;

//	enum PosState{ grey, white, black, empty};// for a given board position is it black, white, empty or edge (grey)
	private Colour board[] = new Colour[Global.array_size];//we want a border of 'edge' positions so +2
												// using a 1D array is useful because using single dimensional coordinates
												// makes function calls etc. less expensive
	
	private Colour territory[] = new Colour[Global.array_size];
	
	
	private int stoneNum;
	private LinkedList<BoardListener> listeners;

	private int board_ko_pos; //this stores where on the board a play will return the board to a previous board state
					  // if no such position exists it will be 0 (which is -1,-1) i.e. in the border
	
	private int up = board_size+1; //the amount to +- to go up or down on the board

	private StringSet strings;
			
	
	//these are functions for determining 1D positions from a pos. NB should not be called on border positions
	private int n(int pos){return pos-up;}//return the 1D coord directly above pos
	private int s(int pos){return (pos+up)%Global.array_size;}//return the 1D coord directly below pos
	private int w(int pos){return pos-1;}//return the 1D coord directly left of pos
	private int e(int pos){return (pos+1)%Global.array_size;}//return the 1D coord directly right of pos
	private int nw(int pos){return pos-up-1;}//return the 1D coord above and to the left of pos
	private int ne(int pos){return pos-up+1;}//return the 1D coord above and to the right of pos
	private int sw(int pos){return pos+up-1;}//return the 1D coord below and to the left of pos
	private int se(int pos){return pos+up+1;}//return the 1D coord below and to the right of pos
	
	
	public GoBoard(){
		for(int i = 0;i<Global.array_size;i++){
			if(i%(Global.board_size+1)==0||i<Global.board_size+1||i>Global.board_size*(Global.board_size+2)){
				board[i]=Colour.GREY;
			}
			else if(i%2==0){
				board[i]=Colour.EMPTY;
			}
			else{
				board[i]=Colour.EMPTY;
			}
			territory[i]=Colour.EMPTY;
		}
		this.listeners = new LinkedList<BoardListener>();
		this.strings = new StringSet();
		this.stoneNum=0;
		
	}
	
	
	//public functions
	public boolean put(Colour colour, int pos){//this is used to put stones onto the board
		if(pos<Global.array_size&&pos>Global.board_size){
			if(stoneNum<19*19&&board[pos]==Colour.EMPTY){
					if(check(pos, colour)){
						board[pos]=colour;
						strings.makeset(pos,colour);
						
						if(board[n(pos)]==Colour.EMPTY){
							strings.addLiberty(pos,n(pos));
						}
						else{
							strings.union(pos,n(pos));
						}
						
						if(board[e(pos)]==Colour.EMPTY){
							strings.addLiberty(pos,e(pos));
						}
						else{
							strings.union(pos,e(pos));
						}
						
						if(board[s(pos)]==Colour.EMPTY){
							strings.addLiberty(pos,s(pos));
						}
						else{
							strings.union(pos,s(pos));
						}	
						
						if(board[w(pos)]==Colour.EMPTY){
							strings.addLiberty(pos,w(pos));
						}
						else{
							strings.union(pos,w(pos));
						}
						int rN=0;
						int rE=0;
						int rS=0;
						int rW=0;
				
						if(strings.noLiberties(n(pos))){rN=removeString(n(pos));}
						if(strings.noLiberties(e(pos))){rE=removeString(e(pos));}
						if(strings.noLiberties(s(pos))){rS=removeString(s(pos));}
						if(strings.noLiberties(w(pos))){rW=removeString(w(pos));}
						
						if(rN+rE+rS+rW==1){
							if(rN==1) board_ko_pos =n(pos);
							else if(rE==1) board_ko_pos =e(pos);
							else if(rS==1) board_ko_pos =s(pos);
							else board_ko_pos =w(pos);
						}
						else board_ko_pos =0;
						stoneNum++;
						notifyListeners();
						return true;
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}
		return false;
	}

	public boolean check(int pos,Colour c) {
		if(pos==board_ko_pos) return false;
		if(board[n(pos)]==Colour.EMPTY||board[e(pos)]==Colour.EMPTY||board[s(pos)]==Colour.EMPTY||board[w(pos)]==Colour.EMPTY) return true;
		boolean hasNeighbourWithLiberties=false;boolean capturesNeighbour = false;
		if(board[n(pos)]!=Colour.GREY){
			hasNeighbourWithLiberties = board[n(pos)]==c&&strings.getLiberties(n(pos)).size()>1;
			capturesNeighbour = board[n(pos)]!=c&&strings.getLiberties(n(pos)).size()==1;
		}
		if(board[e(pos)]!=Colour.GREY){
			hasNeighbourWithLiberties = hasNeighbourWithLiberties || board[e(pos)]==c&&strings.getLiberties(e(pos)).size()>1;
			capturesNeighbour = capturesNeighbour||board[e(pos)]!=c&&strings.getLiberties(e(pos)).size()==1;
		}	
		if(board[s(pos)]!=Colour.GREY){
			hasNeighbourWithLiberties = hasNeighbourWithLiberties || board[s(pos)]==c&&strings.getLiberties(s(pos)).size()>1;
			capturesNeighbour = capturesNeighbour||board[s(pos)]!=c&&strings.getLiberties(s(pos)).size()==1;
		}
		if(board[w(pos)]!=Colour.GREY){
			hasNeighbourWithLiberties = hasNeighbourWithLiberties || board[w(pos)]==c&&strings.getLiberties(w(pos)).size()>1;
			capturesNeighbour = capturesNeighbour||board[w(pos)]!=c&&strings.getLiberties(w(pos)).size()==1;
		}
		return capturesNeighbour||hasNeighbourWithLiberties;
	}
	private int removeString(int pos) {
		//removes the string at pos
		Colour colour = board[pos];
		int removed = 1;
		board[pos]=Colour.EMPTY;
		stoneNum--;
		strings.remove(pos);
		if(n(pos)<Global.array_size&&n(pos)>=0){
			if(board[n(pos)]==colour){removed+=removeString(n(pos));}
			else{strings.addLiberty(n(pos), pos);}
		}
		if(e(pos)<Global.array_size&&e(pos)>=0){
			if(board[e(pos)]==colour){removed+=removeString(e(pos));}
			else{strings.addLiberty(e(pos), pos);}
		}
		if(s(pos)<Global.array_size&&s(pos)>=0){
			if(board[s(pos)]==colour){removed+=removeString(s(pos));}
			else{strings.addLiberty(s(pos), pos);}
		}
		if(w(pos)<Global.array_size&&w(pos)>=0){
			if(board[w(pos)]==colour){removed+=removeString(w(pos));}
			else{strings.addLiberty(w(pos), pos);}
		}
		return removed;
	}
	
	
	public boolean isFull(){
		return stoneNum>=19*19;
	}
	private void classifyEyespace(){//working out which areas are eyespaces and who they belong to
		
	}
	public Colour getColour(int pos){
		return board[pos];
	}
	public void addListener(BoardListener bl) {
		listeners.add(bl);
	}
	private void notifyListeners(){
		for(BoardListener l:listeners){
			l.boardChanged();
		}
	}
	
	private void gameFinished(){
		for(int i = 0;i<Global.array_size;i++){
			territory[i]=board[i];
		}
	}
	
	private Colour territory(int pos){
		if(territory[pos]==Colour.EMPTY){
			Colour n,e,s,w;
			n = territory[n(pos)];
			e = territory[e(pos)];
			s = territory[s(pos)];
			w = territory[w(pos)];
			territory[pos]=Colour.GREY;
			if(n==Colour.EMPTY) n=territoryRec(n(pos),0);
			if(e==Colour.EMPTY) e=territoryRec(e(pos),1);
			if(s==Colour.EMPTY) s=territoryRec(s(pos),2);
			if(w==Colour.EMPTY) w=territoryRec(w(pos),3);
			
			if(n==e&&e==s&&s==w){
				territory[pos]=n;
				return n;
			}
		}	
		territory[pos] = Colour.GREY;
		return Colour.GREY;
	}
	
	
	private Colour territoryRec(int pos,int dir){
		//recursive call of territory. includes a direction so you don't backtrack to empty space
		//0 has come from south, 1 from west, 2 from north, 3 from east
		if(territory[pos]!=Colour.GREY){
			Colour n,e,s,w;
			n = territory[n(pos)];
			e = territory[e(pos)];
			s = territory[s(pos)];
			w = territory[w(pos)];
			if(n==Colour.EMPTY&&dir!=2) n=territory(n(pos));
			if(e==Colour.EMPTY&&dir!=3) e=territory(e(pos));
			if(s==Colour.EMPTY&&dir!=0) s=territory(s(pos));
			if(w==Colour.EMPTY&&dir!=1) w=territory(w(pos));
			
			switch (dir) {
	        case 0:  s=n;
	                 break;
	        case 1:  w=n;
	                 break;
	        case 2:  n=s;
	                 break;
	        case 3:  e=n;
	                 break;
	        default: break;
	    }
			
			if(n==e&&e==s&&s==w){
				return n;
			}	
		}
		return Colour.GREY;
	}
	public Colour scoreBoard(){
		int w = 0;
		int b = 0;
		gameFinished();
		for(int i = 0;i<Global.array_size;i++){
			if(territory[i]!=Colour.GREY){
				if(territory[i]==Colour.WHITE)w++;
				else if(territory[i]==Colour.BLACK)b++;
				else {//territory[i] is empty
					Colour t = territory(i);
					if(t==Colour.WHITE) w++;
					else if(t==Colour.BLACK)b++;
				}
			}
		}
		if(w>b) return Colour.WHITE;
		else if(w==b) return Colour.GREY;
		else return Colour.BLACK;
		
	}
	
	public GoBoard clone(){
		GoBoard newBoard = new GoBoard();
		newBoard.stoneNum = this.stoneNum;
		newBoard.board_ko_pos = this.board_ko_pos;
		newBoard.strings = this.strings.clone();
		return newBoard;
		
	}
	public Colour randomPlayout(Colour whoseTurn) {
		int i = 0;
		while(i<1000&&!isFull()){
			if(put(whoseTurn, (int) (Math.random()*(Global.array_size)))){
				whoseTurn = Global.opponent(whoseTurn);
			}
			i++;
		}
		return scoreBoard();		
	}
}
