package gluttonoussnake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KL_Control implements KeyListener
{
	
	private Snake snake;
	private Window wd;
	public KL_Control(Window w,Snake s)
	{
		snake=s;
		wd=w;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		switch (arg0.getKeyCode())
		{
		case KeyEvent.VK_W: case KeyEvent.VK_UP:
			snake.setDirection(Direction.GO_UP);
			break;
		case KeyEvent.VK_S: case KeyEvent.VK_DOWN:
			snake.setDirection(Direction.GO_DOWN);
			break;
		case KeyEvent.VK_A: case KeyEvent.VK_LEFT:
			snake.setDirection(Direction.GO_LEFT);
			break;
		case KeyEvent.VK_D: case KeyEvent.VK_RIGHT:
			snake.setDirection(Direction.GO_RIGHT);
			break;
		case KeyEvent.VK_P:
			wd.setPause(!wd.isPaused());
			wd.matchTextOnPauseWithPaused();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {	}

	@Override
	public void keyTyped(KeyEvent arg0) {	}
	
}

