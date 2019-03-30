package gluttonoussnake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AL_Pause implements ActionListener
{
	private Window wd;
	
	AL_Pause(Window w)
	{
		this.wd=w;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		wd.setPause(!wd.isPaused());
		wd.matchTextOnPauseWithPaused();
	}

}
