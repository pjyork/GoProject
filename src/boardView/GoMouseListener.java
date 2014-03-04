package boardView;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GoMouseListener implements MouseListener {
	private BoardView view;
	
	public GoMouseListener(BoardView bView){
		view = bView;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		view.place(e.getPoint());

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
