//6-18完成
//推箱子带音乐版
//右键单击--悔棋功能
//用时1：40分钟

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;

import javax.management.timer.Timer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class GameFrame extends JFrame implements ActionListener, MouseListener// 主面板类
		, KeyListener {
	private int grade = 0;
	// row,column记载人的行号 列号
	// leftX,leftY 记载左上角图片的位置 避免图片从(0,0)坐标开始
	private int row = 7, column = 7, leftX = 0, leftY = 0;
	// 记载地图的行列数
	private int mapRow = 0, mapColumn = 0;
	// width,height 记载屏幕的大小
	private int width = 0, height = 0;
	private boolean acceptKey = true;
	// 程序所用到的图片
	private Image pic[] = null;
	// 定义一些常量，对应地图的元素
	final byte WALL = 1, BOX = 2, BOXONEND = 3, END = 4, MANDOWN = 5,
			MANLEFT = 6, MANRIGHT = 7, MANUP = 8, GRASS = 9, MANDOWNONEND = 10,
			MANLEFTONEND = 11, MANRIGHTONEND = 12, MANUPONEND = 13;

	private byte[][] map = null;
	private ArrayList list = new ArrayList();
	Sound sound;
	

	public void getManPosition() {
		for (int i = 0; i < map.length; i++)
			for (int j = 0; j < map[0].length; j++)
				if (map[i][j] == MANDOWN || map[i][j] == MANDOWNONEND
						|| map[i][j] == MANUP || map[i][j] == MANUPONEND
						|| map[i][j] == MANLEFT || map[i][j] == MANLEFTONEND
						|| map[i][j] == MANRIGHT || map[i][j] == MANRIGHTONEND) {
					row = i;
					column = j;
					break;
				}
	}

	/* 显示提示信息对话框 */
	public void DisplayToast(String str) {
		JOptionPane.showMessageDialog(null, str, "提示",
				JOptionPane.ERROR_MESSAGE);
	}

	// 撤销移动
	public void undo() {
		if (acceptKey) {
			// 撤销
			if (list.size() > 0) {
				// 若要撤销 必须走过
				Map priorMap = (Map) list.get(list.size() - 1);
				map = priorMap.getMap();
				row = priorMap.getManX();
				column = priorMap.getManY();
				repaint();
				list.remove(list.size() - 1);
			} else
				DisplayToast("不能再撤销！");
		} else {
			DisplayToast("此关已完成，不能撤销！");
		}
	}

	public void nextGrade() {
		// grade++;
		if (grade >= MapFactory.getCount() - 1) {
			DisplayToast("恭喜你完成所有关卡！");
			acceptKey = false;
		} else {
			grade++;
			super.paint(gBuffer);
			initMap();
			repaint();
			acceptKey = true;
		}
	}

	public void priorGrade() {
		grade--;
		acceptKey = true;
		if (grade < 0)
			grade = 0;
		initMap();
		repaint();
	}

	public void initMap() {
		map = getMap(grade);
		list.clear();
		getMapSizeAndPosition();
		getManPosition();
		// Map currMap=new Map(row, column, map);
		// list.add(currMap);
	}

	/*
	 * //原来是从数组读取 2012－4－20 public byte[][] getMap(int grade) { return
	 * MapFactory.getMap(grade); }
	 */

	public GameFrame() {
		super("推箱子游戏带音乐版");
		setSize(600, 600);
		setVisible(true);
		setResizable(false);
		setLocation(300, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cont = getContentPane();
		cont.setLayout(null);
		cont.setBackground(Color.black);
		// 最初始13张图片
		getPic();
		width = this.getWidth();
		height = this.getHeight();
		this.setFocusable(true);
		initMap();
		// 构造方法执行时从优先数据中恢复游戏
		// 关卡切换时调用initMap()
		// resumeGame();
		this.addKeyListener(this);
		this.addMouseListener(this);
		 =new TimerTest();
		sound = new Sound();
		sound.loadSound();
	}

	private void getMapSizeAndPosition() {
		// TODO Auto-generated method stub
		mapRow = map.length;
		mapColumn = map[0].length;
		leftX = (width - map[0].length * 30) / 2;
		leftY = (height - map.length * 30) / 2;
		System.out.println(leftX);
		System.out.println(leftY);
		System.out.println(mapRow);
		System.out.println(mapColumn);
	}

	public void getPic() {
		pic = new Image[14];
		for (int i = 0; i <= 13; i++) {
			pic[i] = Toolkit.getDefaultToolkit().getImage(
					"images\\pic" + i + ".JPG");
		}
	}

	public byte grassOrEnd(byte man) {
		byte result = GRASS;
		if (man == MANDOWNONEND || man == MANLEFTONEND || man == MANRIGHTONEND
				|| man == MANUPONEND)
			result = END;
		return result;
	}

	private void moveUp() {
		// 上一位p1为WALL
		if (map[row - 1][column] == WALL)
			return;
		// 上一位p1为 BOX,BOXONEND,须考虑P2
		if (map[row - 1][column] == BOX || map[row - 1][column] == BOXONEND) {
			// 上上一位p2为 END,GRASS则向上一步,其他不用处理
			if (map[row - 2][column] == END || map[row - 2][column] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row - 2][column] == END ? BOXONEND : BOX;
				byte manTemp = map[row - 1][column] == BOX ? MANUP : MANUPONEND;
				// 箱子变成temp,箱子往前一步
				map[row - 2][column] = boxTemp;
				// 人变成MANUP,往上走一步
				map[row - 1][column] = manTemp;
				// 人刚才站的地方变成GRASS或者END
				map[row][column] = grassOrEnd(map[row][column]);
				// 人离开后修改人的坐标
				row--;
			}
		} else {
			// 上一位为 GRASS,END,不须考虑P2。其他情况不用处理
			if (map[row - 1][column] == GRASS || map[row - 1][column] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row - 1][column] == END ? MANUPONEND : MANUP;
				// 人变成temp,人往上走一步
				map[row - 1][column] = temp;
				// 人刚才站的地方变成GRASS或者END
				map[row][column] = grassOrEnd(map[row][column]);
				// 人离开后修改人的坐标
				row--;
			}
		}
	}

	private void moveDown() {
		// 下一位p1为WALL
		if (map[row + 1][column] == WALL)
			return;
		// 下一位p1为 BOX,BOXONEND,须考虑P2
		if (map[row + 1][column] == BOX || map[row + 1][column] == BOXONEND) {
			// 下下一位p2为 END,GRASS则向下一步,其他不用处理
			if (map[row + 2][column] == END || map[row + 2][column] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row + 2][column] == END ? BOXONEND : BOX;
				byte manTemp = map[row + 1][column] == BOX ? MANDOWN
						: MANDOWNONEND;
				// 箱子变成boxTemp,箱子往下一步
				map[row + 2][column] = boxTemp;
				// 人变成manTemp,往下走一步
				map[row + 1][column] = manTemp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				row++;
			}

		} else {
			// 下一位为 GRASS,END,不须考虑P2,其他情况不用处理
			if (map[row + 1][column] == GRASS || map[row + 1][column] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row + 1][column] == END ? MANDOWNONEND
						: MANDOWN;
				// 人变成temp,人往下走一步
				map[row + 1][column] = temp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				row++;

			}
		}
	}

	private void moveLeft() {
		// 左一位p1为WALL
		if (map[row][column - 1] == WALL)
			return;
		// 左一位p1为 BOX,BOXONEND
		if (map[row][column - 1] == BOX || map[row][column - 1] == BOXONEND) {
			// 左左一位p2为 END,GRASS则向左一步,其他不用处理
			if (map[row][column - 2] == END || map[row][column - 2] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row][column - 2] == END ? BOXONEND : BOX;
				byte manTemp = map[row][column - 1] == BOX ? MANLEFT
						: MANLEFTONEND;
				// 箱子变成boxTemp,箱子往左一步
				map[row][column - 2] = boxTemp;
				// 人变成manTemp,往左走一步
				map[row][column - 1] = manTemp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column--;
			}
		} else {
			// 左一位为 GRASS,END,其他情况不用处理
			if (map[row][column - 1] == GRASS || map[row][column - 1] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row][column - 1] == END ? MANLEFTONEND
						: MANLEFT;
				// 人变成temp,人往左走一步
				map[row][column - 1] = temp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column--;

			}
		}
	}

	private void moveRight() {
		// 右一位p1为WALL
		if (map[row][column + 1] == WALL)
			return;
		// 右一位为 BOX,BOXONEND
		if (map[row][column + 1] == BOX || map[row][column + 1] == BOXONEND) {
			// 右右一位p2为 END,GRASS则向右一步,其他不用处理
			if (map[row][column + 2] == END || map[row][column + 2] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row][column + 2] == END ? BOXONEND : BOX;
				byte manTemp = map[row][column + 1] == BOX ? MANRIGHT
						: MANRIGHTONEND;
				// 箱子变成boxTemp,箱子往右一步
				map[row][column + 2] = boxTemp;
				// 人变成manTemp,往右走一步
				map[row][column + 1] = manTemp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column++;
			}
		} else {
			// 右一位为 GRASS,END,其他情况不用处理
			if (map[row][column + 1] == GRASS || map[row][column + 1] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row][column + 1] == END ? MANRIGHTONEND
						: MANRIGHT;
				// 人变成temp,人往右走一步
				map[row][column + 1] = temp;
				// 人刚才站的地方变成 grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column++;

			}
		}
	}

	public boolean isFinished() {
		for (int i = 0; i < mapRow; i++)
			for (int j = 0; j < mapColumn; j++)
				if (map[i][j] == END || map[i][j] == MANDOWNONEND
						|| map[i][j] == MANUPONEND || map[i][j] == MANLEFTONEND
						|| map[i][j] == MANRIGHTONEND)
					return false;
		return true;
	}
	private Image iBuffer;
	private Graphics gBuffer;
	public void paint(Graphics g)// 绘图
	{
		//super.paint(g);		
		if(iBuffer==null)
		{
			iBuffer=createImage(this.getSize().width,this.getSize().height);
			gBuffer=iBuffer.getGraphics();
		}
		for (int i = 0; i < mapRow; i++)
			for (int j = 0; j < mapColumn; j++) {
				// 画出地图 i代表行数,j代表列数
				if (map[i][j] != 0)
					gBuffer.drawImage(pic[map[i][j]], leftX + j * 30, leftY + i * 30,
							this);
			}
		gBuffer.setColor(Color.RED);
		gBuffer.setFont(new Font("楷体_2312", Font.BOLD, 30));
		gBuffer.drawString("现在是第", 150, 140);
		gBuffer.drawString(String.valueOf(grade + 1), 310, 140);
		gBuffer.drawString("关", 360, 140);
		g.drawImage(iBuffer,0,0,this);
	}
	/*public void update(Graphics g)
	{
		paint(g);
	}*/

	public int getManX() {
		return row;
	}

	public int getManY() {
		return column;
	}

	public int getGrade() {
		return grade;
	}

	public byte[][] getMap(int grade) {
		return MapFactory.getMap(grade);
	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void keyPressed(KeyEvent e)// 键盘事件
	{
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			// 向上
			moveUp();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			// 向下
			moveDown();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) { // 向左
			moveLeft();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // 向右
			moveRight();
		}
		repaint();
		if (isFinished()) {
			// 禁用按键
			acceptKey = false;

			if (grade == 10) {
				JOptionPane.showMessageDialog(this, "恭喜您通过最后一关！！！");
			} else {
				// 提示进入下一关
				String msg = "恭喜您通过第" + grade + "关!!!\n是否要进入下一关？";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "过关";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg, title, type);
				if (choice == 1)
					System.exit(0);
				else if (choice == 0) {
					// 进入下一关
					acceptKey = true;
					nextGrade();
				}
			}
		}
	}

	public static void main(String[] args) {
		new GameFrame();
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getButton() == MouseEvent.BUTTON3) // 右键撤销移动
		{
			undo();// 撤销移动
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

class TimerTest extends JFrame implements ActionListener
{
	public JLabel jlTime =new JLabel();
	public Timer timer;
	public TimerTest()
	{
		setTitle("Timer测试");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(180,80);
		add(jlTime);
		timer=new Timer();
		timer.start();
		setVisible(true);
	}
	public void actionPerformed(ActionEvent e)
	{
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		jlTime.setText(format.format(date));
	}

}


class Sound// 播放背景音乐
{
	String path = new String("musics\\");
	String file = new String("nor.mid");
	Sequence seq;
	Sequencer midi;
	boolean sign;

	void loadSound() {
		try {
			seq = MidiSystem.getSequence(new File(path + file));
			midi = MidiSystem.getSequencer();
			midi.open();
			midi.setSequence(seq);
			midi.start();
			midi.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		sign = true;
	}

	void mystop() {
		midi.stop();
		midi.close();
		sign = false;
	}

	boolean isplay() {
		return sign;
	}

	void setMusic(String e) {
		file = e;
	}
}