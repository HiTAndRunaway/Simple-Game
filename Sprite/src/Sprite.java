import java.awt.*;

import javax.swing.*;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;

public class Sprite implements KeyListener
{
	public static int m_x=0,m_y=0;
	private Image pic[]=null;
	private int pid=0;
	boolean mFacus=true;
	
	/*this.setTitle(".....");
	Container c=this.getContentPane();
	c.add(new SpritePanel());
	this.setBounds(400,200,300,300);
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setVisible(true);*/
	
	public Sprite()
	{
		pic=new Image[4];
		for(int i=0;i<4;i++)
			pic[i]=Toolkit.getDefaultToolkit().getImage("images\\d"+i+".png");
	}

public void init(int x,int y)
{
	m_x=x;
	m_y=y;
}
public void set(int x,int y)
{
	m_x=x;
	m_y=y;
}
public void DrawSprite(Graphics g,JPanel i)
{
	g.drawImage(pic[pid],m_x,m_y,(ImageObserver)i);
	pid++;
	if(pid==4)
		pid=0;
}
public void UpdateSprite()
{
	if(mFacus==true)
	{
		m_x+=15;
	}
	if(m_x==300)
	{
		m_x=0;
	}
}
public void keyPressed(KeyEvent e)
{
	int keycode=e.getKeyCode();
	switch(keycode)
	{
	case KeyEvent.VK_LEFT:
		m_x-=10;
		break;
	case KeyEvent.VK_RIGHT:
		m_x+=10;
		break;
	}
	repaint();
}
public void keyReleased(KeyEvent arg0)
{
}
public void keyType(KeyEvent arg0)
{
}
public void keyListener(KeyEvent arg0)
{
}
}
/*public class SpritePanel extends JPanel implements Runnable
{
	private Sprite player;
	public SpritePanel()
	{
		player=new Sprite();
		Thread t=new Thread(this);
		t.start();
	}
	public void run()
	{
		while(true)
		{
			player.UpdateSprite();
			try{
				Thread.sleep(500);
			}catch(InterruptedException e){}
			repaint();
		}
	}
	public void paint(Graphics g)
	{
		super.paint(g);
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		player.DrawSprite(g, this);
	}
}*/