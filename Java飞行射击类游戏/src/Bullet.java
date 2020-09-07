import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import javax.swing.JPanel;

//子弹类
public class Bullet {
    /**子弹的X轴速度**/
    static final int BULLET_STEP_X = 3;    
    /**子弹的Y轴速度**/
    static final int BULLET_STEP_Y = 15;   
   
    /**子弹图片的宽度**/
    static final int BULLET_WIDTH = 40;
    
    /** 子弹的XY坐标 **/
    public int m_posX = 0;
    public int m_posY = 0;
    
    /**是否更新绘制子弹**/
    boolean mFacus = true;
    private Image pic[] = null; // 子弹图片数组
    /** 当前帧的ID **/
    private int mPlayID = 0;

    public Bullet() {
        pic = new Image[4];
    	for (int i = 0; i < 4; i++) 
    			pic[i] = Toolkit.getDefaultToolkit().getImage(
    					"images\\bullet_" + i + ".png");
    }
    
    /**初始化坐标**/
    public void init(int x, int y) {
	m_posX = x;
	m_posY = y;
	mFacus = true;
    }
    
    /**绘制子弹**/
    public void DrawBullet(Graphics g,JPanel i)
	{
    	g.drawImage(pic[mPlayID++],m_posX,m_posY,(ImageObserver)i);
    	if(mPlayID==4)mPlayID=0;
    }
    /**更新子弹的坐标点**/
    public void UpdateBullet() {
	if (mFacus) {
	    m_posY -= BULLET_STEP_Y;
	}

    }
    
}
