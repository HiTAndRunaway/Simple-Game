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
    private int W = 50;                  //动物方块图案的宽度
    private int GameSize=10;             //布局大小即行列数
    private boolean Select_first = false;   //是否已经选中第一块
    private int x1, y1;                  //被选中第一块的地图坐标
    private int x2, y2;                  //被选中第二块的地图坐标
    private Point z1=new Point(0,0);
    private Point z2=new Point(0,0);    //折点棋盘坐标
    private int m_nCol = 10;
    private int m_nRow = 10;
    private int[] m_map = new int[10*10];
    private int BLANK_STATE = -1;
    public enum  LinkType {LineType,OneCornerType,TwoCornerType};
    LinkType LType;                       //连通方式

	public LLKPanel() {		
		setPreferredSize(new Dimension(500, 450));
		this.addMouseListener(this);//否则鼠标单击无反应
		StartNewGame();		
	}
	public void StartNewGame()
    {
        //初始化地图,将地图中所有方块区域位置置为空方块状态
        for(int iNum=0;iNum<(m_nCol*m_nRow);iNum++)
        {
            m_map[iNum] = BLANK_STATE;
        }
        Random r = new Random();
        //生成随机地图
        //将所有匹配成对的动物物种放进一个临时的地图中
        ArrayList  tmpMap=new ArrayList ();	
        for(int i=0;i<(m_nCol*m_nRow)/4;i++)
            for(int j=0;j<4;j++)
                tmpMap.add(i);

        //每次从上面的临时地图中取走(获取后并在临时地图删除)
        //一个动物放到地图的空方块上
        for (int i = 0; i < m_nRow * m_nCol; i++)
        {	
            //随机挑选一个位置
            int nIndex = r.nextInt(tmpMap.size())  ;
            //获取该选定物件放到地图的空方块
            m_map[i]=(Integer)tmpMap.get(nIndex);
            //在临时地图除去该动物
            tmpMap.remove(nIndex);		
        }
    }
    public void Init_Graphic()
    {
        Graphics g = this.getGraphics();           //生成Graphics对象
        for (int i = 0; i< 10 * 10; i++)
        {                
            g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W, 
             W * (i / GameSize)+W, W, W,this);
        }
    }  
    //create_image（）方法实现按标号n从所有动物图案的图片中截图。
    private Image create_image(int n)  //按标号n截图 
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
    	newpic=src.getSubimage(x, y, w, h);//截取原图中矩形区域的图形
    	return newpic;
    }
    ///
    ///   检测是否已经赢得了游戏
    ///
    boolean IsWin()
    {
        //检测所有是否尚有非未被消除的方块
        // (非BLANK_STATE状态)
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
    //X直接连通即垂直方向连通
    //
    boolean X_Link(int x, int y1,int y2)
    {
        //保证y1的值小于y2
        if(y1>y2)
        {
	        //数据交换
	        int n=y1;
	        y1=y2;
	        y2=n;
        }

        //直通 	
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
    //Y直接连通即水平方向连通
    //
    boolean Y_Link(int x1,int x2,int y)
    {
        if(x1>x2)
        {
	        int x=x1;
	        x1=x2;
	        x2=x;
        }
        //直通
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
    //   一个折点连通
    //
    boolean OneCornerLink(int x1, int y1,int x2, int y2)
    {
        if (x1 > x2) //目标点（x1,y1）,（x2,y2）两点交换
        {
            int n=x1;
            x1=x2;
            x2=n;
            n=y1;
            y1=y2;
            y2=n;
        }
        if (y2 < y1)  //（x1,y1）为矩形左下顶点，（x2,y2）点为矩形右上顶点
        {
            //判断矩形右下角折点（x2,y1）是否空
            if (m_map[y1 * m_nCol + x2] == BLANK_STATE)
            {
                if (Y_Link(x1, x2, y1) && X_Link(x2, y1, y2))
                //判断折点（x2,y1）与两个目标点是否直通
                {
                    z1.x= x2; z1.y = y1; //保存折点坐标到z1
                    return true;
                }
            }
            //判断矩形左上角折点（x1,y2）是否空
            if (m_map[y2 * m_nCol + x1] == BLANK_STATE)
            {
                if (Y_Link(x2 , x1, y2) && X_Link(x1, y2, y1))
                    //判断折点 （x1,y2）与两个目标点是否直通
                {
                    z1.x = x1; z1.y = y2; //保存折点坐标到z1
                    return true;
                }
            }
            return false;
        }
        else    //（x1,y1）为矩形左上顶点，（x2,y2）点为矩形右下顶点
        {
            //判断矩形左下角折点（x1,y2）是否空
            if (m_map[y2 * m_nCol + x1] == BLANK_STATE)
            {
                if (Y_Link(x1, x2, y2) && X_Link(x1, y1, y2 ))
                    //判断折点 （x1,y2）与两个目标点是否直通
                {
                    z1.x = x1; z1.y = y2; //保存折点坐标到z1
                    return true;
                }
            }
            //判断矩形右上角折点（x2,y1）是否空
            if (m_map[y1 * m_nCol + x2] == BLANK_STATE)
            {
                if (Y_Link(x1 , x2, y1) && X_Link(x2, y1, y2))
                    //判断折点（x2,y1）与两个目标点是否直通  
                {
                    z1.x = x2; z1.y = y1; //保存折点坐标到z1
                    return true;
                }
            }
            return false;
        }
    }
    ///
    ///  两个折点连通
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
        //右
        int x, y;
        for (x = x1 + 1; x <= m_nCol; x++)
        {
            if (x == m_nCol)
                //两个折点在选中方块的右侧，且两个折点在图案区域之外
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
        //左
        for (x = x1 - 1; x >=-1; x--)
        {
            if (x == -1)
                //两个折点在选中方块的左侧，且两个折点在图案区域之外
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
        //上
        for (y = y1 - 1; y >=-1; y--)
        {
            if (y == -1)
                //两个折点在选中方块的上侧，且两个折点在图案区域之外
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
        //下
        for (y = y1 + 1; y <=m_nRow; y++)
        {
            if (y == m_nRow)
                //两个折点在选中方块的下侧，且两个折点在图案区域之外
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
    boolean XThrough(int x, int y, boolean bAdd)//水平方向判断到边界的连通性  
    {
        if (bAdd)   //True,水平向右判断是否连通（是否为空）
        {
            for (int i = x; i < m_nCol; i++)
                if (m_map[y * m_nCol + i] != BLANK_STATE)
                    return false;
        }
        else      //false, 水平向左判断是否连通（是否为空）
        {
            for (int i = 0; i <= x; i++)
                if (m_map[y * m_nCol + i] != BLANK_STATE)
                    return false;
        }
        return true;
    }

    boolean YThrough(int x, int y, boolean bAdd)  //垂直方向判断到边界的连通性）
    {
        if(bAdd)   //True, 垂直方向向下判断是否连通（是否为空）
        {
            for(int i=y;i<m_nRow;i++)
                if(m_map[i*m_nCol+x]!=BLANK_STATE)
                    return false;
        }
        else      //false, 垂直方向向上判断是否连通（是否为空）
        {
            for(int i=0;i<=y;i++)
                if(m_map[i*m_nCol+x]!=BLANK_STATE)
                    return false;
        }
        return true;
    }

     //
    //  判断选中的两个方块是否可以消除
    //
    boolean IsLink(int x1, int y1, int x2, int y2)
    {
        //X直连方式即垂直方向连通
        if(x1==x2)
        {
            if(X_Link(x1,y1,y2))
            { LType=LinkType.LineType ; return true;}
        }
        //Y直连方式即水平方向连通
        else if(y1==y2)
        {
            if(Y_Link(x1,x2,y1))
            { LType = LinkType.LineType; return true; }
        }
        //一个转弯（折点）的联通方式
        if(OneCornerLink(x1,y1,x2,y2))
        {
            LType = LinkType.OneCornerType ; 
            return true; 
        }
        //两个转弯（折点）的联通方式
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
        //第一个方块从地图的0位置开始
        for (int i = 0; i < m_nRow * m_nCol; i++)
        {
            //找到则跳出循环
            if (bFound)
                break;
            //无动物的空格跳过
            if (m_map[i] == BLANK_STATE)
                continue;
            //第二个方块从前一个方块的后面开始
            for (int j = i + 1; j < m_nRow * m_nCol; j++)
            {

                //第二个方块不为空 且与第一个方块的动物相同
                if (m_map[j] != BLANK_STATE && m_map[i] == m_map[j])
                {
                    //算出对应的虚拟行列位置
                    x1 = i % m_nCol;
                    y1 = i / m_nCol;
                    x2 = j % m_nCol;
                    y2 = j / m_nCol;

                    //判断是否可以连通
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
            //（x1,y1）与（x2,y2）连通
            Graphics2D g2 =(Graphics2D) this.getGraphics();     //生成Graphics对象 
            g2.setColor(Color.RED);
            BasicStroke s=new BasicStroke(4); //创建宽度是４的画笔
    		g2.setStroke(s);
            g2.drawRect(x1 * W + 1 + W, y1 * W + 1 + W, W - 3, W - 3);
            g2.drawRect(x2 * W + 1 + W, y2 * W + 1 + W, W - 3, W - 3);
        }
        return bFound;
    }

 
    /// <summary>
    /// 画选中方块之间连接线
    /// </summary>
    private void DrawLinkLine(int x1, int y1, int x2, int y2,LinkType LType)
    { 
        Graphics g = this.getGraphics();     //生成Graphics对象
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
                //p1与pixel_z2不在一直线上，则pixel_z1，pixel_z2交换
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
        //画选中方块的示意边框线
        Graphics2D g2 =(Graphics2D)g;     //生成Graphics对象        
        BasicStroke s=new BasicStroke(4); //创建宽度是4的画笔
		g2.setStroke(s);
        g.drawRect(x * W + 1+W, y * W + 1+W, W - 3, W - 3);
    }     
	// 画游戏界面
	public void paint(Graphics g) {
		//注意此处不能再生成Graphics对象,否则导致错误图像
        //g = this.getGraphics();     
		//注意此处不能清屏,否则出现闪烁问题
        //g.clearRect(0, 0, this.getWidth(), this.getHeight());
        for (int i = 0; i< 10 * 10; i++)
        {
        	if(m_map[i]==BLANK_STATE)//此处不是空百块
        		g.clearRect(W * (i % GameSize)+W, W * (i / GameSize)+W, W, W);
        	else
            g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W, 
             W * (i / GameSize)+W, W, W,this);
        }
        //清除四周连线
        g.clearRect(0, 0, W, 12*W);
        g.clearRect(11*W, 0, W, 12*W);
        g.clearRect(0, 0, 12*W, W);
        g.clearRect(0,11*W , 12*W, W);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
        Graphics g = this.getGraphics();     //生成Graphics对象
        int x, y;
        if (e.getButton() == MouseEvent.BUTTON1)//左键单击
        {
            //计算点击的方块的位置坐标
            x = (e.getX() - W) / W;
            y = (e.getY() - W) / W;
            System.out.print(x);
            System.out.println(x);
            //如果该区域无方块
            if (m_map[y * m_nCol + x] == BLANK_STATE) return;
            if (Select_first == false)
            {
                x1 = x; y1 = y;
                //画选定（x1,y1)处的框线
                DrawSelectedBlock(x1, y1, g);
                Select_first = true;
            }
            else
            {
                x2 = x; y2 = y;
                //判断第二次点击的方块是否已被第一次点击选取，如果是则返回。
                if ((x1 == x2) && (y1 == y2)) return;
                //画选定（x2,y2)处的框线
                DrawSelectedBlock(x2, y2, g);
                //判断是否连通
                if (IsSame(x1, y1, x2, y2) && IsLink(x1, y1, x2, y2))
                {
                	int grade=Integer.parseInt(LLKFrame3.textarea1.getText())+2;
                	LLKFrame3.textarea1.setText(String.valueOf(grade));
                    DrawLinkLine(x1, y1, x2, y2,LType);   //画选中方块之间连接线
                    System.out.println(x1+"连通"+y1);
                    try 
                    { 
                       Thread.currentThread().sleep(500);//毫秒,延时0.5秒
                    }
                    catch(Exception e1){} 
                    //清空记录方块的值
                    m_map[y1 * m_nCol + x1] = BLANK_STATE;
                    m_map[y2 * m_nCol + x2] = BLANK_STATE;
                    Select_first = false; 
                    repaint();
                }
                else //重新选定第一个方块
                {
                    //重画（x1,y1)处动物图案来达到取消原选定（x1,y1)处的框线
                    int i = y1 * m_nCol + x1;
                    g.drawImage(create_image(m_map[i]), W * (i % GameSize)+W,
                         W * (i / GameSize)+W, W, W,this);
                    //设置重新选定第一个方块的坐标
                    x1 = x; y1 = y;
                    Select_first = true;
                }
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3)//智能查找功能
        {
            if (!Find2Block()) 
            	JOptionPane.showMessageDialog(this, "没有连通的方块了！！");
        }
        //察看是否已经胜利
        if (IsWin())
        {
        	JOptionPane.showMessageDialog(this, "恭喜您胜利闯关,即将开始新局");
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



