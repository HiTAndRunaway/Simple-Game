import java.awt.*;
import javax.swing.*;

import java.awt.event.*;
import javax.swing.*;

//import MapJFrame1.B;

import java.awt.geom.*;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class TetrisPanel extends JPanel implements Runnable ,ActionListener
{
	static int x=200;
	static int y=180;
	static int m=1;
   /* static	JButton b2=new JButton("°´Å¥");
    static	JPanel pp=new JPanel();*/
	private Image iBuffer;
	private Graphics gBuffer;
	public TetrisPanel()
	{
		Thread t=new Thread(this);
		t.start();
		/*b2.setBounds(0, 0, 80, 40);
	    pp.add(b2);
	    add(pp,"South");
		b2.addActionListener(this); */
	}
	public void run()
	{
		while(true)
		{
			try
			{
				Thread.sleep(1);
			}catch(InterruptedException e){}
				 x=x+m;            
				 if(x >= 250)
					 {
					 m = -m; 
					 }
				              if(x < 0)
				              {
				            	  m = 1;
				             }
				              repaint();
		}
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.RED);
		g.fillOval(x,y, 80, 80);
	}
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource()==MyWindow.b2)
		{
			run();
		}
	}

}
class MyWindow extends JFrame
{
    static	JButton b2=new JButton("°´Å¥");
    static	JPanel pp=new JPanel();
	MyWindow()
	{
		b2.setBounds(0, 0, 80, 40);
	    pp.add(b2);
	    add(pp,"North");
		b2.addActionListener(new TetrisPanel()); 
		this.setTitle(".......");
		Container c=this.getContentPane();
		c.add(new TetrisPanel());
		this.setBounds(400, 200, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	public static void main(String args[])
	{
		MyWindow DB=new MyWindow();
		DB.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		DB.setVisible(true);
	}
}
