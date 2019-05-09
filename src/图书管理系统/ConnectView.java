package 图书管理系统;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConnectView extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	public ConnectView() {
		setTitle("连接数据库");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(280, 120);
		setLocationRelativeTo(getOwner());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("请输入要连接或新建的数据库：");
		lblNewLabel.setFont(new Font("微软雅黑", Font.PLAIN, 15));
		contentPane.add(lblNewLabel, BorderLayout.NORTH);
		
		textField = new JTextField();
		contentPane.add(textField, BorderLayout.CENTER);
		textField.setColumns(10);
		
		Box horizontalBox = Box.createHorizontalBox();
		contentPane.add(horizontalBox, BorderLayout.SOUTH);
		
		Component horizontalStrut = Box.createHorizontalStrut(60);
		horizontalBox.add(horizontalStrut);
		
		JButton btnNewButton = new JButton("确认");
		btnNewButton.addActionListener(new onBtnConfirmClicked());
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		horizontalBox.add(btnNewButton);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		horizontalBox.add(horizontalGlue);
		
		JButton btnNewButton_1 = new JButton("取消");
		btnNewButton_1.addActionListener(new onBtnCancelClicked());
		horizontalBox.add(btnNewButton_1);
		
		Component horizontalStrut_1 = Box.createHorizontalStrut(60);
		horizontalBox.add(horizontalStrut_1);
		
		setVisible(true);
	}
	
	class onBtnConfirmClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String databaseName = textField.getText().trim();
			MainView mainView = new MainView(databaseName);
			dispose();
		}
		
	}
	
	class onBtnCancelClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}

}
