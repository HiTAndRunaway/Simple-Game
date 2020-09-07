import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

//单JFrame实现连连看V2.0版
//具有统计消去方块的个数功能，
//2012-7-07 18:12

public class LLKFrame3 extends JFrame {

	// 显示已消去方块数量,由于其他类访问所以定义为static
	static JTextField textarea1 = new JTextField(10);
	JPanel panel1 = new JPanel();
	LLKPanel centerPanel;

	public LLKFrame3() {
		JLabel label1 = new JLabel("已消去方块数量:");

		JButton exitButton, newlyButton;
		newlyButton = new JButton("重来一局");
		exitButton = new JButton("退出");
		this.setLayout(new BorderLayout());
		panel1.setLayout(new FlowLayout());
		panel1.add(label1);
		panel1.add(textarea1);

		panel1.add(newlyButton);
		panel1.add(exitButton);
		textarea1.setEditable(false);
		textarea1.setText(Integer.toString(0));// 显示已消去方块数量
		Container contentPane = getContentPane();
		contentPane.add(panel1, BorderLayout.NORTH);
		centerPanel = new LLKPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		this.setBounds(280, 100, 640, 660); // 500 450
		this.setVisible(true);
		this.setFocusable(true);
		// 鼠标事件监听
		exitButton.addMouseListener(new MouseAdapter() {// 退出
					@Override
					public void mouseClicked(MouseEvent e) {
						System.exit(0);
					}
				});
		// 鼠标事件监听
		newlyButton.addMouseListener(new MouseAdapter() {// 重来一局
					@Override
					public void mouseClicked(MouseEvent e) {
						textarea1.setText(Integer.toString(0));// 显示已消去方块数量
						centerPanel.StartNewGame();
						centerPanel.Init_Graphic();
					}
				});
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LLKFrame3 llk = new LLKFrame3();
	}
}


