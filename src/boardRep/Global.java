package boardRep;
import boardRep.Colour;

public final class Global {
	public static int board_size = 13;
	public static int array_size = (Global.board_size+2)*(Global.board_size+1);
	public static Colour opponent(Colour c){
		Colour opponent = Colour.GREY;
		switch (c){
		case BLACK: opponent = Colour.WHITE;
					break;
		case WHITE: opponent = Colour.BLACK;
					break;
		default:
			break;	
		}
		return opponent;
	}
	public static String posString(int pos){
		String posString = "";
		if(pos>0&&pos<array_size){
			posString = "(" + String.valueOf(pos%(board_size+1))+","+ String.valueOf(pos/(board_size+1)) +")"  ;
		}
		return posString;
	}
}
