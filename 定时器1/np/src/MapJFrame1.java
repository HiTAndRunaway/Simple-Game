import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MapJFrame1 extends JFrame
{
	static double x=200;
	static double y=180;
	static double m=1;
    static	JPanel pp=new JPanel();
    static	JButton b2=new JButton("��ť");
    static public Timer timer;

	public MapJFrame1()
	{
		super("Draw 2D Shape Demo");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350,350);
		MapPane map=new MapPane();
		getContentPane().add(map);
		b2.setBounds(0, 0, 80, 40);
	      pp.add(b2);
	      add(pp,"North");
	      timer=new Timer(500,new B());
	      timer.start();
	     b2.addActionListener(new B()); 
	}
	
public static void main(String[] args) {
	MapJFrame1 frame=new MapJFrame1();
	frame.setVisible(true);
	
                                       }

class MapPane extends JPanel
{
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D) g;
		Line2D l=new Line2D.Float(1.0f, 2.0f, 150.0f, 20.0f);
		g2.draw(l);
		Rectangle2D r=new Rectangle2D.Float(50, 80, 100, 100);
		Color c=new Color(10,20,255);
		g2.setColor(c);
		g2.draw(r);
	    Ellipse2D z=new Ellipse2D.Double(MapJFrame1.x,MapJFrame1.y,100,100);
		g2.setColor(Color.GRAY);
		g2.fill(z);
		g2.drawString("Java��ʱ��", 0, 200);
		CubicCurve2D cubic=new CubicCurve2D.Float(0, 100, 120, 50, 170, 270, 220, 50);
		g2.draw(cubic);	
	}
}
class B implements ActionListener
{
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource()==MapJFrame1.b2)
		{
	        
			 while(true)
			 {
				 MapJFrame1.x=MapJFrame1.x+MapJFrame1.m;            
				 if(MapJFrame1.x >= 500)
					 {
					 MapJFrame1.m = -MapJFrame1.m; 
					 }
				              if(MapJFrame1.x < 0)
				              {
				            	  MapJFrame1.m = 1;
				             }
				              repaint();
				          }
				     }
	        
		}
	}
}
