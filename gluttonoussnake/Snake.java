package gluttonoussnake;

import java.awt.Color;

public class Snake
{
	public final static int MAXLENGTH=12*12;
	public final static Color HEAD_COLOR=new Color(0xFF,0,0);
	public final static Color BODY_COLOR_1=new Color(205,91,7);
	public final static Color BODY_COLOR_2=new Color(45,29,184);
	
	public final static int STATUS_MOVE=0;
	public final static int STATUS_SELFEAT=1;
	public final static int STATUS_VICTORY=2;
	public final static int STATUS_GROWUP=3;
	
	private int Location[][];
	/**
	 * save the location of the snake, head to tail.
	 */
	private volatile int Length;
	private volatile int Direct;
	/**direct means where the snake is heading for:
	 * 0 means go up,
	 * 1 means go right,
	 * 2 means go down,
	 * 3 means go left
	 */
	
	public synchronized int getLength()
	{
		return Length;
	}
	public synchronized int[][] getLocation()
	{
		return Location.clone();
	}
	public synchronized int getDirect()
	{
		return Direct;
	}
	public synchronized void setDirection(int it)
	{
		Direct=it;
	}
	public Snake()
	{
		Length=3;
		Location=new int[MAXLENGTH][];
		for (int i=0;i<MAXLENGTH;i++)
		{
			Location[i]=new int[2];
		}
		Direct=((int)(Math.random()*4))%4;
		Location[0][0]=2+(int)(Math.random()*6);
		Location[0][1]=2+(int)(Math.random()*6);
		for (int i=1;i<Length;i++)
		{
			Location[i][0]=Location[i-1][0]-Direction.DELTA_DIRECT[Direct][0];
			Location[i][1]=Location[i-1][1]-Direction.DELTA_DIRECT[Direct][1];
		}
	}
	public synchronized void goOneStep()
	{
		for (int i=Length-1;i>=1;i--)
		{
			Location[i][0]=Location[i-1][0];
			Location[i][1]=Location[i-1][1];
		}
		int []ta=Direction.gextNextLocation(Location[0][0], Location[0][1], Direct);
		Location[0][0]=ta[0];
		Location[0][1]=ta[1];
	}
	public synchronized boolean isHeadinBody()
	{
		for (int i=1;i<Length;i++)
		{
			if ((Location[i][0]==Location[0][0])&&(Location[i][1]==Location[0][1]))
			{
				return true;
			}
		}
		return false;
	}
	public synchronized boolean isHeadOnFood(Food fd)
	{
		if ((fd.getX()==Location[0][0])&&(fd.getY()==Location[0][1]))
		{
			return true;
		}
		return false;
	}
	public synchronized boolean isGoBackward()
	{
		int []ta=Direction.gextNextLocation(Location[0][0], Location[0][1], Direct);
		if ((ta[0]==Location[1][0])&&(ta[1]==Location[1][1]))
		{
			return true;
		}
		return false;
	}
	public synchronized void invertDirect()
	{
		switch(Direct)
		{
		case Direction.GO_UP: Direct=Direction.GO_DOWN;break;
		case Direction.GO_DOWN: Direct=Direction.GO_UP;break;
		case Direction.GO_LEFT: Direct=Direction.GO_RIGHT;break;
		case Direction.GO_RIGHT: Direct=Direction.GO_LEFT;break;
		}
	}
	public synchronized int[] getNextLocation()
	{
		return Direction.gextNextLocation(Location[0][0], Location[0][1], Direct);
	}
	public synchronized boolean isLocationInBody(int lx,int ly)
	{
		for (int i=0;i<Length;i++)
		{
			if ((Location[i][0]==lx)&&Location[i][1]==ly)
			{
				return true;
			}
		}
		return false;
	}
	public synchronized boolean isLocationInBodyButTail(int lx,int ly)
	{
		for (int i=0;i<Length-1;i++)
		{
			if ((Location[i][0]==lx)&&Location[i][1]==ly)
			{
				return true;
			}
		}
		return false;
	}
	public synchronized void growUp()
	{
		int []newlocation=this.getNextLocation();
		for (int i=Length;i>0;i--)
		{
			Location[i][0]=Location[i-1][0];
			Location[i][1]=Location[i-1][1];
		}
		Length++;
		Location[0][0]=newlocation[0];
		Location[0][1]=newlocation[1];	
	}
	public synchronized int actThisStep(Food food)
	{
		if (this.isGoBackward())
		{
			this.invertDirect();
		}
		int []nextloca=this.getNextLocation();
		if (this.isLocationInBodyButTail(nextloca[0], nextloca[1]))
		{
			return Snake.STATUS_SELFEAT;
		}
		else if (this.getLength()==12*12)
		{
			return Snake.STATUS_VICTORY;
		}
		else if ((nextloca[0]==food.getX())&&(nextloca[1]==food.getY()))
		{
			this.growUp();
			return Snake.STATUS_GROWUP;
		}
		else
		{
			this.goOneStep();
			return Snake.STATUS_MOVE;
		}
	}
}
