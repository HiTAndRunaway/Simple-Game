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

	/** ��Ļ�Ŀ��* */
	private int mScreenWidth = 320;
	private int mScreenHeight = 480;
	/** ��Ϸ���˵�״̬* */
	private static final int STATE_GAME = 0;
	/** ��Ϸ״̬* */
	private int mState = STATE_GAME;
	/** ��Ϸ������Դ ����ͼƬ�����л�����Ļ��������* */
	private Image mBitMenuBG0 = null;
	private Image mBitMenuBG1 = null;
	/** ��¼���ű���ͼƬʱʱ���µ�Y����* */
	private int mBitposY0 = 0;
	private int mBitposY1 = 0;
	/** �ӵ����������* */
	final static int BULLET_POOL_COUNT = 15;
	/** �ɻ��ƶ�����* */
	final static int PLAN_STEP = 10;
	/** û��500���뷢��һ���ӵ�* */
	final static int PLAN_TIME = 500;
	/** ���˶��������* */
	final static int ENEMY_POOL_COUNT = 5;
	/** ���˷ɻ�ƫ����* */
	final static int ENEMY_POS_OFF = 65;
	/** ��Ϸ���߳�* */
	private Thread mThread = null;
	/** �߳�ѭ����־* */
	private boolean mIsRunning = false;
	/** �ɻ�����Ļ�е�����* */
	public int mAirPosX = 0;
	public int mAirPosY = 0;

	/** �л���������* */
	Enemy mEnemy[] = null;
	/** �ӵ���������* */
	Bullet mBuilet[] = null;
	/** ��ʼ�������ӵ�ID* */
	public int mSendId = 0;
	/** ��һ���ӵ������ʱ��* */
	public Long mSendTime = 0L;
	Image myPlanePic[];/** ��ҷɻ�����ͼƬ* */
	public int myPlaneID = 0; /** ��ҷɻ���ǰ֡��* */

	/**
	 * ���췽��
	 */
	public GamePanel() {
		setPreferredSize(new Dimension(mScreenWidth, mScreenHeight));
		// �趨�����ڱ����岢�����������
		setFocusable(true);
		addKeyListener(this);
		init();
		setGameState(STATE_GAME);
		/** ������Ϸ���߳�* */
		mIsRunning = true;
		mThread = new Thread(this);// ʵ���߳�
		mThread.start();
		setVisible(true);

	}

	protected void Draw() {
		switch (mState) {
		case STATE_GAME:
			renderBg();// ������Ϸ���棨�������������ҷɻ����ӵ�)
			updateBg(); // ������Ϸ
			break;
		}
	}

	private void init() {
		/** ��Ϸ����* */
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
		/** �������Ƿɻ���������* */
		// mAircraft = new Animation(mContext,new int[]_);
		/** ��һ��ͼƬ��������Ļ00�㣬�ڶ���ͼƬ�ڵ�һ��ͼƬ�Ϸ�* */
		mBitposY0 = 0;
		mBitposY1 = -mScreenHeight;

		/** ��ʼ���ɻ�������* */
		mAirPosX = 150;
		mAirPosY = 400;
		myPlanePic = new Image[6];
		for (int i = 0; i < 6; i++)
			myPlanePic[i] = Toolkit.getDefaultToolkit().getImage(
					"images\\plan_" + i + ".png");
		/** �������˶���* */
		mEnemy = new Enemy[ENEMY_POOL_COUNT];

		for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
			mEnemy[i] = new Enemy();
			mEnemy[i].init(i * ENEMY_POS_OFF, i * ENEMY_POS_OFF-300);
		}

		/** �����ӵ������* */
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
		/** ������Ϸ��ͼ * */
		// ImageIcon icon=new ImageIcon("C://rear.gif");
		// Image mBitMenuBG0=icon.getImage();
		// g.clearRect(0, 0, mScreenWidth, mScreenHeight);
		g.drawImage(mBitMenuBG0, 0, mBitposY0, this);
		g.drawImage(mBitMenuBG1, 0, mBitposY1, this);
		/** �����Լ��ɻ�����* */
		g.drawImage(myPlanePic[myPlaneID], mAirPosX, mAirPosY, this);
		// mAircraft.DrawAnimation(g, mAirPosX, mAirPosY);
		/** �����ӵ����� */
		for (int i = 0; i < BULLET_POOL_COUNT; i++)
			mBuilet[i].DrawBullet(g, this);

		/** ���Ƶ��˶���* */
		for (int i = 0; i < ENEMY_POOL_COUNT; i++)
			mEnemy[i].DrawEnemy(g, this);
	}

	private void updateBg() {
		/** ������Ϸ����ͼƬʵ�����¹���Ч�� * */
		mBitposY0 += 10;
		mBitposY1 += 10;
		if (mBitposY0 == mScreenHeight) {
			mBitposY0 = -mScreenHeight;
		}
		if (mBitposY1 == mScreenHeight) {
			mBitposY1 = -mScreenHeight;
		}

		/** �����ӵ����� * */
		for (int i = 0; i < BULLET_POOL_COUNT; i++) {
			/** �ӵ����������¸�ֵ* */
			mBuilet[i].UpdateBullet();

		}
		/** ���Ƶ��˶���* */
		for (int i = 0; i < ENEMY_POOL_COUNT; i++) {
			mEnemy[i].UpdateEnemy();
			/** �л����� ���� �л�������Ļ��δ������������* */
			if (mEnemy[i].mAnimState == Enemy.ENEMY_DEATH_STATE
					&& mEnemy[i].mPlayID == 6
					|| mEnemy[i].m_posY >= mScreenHeight) {
				mEnemy[i].init(UtilRandom(0, ENEMY_POOL_COUNT) * ENEMY_POS_OFF,
						0);
			}

		}

		/** ����ʱ���ʼ��Ϊ������ӵ�* */
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

		// �����ӵ�����˵���ײ
		Collision();

	}

	public void Collision() {
		// �����ӵ��������ײ
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
	 * ����һ�������
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
			/** ���� */
			Draw();
			// ��ʱ0.1��
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// ��������ʲô��������
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		System.out.println(key);
		if (key == KeyEvent.VK_UP)// ���������ϼ�������
			mAirPosY -= PLAN_STEP;
		if (key == KeyEvent.VK_DOWN)// ���������¼�������
			mAirPosY += PLAN_STEP;
		if (key == KeyEvent.VK_LEFT)// �����������������
		{
			mAirPosX -= PLAN_STEP;
			if (mAirPosX < 0)
				mAirPosX = 0;
		}
		if (key == KeyEvent.VK_RIGHT)// ���������Ҽ�������
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
	// // surfaceView���ٵ�ʱ��
	// mIsRunning = false;
	//	}
}