package boardRep;

import java.util.LinkedList;

public class GoBoard {
	private int board_size=Public.board_size;
	private int max_strings;

//	enum PosState{ grey, white, black, empty};// for a given board position is it black, white, empty or edge (grey)
	private Colour board[] = new Colour[Public.array_size];//we want a border of 'edge' positions so +2
												// using a 1D array is useful because using single dimensional coordinates
												// makes function calls etc. less expensive
	private int stoneNum;
	private LinkedList<BoardListener> listeners;

	int board_ko_pos; //this stores where on the board a play will return the board to a previous board state
					  // if no such position exists it will be 0 (which is -1,-1) i.e. in the border
	
	private int up = board_size+1; //the amount to +- to go up or down on the board

	private StringSet strings;
			
	
	//these are functions for determining 1D positions from a pos. NB should not be called on border positions
	private int n(int pos){return pos-up;}//return the 1D coord directly above pos
	private int s(int pos){return (pos+up)%Public.array_size;}//return the 1D coord directly below pos
	private int w(int pos){return pos-1;}//return the 1D coord directly left of pos
	private int e(int pos){return (pos+1)%Public.array_size;}//return the 1D coord directly right of pos
	private int nw(int pos){return pos-up-1;}//return the 1D coord above and to the left of pos
	private int ne(int pos){return pos-up+1;}//return the 1D coord above and to the right of pos
	private int sw(int pos){return pos+up-1;}//return the 1D coord below and to the left of pos
	private int se(int pos){return pos+up+1;}//return the 1D coord below and to the right of pos
	
	
	public GoBoard(){
		for(int i = 0;i<Public.array_size;i++){
			if(i%(Public.board_size+1)==0||i<Public.board_size+1){
				board[i]=Colour.GREY;
			}
			else if(i%2==0){
				board[i]=Colour.EMPTY;
			}
			else{
				board[i]=Colour.EMPTY;
			}
		}
		this.listeners = new LinkedList<BoardListener>();
		this.strings = new StringSet();
		this.stoneNum=0;
		
	}
	
	
	//public functions
	public boolean put(Colour colour, int pos){//this is used to put stones onto the board
		if(pos<Public.array_size&&pos>Public.board_size){
			if(stoneNum<19*19&&board[pos]==Colour.EMPTY){
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
				
				if(strings.noLiberties(n(pos))){removeString(n(pos));}
				if(strings.noLiberties(e(pos))){removeString(e(pos));}
				if(strings.noLiberties(s(pos))){removeString(s(pos));}
				if(strings.noLiberties(w(pos))){removeString(w(pos));}
				
				stoneNum++;
				notifyListeners();
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	private void removeString(int pos) {
		//removes the string at pos
		Colour colour = board[pos];
		board[pos]=Colour.EMPTY;
		stoneNum--;
		strings.remove(pos);
		if(n(pos)<Public.array_size&&n(pos)>=0){
			if(board[n(pos)]==colour){removeString(n(pos));}
			else{strings.addLiberty(n(pos), pos);}
		}
		if(e(pos)<Public.array_size&&e(pos)>=0){
			if(board[e(pos)]==colour){removeString(e(pos));}
			else{strings.addLiberty(e(pos), pos);}
		}
		if(s(pos)<Public.array_size&&s(pos)>=0){
			if(board[s(pos)]==colour){removeString(s(pos));}
			else{strings.addLiberty(s(pos), pos);}
		}
		if(w(pos)<Public.array_size&&w(pos)>=0){
			if(board[w(pos)]==colour){removeString(w(pos));}
			else{strings.addLiberty(w(pos), pos);}
		}
	}
	
	
	public boolean isFull(){
		return stoneNum==19*19;
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
}
