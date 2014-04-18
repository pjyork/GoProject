package boardRep;

public interface Liberties {
	public void setNum(int newNum);
	public Liberties union (Liberties that);
	public int size();
	public boolean empty();
	public boolean contains(int pos);
	public void add(int pos);
	public void remove(int pos);
	public Liberties clone();
}
