import java.awt.Graphics;
import java.awt.event.*;

import javax.swing.JPanel;

public class SpritePanel extends JPanel implements Runnable
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
