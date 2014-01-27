class go_board {
	private int board_size;
	private int max_strings;


	enum PosState{ grey, white, black, empty};// for a given board position is it black, white, empty or edge (grey)
	private PosState board[(board_size+2)*(board_size+1)];//we want a border of 'edge' positions so +2
												// using a 1D array is useful because using single dimensional coordinates
												// makes function calls etc. less expensive
	


	int board_ko_pos; //this stores where on the board a play will return the board to a previous board state
					  // if no such position exists it will be 0 (which is -1,-1) i.e. in the border
	
	private int up = board_size+1; //the amount to +- to go up or down on the board

	private stringsets blackStrings;
	private stringsets whiteStrings;
			
	
	//these are functions for determining 1D positions from a pos. NB should not be called on border positions
	private int n(int pos){return pos-up;}//return the 1D coord directly above pos
	private int s(int pos){return pos+up;}//return the 1D coord directly below pos
	private int w(int pos){return pos-1;}//return the 1D coord directly left of pos
	private int e(int pos){return pos+1;}//return the 1D coord directly right of pos
	private int nw(int pos){return pos-up-1;}//return the 1D coord above and to the left of pos
	private int ne(int pos){return pos-up+1;}//return the 1D coord above and to the right of pos
	private int sw(int pos){return pos+up-1;}//return the 1D coord below and to the left of pos
	private int se(int pos){return pos+up+1;}//return the 1D coord below and to the right of pos
	
	//public functions
	public void put(PosState colour, int pos){//this is used to put stones onto the board
		board[pos]=colour;
		if(colour==white){
			strings.makeset(pos,colour);
			strings.union(pos,n(pos));
			strings.union(pos,e(pos));
			strings.union(pos,s(pos));
			strings.union(pos,w(pos));
		}
	}


	private void classifyEyespace(){
		
	}

	public void updateStrings(){
		
	}

}
