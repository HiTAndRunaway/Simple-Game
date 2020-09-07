//6-18���
//�����Ӵ����ְ�
//�Ҽ�����--���幦��
//��ʱ1��40����

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

public class GameFrame extends JFrame implements ActionListener, MouseListener// �������
		, KeyListener {
	private int grade = 0;
	// row,column�����˵��к� �к�
	// leftX,leftY �������Ͻ�ͼƬ��λ�� ����ͼƬ��(0,0)���꿪ʼ
	private int row = 7, column = 7, leftX = 0, leftY = 0;
	// ���ص�ͼ��������
	private int mapRow = 0, mapColumn = 0;
	// width,height ������Ļ�Ĵ�С
	private int width = 0, height = 0;
	private boolean acceptKey = true;
	// �������õ���ͼƬ
	private Image pic[] = null;
	// ����һЩ��������Ӧ��ͼ��Ԫ��
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

	/* ��ʾ��ʾ��Ϣ�Ի��� */
	public void DisplayToast(String str) {
		JOptionPane.showMessageDialog(null, str, "��ʾ",
				JOptionPane.ERROR_MESSAGE);
	}

	// �����ƶ�
	public void undo() {
		if (acceptKey) {
			// ����
			if (list.size() > 0) {
				// ��Ҫ���� �����߹�
				Map priorMap = (Map) list.get(list.size() - 1);
				map = priorMap.getMap();
				row = priorMap.getManX();
				column = priorMap.getManY();
				repaint();
				list.remove(list.size() - 1);
			} else
				DisplayToast("�����ٳ�����");
		} else {
			DisplayToast("�˹�����ɣ����ܳ�����");
		}
	}

	public void nextGrade() {
		// grade++;
		if (grade >= MapFactory.getCount() - 1) {
			DisplayToast("��ϲ��������йؿ���");
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
	 * //ԭ���Ǵ������ȡ 2012��4��20 public byte[][] getMap(int grade) { return
	 * MapFactory.getMap(grade); }
	 */

	public GameFrame() {
		super("��������Ϸ�����ְ�");
		setSize(600, 600);
		setVisible(true);
		setResizable(false);
		setLocation(300, 20);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container cont = getContentPane();
		cont.setLayout(null);
		cont.setBackground(Color.black);
		// ���ʼ13��ͼƬ
		getPic();
		width = this.getWidth();
		height = this.getHeight();
		this.setFocusable(true);
		initMap();
		// ���췽��ִ��ʱ�����������лָ���Ϸ
		// �ؿ��л�ʱ����initMap()
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
		// ��һλp1ΪWALL
		if (map[row - 1][column] == WALL)
			return;
		// ��һλp1Ϊ BOX,BOXONEND,�뿼��P2
		if (map[row - 1][column] == BOX || map[row - 1][column] == BOXONEND) {
			// ����һλp2Ϊ END,GRASS������һ��,�������ô���
			if (map[row - 2][column] == END || map[row - 2][column] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row - 2][column] == END ? BOXONEND : BOX;
				byte manTemp = map[row - 1][column] == BOX ? MANUP : MANUPONEND;
				// ���ӱ��temp,������ǰһ��
				map[row - 2][column] = boxTemp;
				// �˱��MANUP,������һ��
				map[row - 1][column] = manTemp;
				// �˸ղ�վ�ĵط����GRASS����END
				map[row][column] = grassOrEnd(map[row][column]);
				// ���뿪���޸��˵�����
				row--;
			}
		} else {
			// ��һλΪ GRASS,END,���뿼��P2������������ô���
			if (map[row - 1][column] == GRASS || map[row - 1][column] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row - 1][column] == END ? MANUPONEND : MANUP;
				// �˱��temp,��������һ��
				map[row - 1][column] = temp;
				// �˸ղ�վ�ĵط����GRASS����END
				map[row][column] = grassOrEnd(map[row][column]);
				// ���뿪���޸��˵�����
				row--;
			}
		}
	}

	private void moveDown() {
		// ��һλp1ΪWALL
		if (map[row + 1][column] == WALL)
			return;
		// ��һλp1Ϊ BOX,BOXONEND,�뿼��P2
		if (map[row + 1][column] == BOX || map[row + 1][column] == BOXONEND) {
			// ����һλp2Ϊ END,GRASS������һ��,�������ô���
			if (map[row + 2][column] == END || map[row + 2][column] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row + 2][column] == END ? BOXONEND : BOX;
				byte manTemp = map[row + 1][column] == BOX ? MANDOWN
						: MANDOWNONEND;
				// ���ӱ��boxTemp,��������һ��
				map[row + 2][column] = boxTemp;
				// �˱��manTemp,������һ��
				map[row + 1][column] = manTemp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				row++;
			}

		} else {
			// ��һλΪ GRASS,END,���뿼��P2,����������ô���
			if (map[row + 1][column] == GRASS || map[row + 1][column] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row + 1][column] == END ? MANDOWNONEND
						: MANDOWN;
				// �˱��temp,��������һ��
				map[row + 1][column] = temp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				row++;

			}
		}
	}

	private void moveLeft() {
		// ��һλp1ΪWALL
		if (map[row][column - 1] == WALL)
			return;
		// ��һλp1Ϊ BOX,BOXONEND
		if (map[row][column - 1] == BOX || map[row][column - 1] == BOXONEND) {
			// ����һλp2Ϊ END,GRASS������һ��,�������ô���
			if (map[row][column - 2] == END || map[row][column - 2] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row][column - 2] == END ? BOXONEND : BOX;
				byte manTemp = map[row][column - 1] == BOX ? MANLEFT
						: MANLEFTONEND;
				// ���ӱ��boxTemp,��������һ��
				map[row][column - 2] = boxTemp;
				// �˱��manTemp,������һ��
				map[row][column - 1] = manTemp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column--;
			}
		} else {
			// ��һλΪ GRASS,END,����������ô���
			if (map[row][column - 1] == GRASS || map[row][column - 1] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row][column - 1] == END ? MANLEFTONEND
						: MANLEFT;
				// �˱��temp,��������һ��
				map[row][column - 1] = temp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column--;

			}
		}
	}

	private void moveRight() {
		// ��һλp1ΪWALL
		if (map[row][column + 1] == WALL)
			return;
		// ��һλΪ BOX,BOXONEND
		if (map[row][column + 1] == BOX || map[row][column + 1] == BOXONEND) {
			// ����һλp2Ϊ END,GRASS������һ��,�������ô���
			if (map[row][column + 2] == END || map[row][column + 2] == GRASS) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte boxTemp = map[row][column + 2] == END ? BOXONEND : BOX;
				byte manTemp = map[row][column + 1] == BOX ? MANRIGHT
						: MANRIGHTONEND;
				// ���ӱ��boxTemp,��������һ��
				map[row][column + 2] = boxTemp;
				// �˱��manTemp,������һ��
				map[row][column + 1] = manTemp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
				map[row][column] = grassOrEnd(map[row][column]);
				column++;
			}
		} else {
			// ��һλΪ GRASS,END,����������ô���
			if (map[row][column + 1] == GRASS || map[row][column + 1] == END) {
				Map currMap = new Map(row, column, map);
				list.add(currMap);
				byte temp = map[row][column + 1] == END ? MANRIGHTONEND
						: MANRIGHT;
				// �˱��temp,��������һ��
				map[row][column + 1] = temp;
				// �˸ղ�վ�ĵط���� grassOrEnd(map[row][column])
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
	public void paint(Graphics g)// ��ͼ
	{
		//super.paint(g);		
		if(iBuffer==null)
		{
			iBuffer=createImage(this.getSize().width,this.getSize().height);
			gBuffer=iBuffer.getGraphics();
		}
		for (int i = 0; i < mapRow; i++)
			for (int j = 0; j < mapColumn; j++) {
				// ������ͼ i��������,j��������
				if (map[i][j] != 0)
					gBuffer.drawImage(pic[map[i][j]], leftX + j * 30, leftY + i * 30,
							this);
			}
		gBuffer.setColor(Color.RED);
		gBuffer.setFont(new Font("����_2312", Font.BOLD, 30));
		gBuffer.drawString("�����ǵ�", 150, 140);
		gBuffer.drawString(String.valueOf(grade + 1), 310, 140);
		gBuffer.drawString("��", 360, 140);
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

	public void keyPressed(KeyEvent e)// �����¼�
	{
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			// ����
			moveUp();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			// ����
			moveDown();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) { // ����
			moveLeft();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) { // ����
			moveRight();
		}
		repaint();
		if (isFinished()) {
			// ���ð���
			acceptKey = false;

			if (grade == 10) {
				JOptionPane.showMessageDialog(this, "��ϲ��ͨ�����һ�أ�����");
			} else {
				// ��ʾ������һ��
				String msg = "��ϲ��ͨ����" + grade + "��!!!\n�Ƿ�Ҫ������һ�أ�";
				int type = JOptionPane.YES_NO_OPTION;
				String title = "����";
				int choice = 0;
				choice = JOptionPane.showConfirmDialog(null, msg, title, type);
				if (choice == 1)
					System.exit(0);
				else if (choice == 0) {
					// ������һ��
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
		if (e.getButton() == MouseEvent.BUTTON3) // �Ҽ������ƶ�
		{
			undo();// �����ƶ�
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
		setTitle("Timer����");
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


class Sound// ���ű�������
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