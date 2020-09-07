//Java RPG 游戏开发中地图的初步构建
//文件：Example1.Java

import java.awt.Container;
import javax.swing.JFrame;

public class planeFrame extends JFrame {
	public planeFrame() {
		// 默认的窗体名称
		setTitle("飞行射击类游戏");
		// 获得我们自定义面板[地图面板]的实例
		GamePanel panel = new GamePanel();
		Container contentPane = getContentPane();
		contentPane.add(panel);
		// 执行并构建窗体设定
		pack();
	}
	public static void main(String[] args) {
		planeFrame e1 = new planeFrame();
		// 设定允许窗体关闭操作
		e1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 显示窗体
		e1.setVisible(true);
	}
}
