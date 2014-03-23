package boardRep;

public class StringSet {
	
	
	
	   private class Node{//a specific node in the disjoint set forest. contains  parent, value, and rank, colour (note that value and rank are only used if it is the root/representative of its tree)
		   //colour is 0 white, 1 grey, 2 black
		  
		   
			int position; Node parent; int rank; Colour colour;Liberties liberties;
			public Node(int value, Node parent, int rank, Colour colour){
				this.position=value;this.parent=parent;this.rank=rank;this.colour=colour;
				this.liberties= new ArrayLiberties();
			}
	   };
	   Node[] nodes = new Node[Global.array_size]; // an array which contains the node, if it exists, for each position on the board
	   



	  //public functions
		public void makeset(int x,Colour sColour){//called with x as the board pos and sColour as the colour of the stone placed there. called whenever a move is made
			Node xNode = new Node(x,null,0,sColour);
			xNode.parent = xNode;
			nodes[x]=xNode;
		}	
		public void union(int x, int y){
			//used when a new stone is added to the board - it is unioned with the positions to the N,S,W,E
			if(x<Global.array_size&&y<Global.array_size&&y>=0){
				Node xRep = intFind(x);				
				Node yRep = intFind(y);
				if(xRep!=null&&yRep!=null){
					removeLiberty(x,yRep);
					if(xRep==yRep||xRep.colour!=yRep.colour||yRep==null){ return;}//if x and y are already in the same set, or their colour is different then nothing changes
					if(xRep.rank>yRep.rank){
						yRep.parent = xRep;
						xRep.liberties=xRep.liberties.union(yRep.liberties);
					}
					else{
						if(xRep.rank<yRep.rank){
							xRep.parent = yRep;
							yRep.liberties=xRep.liberties.union(yRep.liberties);
						}
						else{
							yRep.parent = xRep;
							xRep.rank = xRep.rank + 1;
							xRep.liberties=xRep.liberties.union(yRep.liberties);
						}
					}
				}
			}
		}

		private void removeLiberty(int x, Node yRep) {
			yRep.liberties.remove(x);
		}
		public int findRep(int x){//finds the value of the node's (at x on the board) representative. this is used in classification of strings in the program
			return intFind(x).position;
		}
		public boolean noLiberties(int pos){
			Node xNode = intFind(pos);
			if(xNode!=null){return intFind(pos).liberties.empty();}
		    return false;
		}

		//private functions
		private Node intFind(int x){ //given an int returns the corresponding node's representative. used by union
			Node xNode = nodes[x];
			
			return nodeFind(xNode);
		}
		
		private Node nodeFind(Node xNode){
			if(xNode!=null){
				if (xNode.parent!=xNode){
					xNode.parent = nodeFind(xNode.parent);
				}
				return xNode.parent;
			}
			else{
				return xNode;
			}
	
		}
		public Liberties getLiberties(int x){
			Node xNode = intFind(x);
			if(xNode!=null) return xNode.liberties;
			return null;
		}
		public void remove(int pos) {
			nodes[pos]=null;
		}
		public void addLiberty(int x, int lib) {
			Node xNode = intFind(x);
			if(xNode!=null){xNode.liberties.add(lib);}
		}
}
