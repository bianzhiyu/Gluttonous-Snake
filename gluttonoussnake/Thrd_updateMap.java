package gluttonoussnake;

public class Thrd_updateMap implements Runnable
{
	private Window wd;
	private Snake snake;
	private final static int updateTime=200;
	public Thrd_updateMap(Window r,Snake s)
	{
		wd=r;
		snake=s;
	}
	@Override
	public void run() {
		Food food=new Food(Food.generateFood(snake));
		boolean isAlive=true;
		while(isAlive)
		{
			while (isAlive &&(!wd.isRunning() || wd.isPaused()))
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wd.paintMapWhite();
			wd.setScores(snake.getLength());
			try {
				wd.showSnake(snake);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			wd.showFood(food);
			try {
				Thread.sleep(updateTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			switch (snake.actThisStep(food))
			{
			case Snake.STATUS_SELFEAT:
				wd.setStopStatus();
				isAlive=false;
				wd.showGameOverDialog_SelfEat(snake.getLength());
				break;
			case Snake.STATUS_VICTORY:
				wd.setStopStatus();
				isAlive=false;
				wd.showVictoryDailog();
				break;
			case Snake.STATUS_GROWUP:
				food=new Food(Food.generateFood(snake));
				break;
			case Snake.STATUS_MOVE:
				break;
			}
		}
		wd.disposeGameSource();
		wd.setPause(false);
		wd.matchTextOnPauseWithPaused();
		//wd.paintMapWhite();
	}
	
}
