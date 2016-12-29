package xhs.project.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import xhs.tools.mina.handler.ServerHandler;
import xhs.tools.mina.handler.ServerListener;

public class WindowShow {

	// ����ע��
	public final static boolean testSysout = true;
	private JFrame frame;
	public JButton btStart;// ����������
	public JButton btClear;// �����ʾ
	public static JTextArea taShow;
	// public JTextField tfSend;//��Ҫ���͵��ı���Ϣ
	// public JButton btSend;//������Ϣ��ť
	public List<Socket> clients;// �������ӵ��������Ŀͻ���
	private JScrollPane scrollPane;
	private JPanel Jpanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowShow window = new WindowShow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WindowShow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {
		frame = new JFrame("�������");
		// Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();
		// // �����ʾ����С����
		// Dimension frameSize = frame.getSize(); // ��ô��ڴ�С����
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ����仰���ڲ��ܹص�
		// frame.getContentPane().setLayout(null);
		// tfSend = new JTextField();
		// tfSend.setBounds(10, 225, 237, 27);
		// tfSend.setColumns(20);
		// frame.getContentPane().add(tfSend);
		btClear = new JButton("\u53D1\u9001");
		btClear.setText("���");
		// btSend.setBounds(256, 227, 77, 23);
		// frame.getContentPane().add(btSend);

		btStart = new JButton("\u76D1\u542C");
		btStart.setText("��ʼ����");
		// btStart.setBounds(343, 225, 77, 23);
		// frame.getContentPane().add(btStart);

		taShow = new JTextArea();
		taShow.setEditable(false);
		// taShow.setBounds(27, 10, 393, 206);
		// frame.getContentPane().add(taShow);

		Jpanel = new JPanel(new FlowLayout());
		// Jpanel.add(tfSend);
		Jpanel.add(btClear);
		Jpanel.add(btStart);

		scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportView(taShow); // this ȥ��Ҳ����

		frame.setSize(500, 400);
		frame.getContentPane().add(Jpanel, BorderLayout.SOUTH);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// frame.setLocation(100, 200);

		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ServerListener();
				// new UserServer();
				// new DeviceServer();

			}
		});
		// btSend.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// server.sendMsg(tfSend.getText());
		// tfSend.setText("");
		// }
		// });
		btClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				taShow.setText("");
				WindowShow.println("sersion������"+ServerHandler.size);

			}

		});
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "ȷ���ر���", "��ܰ��ʾ",
						JOptionPane.YES_NO_OPTION);
				if (a == 1) {
					// server.closeServer();
					System.exit(0); // �ر�
				}
			}
		});
	}

	public static void println(String s) {
		if (s != null) {
			taShow.setText(taShow.getText() + s + "\n");
		}

	}

	public static void print(String s, byte[] data) {
		if (s != null) {
			taShow.setText(taShow.getText() + s);
			for (int i = 0; i < data.length; i++) {
				taShow.setText(taShow.getText() + data[i] + ",");
				if (i == (data.length - 1)) {
					taShow.setText(taShow.getText() + "\n");
				}
			}

		}

	}
}
