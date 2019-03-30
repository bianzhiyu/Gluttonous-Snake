package gluttonoussnake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AL_Start implements ActionListener
{
	private Window wd;
	public AL_Start(Window w)
	{
		wd=w;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (wd.isRunning()) return;
		
		Snake snake=new Snake();
		wd.initializeRunningStatus();
		
		KL_Control keyinput=new KL_Control(wd,snake);
		wd.addKeyListener(keyinput);
		
		Thread t1=new Thread(new Thrd_updateMap(wd,snake));
		t1.start();
	}
}
