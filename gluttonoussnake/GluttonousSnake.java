package gluttonoussnake;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


class Window
{
	public JFrame Root;
	private JLabel Map[][],Scores;
	private JButton Bn_Start,Bn_Pause;
	private JTextArea Explanation;
	private boolean Running,Pause;
	//running is the flag to show whether game is running.
	
	final static int MAXROW=12;
	final static int MAXCOL=12;
	
	public synchronized void initializeRunningStatus()
	{
		Running=true;
		Pause=false;
		this.paintMapWhite();
		this.matchTextOnPauseWithPaused();
	}
	public synchronized boolean isRunning()
	{
		return Running;
	}
	public synchronized void setRunning(boolean f)
	{
		Running=f;
	}
	public synchronized void setStopStatus()
	{
		Running=false;
		Pause=false;
	}
	public synchronized boolean isPaused()
	{
		return Pause;
	}
	public synchronized void setPause(boolean b)
	{
		Pause=b;
	}
	public synchronized void matchTextOnPauseWithPaused()
	{
		if (!Pause)
		{
			Bn_Pause.setText("Pause");
		}
		else
		{
			Bn_Pause.setText("Resume");
		}
	}
	public void addKeyListener(KeyListener ad)
	{
		Root.addKeyListener(ad);
		Bn_Start.addKeyListener(ad);
		Bn_Pause.addKeyListener(ad);
		Explanation.addKeyListener(ad);
	}
	public Window()
	{
		Running=false;
		Pause=false;
		
		Root=new JFrame("Ã∞≥‘…ﬂ 0.03");//"Gluttonous Snake"
		Root.setLayout(null);
		Root.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container Fct=Root.getContentPane();
		
		Map=new JLabel[12][];
		Font ft=new Font("Arial", Font.BOLD, 25);
		for (int i=0;i<MAXROW;i++)
		{
			Map[i]=new JLabel[12];
			for (int j=0;j<MAXCOL;j++)
			{
				Map[i][j]=new JLabel("  ");
				Map[i][j].setBounds(10+j*40, 10+i*40, 40, 40);
				Map[i][j].setBorder(BorderFactory.createEmptyBorder());
				Map[i][j].setOpaque(true);
				Map[i][j].setFont(ft);
				Fct.add(Map[i][j]);
			}
		}
		
		Bn_Start=new JButton("Start");
		Bn_Start.setFont(ft);
		Bn_Start.setBounds(10,500,100,40);
		Bn_Start.addActionListener(new AL_Start(this));
				
		Bn_Pause=new JButton("Pause");
		Bn_Pause.setFont(ft);
		Bn_Pause.setBounds(130,500,140,40);
		Bn_Pause.addActionListener(new AL_Pause(this));
		
		Scores=new JLabel("Your Score");
		Scores.setBounds(300,500,190,40);
		Scores.setFont(ft);
		
		Fct.add(Bn_Start);
		Fct.add(Bn_Pause);
		Fct.add(Scores);
		
		Explanation=new JTextArea("Press Start to run.\n"
				+ "Press arrow keys to control \ndirection the snakes move towords.\n"
				+ "You can also use WSAD to move,\n"
				+ "Press Key P or Pause to pause.");
		Explanation.setFont(ft);
		Explanation.setBounds(10,550,480,160);
		Explanation.setOpaque(true);
		Explanation.setEditable(false);
		
		Fct.add(Explanation);
		
		Root.setSize(530, 810);
	}
	public void show()
	{
		Root.setVisible(true);
	}
	public synchronized void paintMapWhite()
	{
		for (int i=0;i<12;i++)
		{
			for (int j=0;j<12;j++)
			{
				Map[i][j].setBackground(Color.WHITE);
				Map[i][j].setText("  ");
			}
		}
	}
	public synchronized void showSnake(Snake snake) throws Exception
	{
		if (snake.getLength()>=Snake.MAXLENGTH)
		{
			throw(new Exception("Snake-Length Limit Exceeded."));
		}
		int[][] location=snake.getLocation();
		
		//paintMapWhite();
		Map[location[0][0]][location[0][1]].setBackground(Snake.HEAD_COLOR);
		Map[location[0][0]][location[0][1]].setText(Direction.CHARACTER[snake.getDirect()]);
		double tl=(double)snake.getLength()-1;
		int d1=Snake.BODY_COLOR_2.getRed()-Snake.BODY_COLOR_1.getRed();
		int d2=Snake.BODY_COLOR_2.getGreen()-Snake.BODY_COLOR_1.getGreen();
		int d3=Snake.BODY_COLOR_2.getBlue()-Snake.BODY_COLOR_1.getBlue();
		int[] k={(int)(d1/tl),(int)(d2/tl),(int)(d3/tl)};
		int[] b={Snake.BODY_COLOR_1.getRed(),
				Snake.BODY_COLOR_1.getGreen(),
				Snake.BODY_COLOR_1.getBlue()};
		for (int i=1;i<snake.getLength();i++)
		{
			int[] y={k[0]*(i-1)+b[0],k[1]*(i-1)+b[1],k[2]*(i-1)+b[2]};
			for (int j=0;j<3;j++)
			{
				if (y[j]<0) y[j]=0;
				else if (y[j]>255) y[j]=255;
			}
			Color cr=new Color(y[0],y[1],y[2]);
			Map[location[i][0]][location[i][1]].setBackground(cr);
		}
	}
	public synchronized void setScores(int s)
	{
		Scores.setText(""+s);
	}
	public synchronized void showFood(Food food)
	{
		Map[food.getX()][food.getY()].setBackground(Food.FOOD_COLOR);
		Map[food.getX()][food.getY()].setText(" F");
	}
	public void showGameOverDialog_SelfEat(int sc)
	{
		JLabel tmp=new JLabel("You have eaten yourself!\n"
				+ "Your Scores are "+sc);
		tmp.setFont(new Font("Arial", Font.BOLD, 25));
		JOptionPane.showMessageDialog(Root,tmp);
	}
	public void showVictoryDailog()
	{
		JLabel tmp=new JLabel("You are Snake-Super! You win!");
		tmp.setFont(new Font("Arial", Font.BOLD, 25));
		JOptionPane.showMessageDialog(Root,tmp);
	}
	public synchronized void disposeGameSource()
	{
		this.paintMapWhite();
		KeyListener[]kl=this.Bn_Pause.getKeyListeners();
		for (int i=0;i<kl.length;i++)
		{
			Bn_Pause.removeKeyListener(kl[i]);
		}
		kl=this.Bn_Start.getKeyListeners();
		for (int i=0;i<kl.length;i++)
		{
			Bn_Start.removeKeyListener(kl[i]);
		}
		kl=this.Root.getKeyListeners();
		for (int i=0;i<kl.length;i++)
		{
			Root.removeKeyListener(kl[i]);
		}
	}
}

public class GluttonousSnake {
	public static void main(String args[])
	{
		Window wd=new Window();
		wd.show();
	}
}
