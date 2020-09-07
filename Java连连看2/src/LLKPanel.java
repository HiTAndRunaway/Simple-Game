import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class LLKPanel extends JPanel implements MouseListener{
	/**
	 * 
	 */
    private int W = 50;                  //���﷽��ͼ���Ŀ��
    private int GameSize=10;             //���ִ�С��������
    private boolean Select_first = false;   //�Ƿ��Ѿ�ѡ�е�һ��
    private int x1, y1;                  //��ѡ�е�һ��ĵ�ͼ����
    private int x2, y2;                  //��ѡ�еڶ���ĵ�ͼ����
    private Point z1=new Point(0,0);
    private Point z2=new Point(0,0);    //�۵���������
    private int m_nCol = 10;
    private int m_nRow = 10;
    private int[] m_map = new int[10*10];
    private int BLANK_STATE = -1;
    public enum  LinkType {LineType,OneCornerType,TwoCornerType};
    LinkType LType;                       //��ͨ��ʽ

	public LLKPanel() {		
		setPreferredSize(new Dimension(500, 450));
		this.addMouseListener(this);//������굥���޷�Ӧ
		StartNewGame();		
	}
	public void StartNewGame()
    {
        //��ʼ����ͼ,����ͼ�����з�������λ����Ϊ�շ���״̬
        for(int iNum=0;iNum<(m_nCol*m_nRow);iNum++)
        {
            m_map[iNum] = BLANK_STATE;
        }
        Random r = new Random();
        //���������ͼ
        //������ƥ��ɶԵĶ������ַŽ�һ����ʱ�ĵ�ͼ��
        ArrayList  tmpMap=new ArrayList ();	
        for(int i=0;i<(m_nCol*m_nRow)/4;i++)
            for(int j=0;j<4;j++)
                tmpMap.add(i);

        //ÿ�δ��������ʱ��ͼ��ȡ��(��ȡ������ʱ��ͼɾ��)
        //һ������ŵ���ͼ�Ŀշ�����
        for (int i = 0; i < m_nRow * m_nCol; i++)
        {	
            //�����ѡһ��λ��
            int nIndex = r.nextInt(tmpMap.size())  ;
            //��ȡ��ѡ������ŵ���ͼ�Ŀշ���
            m_map[i]=(Integer)tmpMap.get(nIndex);
            //����ʱ��ͼ��ȥ�ö���
            tmpMap.remove(nIndex);		
        }
    }
    public void Init_Graphic()
    {
        Graphics g = this.getGraphics();           //����Graphics����
        for (int i = 0; i< 10 * 10; i++)
        {                
            g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W, 
             W * (i / GameSize)+W, W, W,this);
        }
    }  
    //create_image��������ʵ�ְ����n�����ж���ͼ����ͼƬ�н�ͼ��
    private Image create_image(int n)  //�����n��ͼ 
    {
    	int x=0;
    	int y=n*39 ;
    	int w=39;
    	int h=39;
    	BufferedImage src=null;
    	BufferedImage newpic=null;
    	try{
    		 src = ImageIO.read(new File("pic\\animal2.bmp"));
    	}
    	catch(Exception e)
    	{
    		System.out.println(e);
    	}
    	newpic=src.getSubimage(x, y, w, h);//��ȡԭͼ�о��������ͼ��
    	return newpic;
    }
    ///
    ///   ����Ƿ��Ѿ�Ӯ������Ϸ
    ///
    boolean IsWin()
    {
        //��������Ƿ����з�δ�������ķ���
        // (��BLANK_STATE״̬)
        for(int i=0;i<m_nRow*m_nCol;i++)
        {
            if(m_map[i] != BLANK_STATE)
            {
                return false;
            }
        }
        return true;
    }
    private boolean IsSame(int x1, int y1,int x2, int y2)
    {
        if (m_map[y1 * m_nCol + x1] == m_map[y2 * m_nCol + x2])
            return true;
        else
            return false; 
    }
    //
    //Xֱ����ͨ����ֱ������ͨ
    //
    boolean X_Link(int x, int y1,int y2)
    {
        //��֤y1��ֵС��y2
        if(y1>y2)
        {
	        //���ݽ���
	        int n=y1;
	        y1=y2;
	        y2=n;
        }

        //ֱͨ 	
        for(int i=y1+1;i<=y2;i++)
        {
	        if(i==y2)
		        return true;
	        if(m_map[i*m_nCol+x]!=BLANK_STATE)
		        break;
        }
        return false;
    }

    //
    //Yֱ����ͨ��ˮƽ������ͨ
    //
    boolean Y_Link(int x1,int x2,int y)
    {
        if(x1>x2)
        {
	        int x=x1;
	        x1=x2;
	        x2=x;
        }
        //ֱͨ
        for(int i=x1+1;i<=x2;i++)
        {
	        if(i==x2)
		        return true;
	        if(m_map[y*m_nCol+i]!=BLANK_STATE)
		        break;
        }
        return false;
    }

    //
    //   һ���۵���ͨ
    //
    boolean OneCornerLink(int x1, int y1,int x2, int y2)
    {
        if (x1 > x2) //Ŀ��㣨x1,y1��,��x2,y2�����㽻��
        {
            int n=x1;
            x1=x2;
            x2=n;
            n=y1;
            y1=y2;
            y2=n;
        }
        if (y2 < y1)  //��x1,y1��Ϊ�������¶��㣬��x2,y2����Ϊ�������϶���
        {
            //�жϾ������½��۵㣨x2,y1���Ƿ��
            if (m_map[y1 * m_nCol + x2] == BLANK_STATE)
            {
                if (Y_Link(x1, x2, y1) && X_Link(x2, y1, y2))
                //�ж��۵㣨x2,y1��������Ŀ����Ƿ�ֱͨ
                {
                    z1.x= x2; z1.y = y1; //�����۵����굽z1
                    return true;
                }
            }
            //�жϾ������Ͻ��۵㣨x1,y2���Ƿ��
            if (m_map[y2 * m_nCol + x1] == BLANK_STATE)
            {
                if (Y_Link(x2 , x1, y2) && X_Link(x1, y2, y1))
                    //�ж��۵� ��x1,y2��������Ŀ����Ƿ�ֱͨ
                {
                    z1.x = x1; z1.y = y2; //�����۵����굽z1
                    return true;
                }
            }
            return false;
        }
        else    //��x1,y1��Ϊ�������϶��㣬��x2,y2����Ϊ�������¶���
        {
            //�жϾ������½��۵㣨x1,y2���Ƿ��
            if (m_map[y2 * m_nCol + x1] == BLANK_STATE)
            {
                if (Y_Link(x1, x2, y2) && X_Link(x1, y1, y2 ))
                    //�ж��۵� ��x1,y2��������Ŀ����Ƿ�ֱͨ
                {
                    z1.x = x1; z1.y = y2; //�����۵����굽z1
                    return true;
                }
            }
            //�жϾ������Ͻ��۵㣨x2,y1���Ƿ��
            if (m_map[y1 * m_nCol + x2] == BLANK_STATE)
            {
                if (Y_Link(x1 , x2, y1) && X_Link(x2, y1, y2))
                    //�ж��۵㣨x2,y1��������Ŀ����Ƿ�ֱͨ  
                {
                    z1.x = x2; z1.y = y1; //�����۵����굽z1
                    return true;
                }
            }
            return false;
        }
    }
    ///
    ///  �����۵���ͨ
    ///
    boolean TwoCornerLink(int x1, int y1, int x2, int y2)
    {
        if (x1 > x2)
        {
            int n = x1;
            x1 = x2;
            x2 = n;
            n = y1;
            y1 = y2;
            y2 = n;
        }
        //��
        int x, y;
        for (x = x1 + 1; x <= m_nCol; x++)
        {
            if (x == m_nCol)
                //�����۵���ѡ�з�����Ҳ࣬�������۵���ͼ������֮��
                if (XThrough(x2 + 1, y2, true))
                    //Y_Link(x2 + 1, m_nCol-1, y2)&&m_map[y1 * (m_nCol-1) + x]== BLANK_STATE
                {
                    z2.x = m_nCol; z2.y = y1;
                    z1.x = m_nCol; z1.y = y2;
                    return true;
                }
                else
                    break ;
            if (m_map[y1 * m_nCol + x] != BLANK_STATE)
                break;
            if (OneCornerLink(x, y1, x2, y2))
            {
                z2.x = x; z2.y = y1;
                return true;
            }
        }
        //��
        for (x = x1 - 1; x >=-1; x--)
        {
            if (x == -1)
                //�����۵���ѡ�з������࣬�������۵���ͼ������֮��
                if (XThrough(x2 - 1, y2, false))
                {
                    z2.x = -1; z2.y = y1;
                    z1.x = -1; z1.y = y2;
                    return true;
                }
                else
                    break;

            if (m_map[y1 * m_nCol + x] != BLANK_STATE)
                break;
            if (OneCornerLink(x, y1, x2, y2))
            {
                z2.x = x; z2.y = y1;
                return true;
            }
        }
        //��
        for (y = y1 - 1; y >=-1; y--)
        {
            if (y == -1)
                //�����۵���ѡ�з�����ϲ࣬�������۵���ͼ������֮��
                if (YThrough(x2, y2 - 1, false))
                {
                    z2.x = x1; z2.y = -1;
                    z1.x = x2; z1.y = -1;
                    return true;
                }
                else
                    break;
            if (m_map[y * m_nCol + x1] != BLANK_STATE)
                break;
            if (OneCornerLink(x1, y, x2, y2))
            {
                z2.x = x1; z2.y = y;
                return true;
            }
        }
        //��
        for (y = y1 + 1; y <=m_nRow; y++)
        {
            if (y == m_nRow)
                //�����۵���ѡ�з�����²࣬�������۵���ͼ������֮��
                if (YThrough(x2, y2 + 1, true))
                {
                    z2.x = x1; z2.y = m_nRow;
                    z1.x = x2; z1.y = m_nRow;
                    return true;
                }
                else
                    break;
            if (m_map[y * m_nCol + x1] != BLANK_STATE)
                break;
            if (OneCornerLink(x1, y, x2, y2))
            {
                z2.x = x1; z2.y = y;
                return true;
            }
        }
        return false;
    }
    boolean XThrough(int x, int y, boolean bAdd)//ˮƽ�����жϵ��߽����ͨ��  
    {
        if (bAdd)   //True,ˮƽ�����ж��Ƿ���ͨ���Ƿ�Ϊ�գ�
        {
            for (int i = x; i < m_nCol; i++)
                if (m_map[y * m_nCol + i] != BLANK_STATE)
                    return false;
        }
        else      //false, ˮƽ�����ж��Ƿ���ͨ���Ƿ�Ϊ�գ�
        {
            for (int i = 0; i <= x; i++)
                if (m_map[y * m_nCol + i] != BLANK_STATE)
                    return false;
        }
        return true;
    }

    boolean YThrough(int x, int y, boolean bAdd)  //��ֱ�����жϵ��߽����ͨ�ԣ�
    {
        if(bAdd)   //True, ��ֱ���������ж��Ƿ���ͨ���Ƿ�Ϊ�գ�
        {
            for(int i=y;i<m_nRow;i++)
                if(m_map[i*m_nCol+x]!=BLANK_STATE)
                    return false;
        }
        else      //false, ��ֱ���������ж��Ƿ���ͨ���Ƿ�Ϊ�գ�
        {
            for(int i=0;i<=y;i++)
                if(m_map[i*m_nCol+x]!=BLANK_STATE)
                    return false;
        }
        return true;
    }

     //
    //  �ж�ѡ�е����������Ƿ��������
    //
    boolean IsLink(int x1, int y1, int x2, int y2)
    {
        //Xֱ����ʽ����ֱ������ͨ
        if(x1==x2)
        {
            if(X_Link(x1,y1,y2))
            { LType=LinkType.LineType ; return true;}
        }
        //Yֱ����ʽ��ˮƽ������ͨ
        else if(y1==y2)
        {
            if(Y_Link(x1,x2,y1))
            { LType = LinkType.LineType; return true; }
        }
        //һ��ת�䣨�۵㣩����ͨ��ʽ
        if(OneCornerLink(x1,y1,x2,y2))
        {
            LType = LinkType.OneCornerType ; 
            return true; 
        }
        //����ת�䣨�۵㣩����ͨ��ʽ
        else if(TwoCornerLink(x1,y1,x2,y2))
        {
            LType = LinkType.TwoCornerType;
            return true;
        }
        return false;
    }

    private boolean Find2Block()
    {
        boolean bFound = false;
        //��һ������ӵ�ͼ��0λ�ÿ�ʼ
        for (int i = 0; i < m_nRow * m_nCol; i++)
        {
            //�ҵ�������ѭ��
            if (bFound)
                break;
            //�޶���Ŀո�����
            if (m_map[i] == BLANK_STATE)
                continue;
            //�ڶ��������ǰһ������ĺ��濪ʼ
            for (int j = i + 1; j < m_nRow * m_nCol; j++)
            {

                //�ڶ������鲻Ϊ�� �����һ������Ķ�����ͬ
                if (m_map[j] != BLANK_STATE && m_map[i] == m_map[j])
                {
                    //�����Ӧ����������λ��
                    x1 = i % m_nCol;
                    y1 = i / m_nCol;
                    x2 = j % m_nCol;
                    y2 = j / m_nCol;

                    //�ж��Ƿ������ͨ
                    if (IsLink(x1, y1, x2, y2))
                    {
                        bFound = true;
                        break;
                    }
                }
            }
        }
        if (bFound)
        {
            //��x1,y1���루x2,y2����ͨ
            Graphics2D g2 =(Graphics2D) this.getGraphics();     //����Graphics���� 
            g2.setColor(Color.RED);
            BasicStroke s=new BasicStroke(4); //��������ǣ��Ļ���
    		g2.setStroke(s);
            g2.drawRect(x1 * W + 1 + W, y1 * W + 1 + W, W - 3, W - 3);
            g2.drawRect(x2 * W + 1 + W, y2 * W + 1 + W, W - 3, W - 3);
        }
        return bFound;
    }

 
    /// <summary>
    /// ��ѡ�з���֮��������
    /// </summary>
    private void DrawLinkLine(int x1, int y1, int x2, int y2,LinkType LType)
    { 
        Graphics g = this.getGraphics();     //����Graphics����
        Point p1 = new Point(x1 * W + W / 2+W, y1 * W + W / 2+W);
        Point p2 = new Point(x2 * W + W / 2+W, y2 * W + W / 2+W);
        if (LType == LinkType.LineType)
            g.drawLine(p1.x,p1.y,p2.x,p2.y);
        if (LType == LinkType.OneCornerType)
        {
            Point pixel_z1 = new Point(z1.x * W + W / 2+W, z1.y * W + W / 2+W);
            g.drawLine(p1.x,p1.y,pixel_z1.x,pixel_z1.y);
            g.drawLine(pixel_z1.x,pixel_z1.y, p2.x,p2.y);
        }
        if (LType == LinkType.TwoCornerType)
        {
            Point pixel_z1 = new Point(z1.x * W + W / 2+W, z1.y * W + W / 2+W);
            Point pixel_z2 = new Point(z2.x * W + W / 2+W, z2.y * W + W / 2+W);
            if (!(p1.x == pixel_z2.x || p1.y == pixel_z2.y))
            {
                //p1��pixel_z2����һֱ���ϣ���pixel_z1��pixel_z2����
                Point c;
                c = pixel_z1;
                pixel_z1 = pixel_z2;
                pixel_z2 = c;
            }
            g.drawLine( p1.x,p1.y, pixel_z2.x,pixel_z2.y);
            g.drawLine( pixel_z2.x,pixel_z2.y, pixel_z1.x,pixel_z1.y);
            g.drawLine( pixel_z1.x,pixel_z1.y, p2.x,p2.y);
        } 
    }

    private void DrawSelectedBlock(int x, int y, Graphics g)            
    {    	
        //��ѡ�з����ʾ��߿���
        Graphics2D g2 =(Graphics2D)g;     //����Graphics����        
        BasicStroke s=new BasicStroke(4); //���������4�Ļ���
		g2.setStroke(s);
        g.drawRect(x * W + 1+W, y * W + 1+W, W - 3, W - 3);
    }     
	// ����Ϸ����
	public void paint(Graphics g) {
		//ע��˴�����������Graphics����,�����´���ͼ��
        //g = this.getGraphics();     
		//ע��˴���������,���������˸����
        //g.clearRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i< 10 * 10; i++)
        {
        	if(m_map[i]==BLANK_STATE)//�˴����ǿհٿ�
        		g.clearRect(W * (i % GameSize)+W, W * (i / GameSize)+W, W, W);
        	else
            g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W, 
             W * (i / GameSize)+W, W, W,this);
        }
        //�����������
        g.clearRect(0, 0, W, 12*W);
        g.clearRect(11*W, 0, W, 12*W);
        g.clearRect(0, 0, 12*W, W);
        g.clearRect(0,11*W , 12*W, W);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
        Graphics g = this.getGraphics();     //����Graphics����
        int x, y;
        if (e.getButton() == MouseEvent.BUTTON1)//�������
        {
            //�������ķ����λ������
            x = (e.getX() - W) / W;
            y = (e.getY() - W) / W;
            System.out.print(x);
            System.out.println(x);
            //����������޷���
            if (m_map[y * m_nCol + x] == BLANK_STATE) return;
            if (Select_first == false)
            {
                x1 = x; y1 = y;
                //��ѡ����x1,y1)���Ŀ���
                DrawSelectedBlock(x1, y1, g);
                Select_first = true;
            }
            else
            {
                x2 = x; y2 = y;
                //�жϵڶ��ε���ķ����Ƿ��ѱ���һ�ε��ѡȡ��������򷵻ء�
                if ((x1 == x2) && (y1 == y2)) return;
                //��ѡ����x2,y2)���Ŀ���
                DrawSelectedBlock(x2, y2, g);
                //�ж��Ƿ���ͨ
                if (IsSame(x1, y1, x2, y2) && IsLink(x1, y1, x2, y2))
                {
                	int grade=Integer.parseInt(LLKFrame3.textarea1.getText())+2;
                	LLKFrame3.textarea1.setText(String.valueOf(grade));
                    DrawLinkLine(x1, y1, x2, y2,LType);   //��ѡ�з���֮��������
                    System.out.println(x1+"��ͨ"+y1);
                    try 
                    { 
                       Thread.currentThread().sleep(500);//����,��ʱ0.5��
                    }
                    catch(Exception e1){} 
                    //��ռ�¼�����ֵ
                    m_map[y1 * m_nCol + x1] = BLANK_STATE;
                    m_map[y2 * m_nCol + x2] = BLANK_STATE;
                    Select_first = false; 
                    repaint();
                }
                else //����ѡ����һ������
                {
                    //�ػ���x1,y1)������ͼ�����ﵽȡ��ԭѡ����x1,y1)���Ŀ���
                    int i = y1 * m_nCol + x1;
                    g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W,
                         W * (i / GameSize)+W, W, W,this);
                    //��������ѡ����һ�����������
                    x1 = x; y1 = y;
                    Select_first = true;
                }
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3)//���ܲ��ҹ���
        {
            if (!Find2Block()) 
            	JOptionPane.showMessageDialog(this, "û����ͨ�ķ����ˣ���");
        }
        //�쿴�Ƿ��Ѿ�ʤ��
        if (IsWin())
        {
        	JOptionPane.showMessageDialog(this, "��ϲ��ʤ������,������ʼ�¾�");
            //StartNewGame();
        }		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}



