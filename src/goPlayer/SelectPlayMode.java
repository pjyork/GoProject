package goPlayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

public class SelectPlayMode implements ActionListener{
	private PlayMode playMode;
	private GoPlayer goPlayer;

	public SelectPlayMode(GoPlayer goPlayer, PlayMode playMode, AbstractButton button){
		this.goPlayer = goPlayer;
		this.playMode = playMode;
		button.addActionListener(this);

	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(goPlayer!=null){
			goPlayer.play(playMode);
		}
	}

}
