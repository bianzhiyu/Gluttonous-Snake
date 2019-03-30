package gluttonoussnake;

import java.awt.Color;

public class Food
{
	public static final Color FOOD_COLOR=new Color(13,205,7);
	private int X,Y;
	public Food()
	{
		X=0;Y=0;
	}
	public Food(int []d)
	{
		X=d[0];Y=d[1];
	}
	public int getX(){return X;}
	public int getY(){return Y;}
	public static int[] generateFood(Snake snake)
	{
		boolean [][]fgmap=new boolean[12][12];
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				fgmap[i][j]=false;
			}
		}
		int [][]location=snake.getLocation();
		for (int i=0;i<snake.getLength();i++)
		{
			fgmap[location[i][0]][location[i][1]]=true;
		}
		int v=(int)(Math.random()*(12*12-snake.getLength()));
		v=v%(12*12-snake.getLength());
		int x=0,y=0;
		for (int i=0;i<v;i++)
		{
			y+=1;
			if (y==12)
			{
				y=0;
				x+=1;
			}
			while (fgmap[x][y])
			{
				y=y+1;
				if (y==12)
				{
					y=0;
					x+=1;
				}
			}
		}
		int []ans={x,y};
		return ans;
	}
}
