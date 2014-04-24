package boardRep;

public class ArrayLiberties implements Liberties {
	private boolean[] liberties;
	public int num;
	
	public ArrayLiberties(){
		liberties = new boolean[Global.array_size];
		num=0;
	}
	
	@Override
	public Liberties union(Liberties that) {
		Liberties newLiberties = new ArrayLiberties();
		int newNum = 0;
		for(int i=0;i<liberties.length;i++){
			if(this.liberties[i]||that.contains(i)){
				newLiberties.add(i);
				newNum++;
			}
		}
		newLiberties.setNum(newNum);
		return newLiberties;
	}

	@Override
	public int size() {
		return num;
	}

	@Override
	public boolean empty() {
		return num==0;
	}

	@Override
	public boolean contains(int pos) {
		return liberties[pos];
	}

	@Override
	public void add(int pos) {
		if(!liberties[pos]){num++;}
		liberties[pos]=true;
	}

	@Override
	public void remove(int pos) {
		if(liberties[pos]){num--;}
		liberties[pos]=false;
	}

	@Override
	public void setNum(int newNum) {
		num=newNum;
	}
	
	@Override
	public ArrayLiberties clone(){
		ArrayLiberties newLiberties = new ArrayLiberties();
		newLiberties.liberties=this.liberties.clone();
		newLiberties.num=this.num;
		return newLiberties;
		
	}

}
