import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	/** 屏幕的宽高* */
	private int mScreenWidth = 320;
	private int mScreenHeight = 480;
	/** 游戏主菜单状态* */
	private static final int STATE_GAME = 0;
	/** 游戏状态* */
	private int mState = STATE_GAME;
	/** 游戏背景资源 两张图片进行切换让屏幕滚动起来* */
	private Image mBitMenuBG0 = null;
	private Image mBitMenuBG1 = null;
	/** 记录两张背景图片时时更新的Y坐标* */
	private int mBitposY0 = 0;
	private int mBitposY1 = 0;
	/** 子弹对象的数量* */
	final static int BULLET_POOL_COUNT = 15;
	/** 飞机移动步长* */
	final static int PLAN_STEP = 10;
	/** 没过500毫秒发射一颗子弹* */
	final static int PLAN_TIME = 500;
	/** 敌人对象的数量* */
	final static int ENEMY_POOL_COUNT = 5;
	/** 敌人飞机偏移量* */
	final static int ENEMY_POS_OFF = 65;
	/** 游戏主线程* */
	private Thread mThread = null;
	/** 线程循环标志* */
	private boolean mIsRunning = false;
	/** 飞机在屏幕中的坐标* */
	public int mAirPosX = 0;
	public int mAirPosY = 0;

	/** 敌机对象数组* */
	Enemy mEnemy[] = null;
	/** 子弹对象数组* */
	Bullet mBuilet[] = null;
	/** 初始化发射子弹ID* */
	public int mSendId = 0;
	/** 上一颗子弹发射的时间* */
	public Long mSendTime = 0L;
	Image myPlanePic[];/** 玩家飞机所有图片* */
	public int myPlaneID = 0; /** 玩家飞机当前帧号* */

	/**
	 * 构造方法
	 */
	public GamePanel() {
		setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
		// 设定焦点在本窗体并付与监听对象
		setFocusable(true);
		addKeyListener(this);
		init();
		setGameState(STATE_GAME);
		/** 启动游戏主线程* */
		mIsRunning = true;
		mThread = new Thread(this);// 实例线程
		mThread.start();
		setVisible(true);

	}

	protected void Draw() {
		switch (mState) {
		case STATE_GAME:
			renderBg();// 绘制游戏界面（包括背景、敌我飞机、子弹)
			updateBg(); // 更新游戏
			break;
		}
	}

	private void init() {
		/** 游戏背景* */
		try {
			mBitMenuBG0 = Toolkit.getDefaultToolkit().getImage(
					"images\\map_0.png");

			mBitMenuBG1 = Toolkit.getDefaultToolkit().getImage(
					"images\\map_1.png");
			// Toolkit.getDefaultToolkit().getImage("pic\\t0.jpg");
			ImageIO.read(new File("images/map_1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** 创建主角飞机动画对象* */
		// mAircraft = new Animation(mContext,new int[]_);
		/** 第一张图片津贴在屏幕00点，第二张图片在第一张图片上方* */
		mBitposY0 = 0;
		mBitposY1 = -mScreenHeight;

		/** 初始化飞机的坐标* */
		mAirPosX = 150;
		mAirPosY = 400;
		myPlanePic = new Image[6];
		for (int i = 0; i < 6; i++)
			myPlanePic[i] = Toolkit.getDefaultToolkit().getImage(
					"images\\plan_" + i + ".png");
		/** 创建敌人对象* */
		mEnemy = new Enemy[ENEMY_POOL_COUNT];

		for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
			mEnemy[i] = new Enemy();
			mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF-300);
		}

		/** 创建子弹类对象* */
		mBuilet = new Bullet[BULLET_POOL_COUNT];
		for (int i = 0; i < BULLET_POOL_COUNT; i++) {
			mBuilet[i] = new Bullet();
		}
		mSendTime = System.currentTimeMillis();
	}

	private void setGameState(int newState) {
		mState = newState;
	}

	public void renderBg() {
		// Graphics g=this.getGraphics();
		myPlaneID++;
		if (myPlaneID == 6)
			myPlaneID = 0;
		repaint();
	}

	public void paint(Graphics g) {
		/** 绘制游戏地图 * */
		// ImageIcon icon=new ImageIcon("C://rear.gif");
		// Image mBitMenuBG0=icon.getImage();
		// g.clearRect(0, 0, mScreenWidth, mScreenHeight);
		g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
		g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
		/** 绘制自己飞机动画* */
		g.drawImage(myPlanePic[myPlaneID], mAirPosX, mAirPosY, this);
		// mAircraft.DrawAnimation(g, mAirPosX, mAirPosY);
		/** 绘制子弹动画 */
		for (int i = 0; i < BULLET_POOL_COUNT; i++)
			mBuilet[i].DrawBullet(g, this);

		/** 绘制敌人动画* */
		for (int i = 0; i < ENEMY_POOL_COUNT; i++)
			mEnemy[i].DrawEnemy(g, this);
	}

	private void updateBg() {
		/** 更新游戏背景图片实现向下滚动效果 * */
		mBitposY0 += 10;
		mBitposY1 += 10;
		if (mBitposY0 == mScreenHeight) {
			mBitposY0 = -mScreenHeight;
		}
		if (mBitposY1 == mScreenHeight) {
			mBitposY1 = -mScreenHeight;
		}

		/** 更新子弹动画 * */
		for (int i = 0; i < BULLET_POOL_COUNT; i++) {
			/** 子弹出屏后重新赋值* */
			mBuilet[i].UpdateBullet();

		}
		/** 绘制敌人动画* */
		for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
			mEnemy[i].UpdateEnemy();
			/** 敌机死亡 或者 敌机超过屏幕还未死亡重置坐标* */
			if (mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE
					&& mEnemy[i].mPlayID == 6
					|| mEnemy[i].m_posY >= mScreenHeight) {
				mEnemy[i].init(UtilRandom(0, ENEMY_POOL_COUNT) * ENEMY_POS_OFF,
						0);
			}

		}

		/** 根据时间初始化为发射的子弹* */
		if (mSendId < BULLET_POOL_COUNT) {
			long now = System.currentTimeMillis();
			if (now - mSendTime >= PLAN_TIME) {
				mBuilet[mSendId].init(mAirPosX-5, mAirPosY-40);
				mSendTime = now;
				mSendId++;
			}
		} else {
			mSendId = 0;
		}

		// 更新子弹与敌人的碰撞
		Collision();

	}

	public void Collision() {
		// 更新子弹与敌人碰撞
		for (int i = 0; i < BULLET_POOL_COUNT; i++) {
			for (int j = 0; j < ENEMY_POOL_COUNT; j++) {
				if (mBuilet[i].m_posX >= mEnemy[j].m_posX
						&& mBuilet[i].m_posX <= mEnemy[j].m_posX + 30
						&& mBuilet[i].m_posY >= mEnemy[j].m_posY
						&& mBuilet[i].m_posY <= mEnemy[j].m_posY + 30

				) {
					mEnemy[j].mAnimState = Enemy.ENEMY_DEATH_STATE;
				}
			}

		}
	}

	/**
	 * 返回一个随机数
	 * 
	 * @param botton
	 * @param top
	 * @return
	 */
	private int UtilRandom(int botton, int top) {
		return ((Math.abs(new Random().nextInt()) % (top - botton)) + botton);
	}

	public void run() {
		while (mIsRunning) {
			/** 绘制 */
			Draw();
			// 延时0.1秒
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// 在这里检测什么键被按下
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		System.out.println(key);
		if (key == KeyEvent.VK_UP)// 假如是向上键被按下
			mAirPosY -= PLAN_STEP;
		if (key == KeyEvent.VK_DOWN)// 假如是向下键被按下
			mAirPosY += PLAN_STEP;
		if (key == KeyEvent.VK_LEFT)// 假如是向左键被按下
		{
			mAirPosX -= PLAN_STEP;
			if (mAirPosX < 0)
				mAirPosX = 0;
		}
		if (key == KeyEvent.VK_RIGHT)// 假如是向右键被按下
		{
			mAirPosX += PLAN_STEP;
			if (mAirPosX > mScreenWidth - 30)
				mAirPosX = mScreenWidth - 30;
		}
		System.out.println(mAirPosX + ":" + mAirPosY);
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
	// public static void main(String[] args) {
	// GamePanel frame = new GamePanel();
	// }
	// public void surfaceDestroyed(SurfaceHolder arg0) {
	// // surfaceView销毁的时候
	// mIsRunning = false;
	//	}
}