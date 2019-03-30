package gluttonoussnake;

public class Direction
{
	final static int GO_UP=0,GO_RIGHT=1,GO_DOWN=2,GO_LEFT=3;
	final static int[][] DELTA_DIRECT={{-1,0},{0,1},{1,0},{0,-1}};
	final static String[] CHARACTER={" ^","->"," v","<-"};
	public static int[]gextNextLocation(int x,int y,int D)
	{
		int nx=x+DELTA_DIRECT[D][0],ny=y+DELTA_DIRECT[D][1];
		if (nx==-1) nx=11;
		if (nx==12) nx=0;
		if (ny==-1) ny=11;
		if (ny==12) ny=0;
		int [] ans={nx,ny};
		return ans;
	}
}