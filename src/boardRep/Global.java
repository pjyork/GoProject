package boardRep;
import boardRep.Colour;

public class Global {
	public static int board_size = 19;
	public static int array_size = (Global.board_size+2)*(Global.board_size+1);
	public static Colour opponent(Colour c){
		Colour opponent = Colour.GREY;
		switch (c){
		case BLACK: opponent = Colour.WHITE;
					break;
		case WHITE: opponent = Colour.BLACK;
					break;	
		}
		return opponent;
	}
}
