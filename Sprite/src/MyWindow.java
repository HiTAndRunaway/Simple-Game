import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MyWindow extends JFrame
{
	public MyWindow()
	{
		this.setTitle(".....");
		Container c=this.getContentPane();
		c.add(new SpritePanel());
		this.setBounds(400,200,300,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public static void main(String args[])
	{
		MyWindow DB=new MyWindow();
		DB.addWindowListener(new WindowAdapter()
		{
			public void windowclosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
	}

}
