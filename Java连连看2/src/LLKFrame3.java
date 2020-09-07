import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

//��JFrameʵ��������V2.0��
//����ͳ����ȥ����ĸ������ܣ�
//2012-7-07 18:12

public class LLKFrame3 extends JFrame {

	// ��ʾ����ȥ��������,����������������Զ���Ϊstatic
	static JTextField textarea1 = new JTextField(10);
	JPanel panel1 = new JPanel();
	LLKPanel centerPanel;

	public LLKFrame3() {
		JLabel label1 = new JLabel("����ȥ��������:");

		JButton exitButton, newlyButton;
		newlyButton = new JButton("����һ��");
		exitButton = new JButton("�˳�");
		this.setLayout(new BorderLayout());
		panel1.setLayout(new FlowLayout());
		panel1.add(label1);
		panel1.add(textarea1);

		panel1.add(newlyButton);
		panel1.add(exitButton);
		textarea1.setEditable(false);
		textarea1.setText(Integer.toString(0));// ��ʾ����ȥ��������
		Container contentPane = getContentPane();
		contentPane.add(panel1, BorderLayout.NORTH);
		centerPanel = new LLKPanel();
		contentPane.add(centerPanel, BorderLayout.CENTER);
		this.setBounds(280, 100, 640, 660); // 500 450
		this.setVisible(true);
		this.setFocusable(true);
		// ����¼�����
		exitButton.addMouseListener(new MouseAdapter() {// �˳�
					@Override
					public void mouseClicked(MouseEvent e) {
						System.exit(0);
					}
				});
		// ����¼�����
		newlyButton.addMouseListener(new MouseAdapter() {// ����һ��
					@Override
					public void mouseClicked(MouseEvent e) {
						textarea1.setText(Integer.toString(0));// ��ʾ����ȥ��������
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


