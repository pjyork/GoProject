class stringsets{
   struct node{//a specific node in the disjoint set forest. contains  parent, value, and rank, colour (note that value and rank are only used if it is the root/representative of its tree)
	   //colour is 0 white, 1 grey, 2 black
	int value; node *parent; int rank; int colour;
   };
   node node[go_board_size]; // an array which contains the node, if it exists, for each position on the board
   



  //public functions
	public void makeset(int x,int sColour){//called with x as the board pos and sColour as the colour of the stone placed there. called whenever a move is made
		node xNode = {
			.value = x;
			.parent = null;
			.rank = 0;
			.colour = sColour;
		};
		xNode.parent = xNode;
		node[x]=xNode;
	}	
	public void  union(int x, int y){//used when a new stone is added to the board - it is unioned with the positions to the N,S,W,E
		node xRep = intFind(x);
		node yRep = intFind(y);
		if(xRep==yRep||xRep.colour!=yRep.colour||yRep==null){ return;}//if x and y are already in the same set, or their colour is different then nothing changes
		if(xRep.rank>yRep.rank){
			yRep.parent = xRep;
		}
		else{
			if(xRep.rank<yRep.rank){
				xRep.Parent = yRep;
			}
			else{
				yRoot.parent = xRoot;
				xRoot.rank++;
			}
		}
	}

	public int findRep(int x){//finds the value of the node's (at x on the board) representative. this is used in classification of strings in the program
		return intFind(x).value;
	}

	//private functions
	private node intFind(int x){ //given an int returns the corresponding node's representative. used by union
		node xNode = node[x];
		
		return nodeFind(xNode);
	}
	
	private node nodeFind(node xNode){
		if (xNode.parent!=xNode){
			xNode.parent = nodeFind(xNode);
		}
		return xNode.parent;
	}
	
