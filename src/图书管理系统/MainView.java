package 图书管理系统;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainView extends JFrame{

	public static DatabaseHandler databaseHandler;
	
	private TableViewHandler tableViewHandler;
	private JTabbedPane tabbedPane;
	
	SearchPanel searchPanel;
	
	private static JLabel bookAmount;
	
	public MainView(String databaseName){
		tabbedPane = new JTabbedPane();
		bookAmount = new JLabel();
		
		//TODO: 加入登陆界面
		this.databaseHandler = new DatabaseHandler(databaseName, "");
		this.tableViewHandler = new TableViewHandler(databaseHandler, tabbedPane);
		
		initMainView();
		
		setVisible(true);
		setSize(1300, 850);
		setLocationRelativeTo(getOwner());
		setTitle("图书管理系统");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void initMainView(){
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelOperations = new JPanel(new BorderLayout());
		JPanel panelOpNorth = new JPanel(new BorderLayout());
		JPanel panelOpCenter = new JPanel(new BorderLayout());
		
		JMenuBar menuBar = new JMenuBar();
		onMenuItemClicked menuItemListener = new onMenuItemClicked();
		JMenu menuSorting = new JMenu("分类");
		JMenuItem menuItem1 = new JMenuItem("显示所有图书");
		JMenuItem menuItem2 = new JMenuItem("更改分类名称");
		JMenuItem menuItem3 = new JMenuItem("新建分类");
		JMenuItem menuItem4 = new JMenuItem("删除此分类");
		menuItem1.addActionListener(menuItemListener);
		menuItem2.addActionListener(menuItemListener);
		menuItem3.addActionListener(menuItemListener);
		menuItem4.addActionListener(menuItemListener);
		menuSorting.add(menuItem1);
		//menuSorting.add(menuItem2);
		menuSorting.add(menuItem3);
		menuSorting.add(menuItem4);
		menuBar.add(menuSorting);
		panelOpNorth.add(menuBar, BorderLayout.NORTH);
		
		JButton btnSubmit = new JButton("确认更改");
		panelOpNorth.add(btnSubmit, BorderLayout.CENTER);
		btnSubmit.addActionListener(new onBtnSubmitClicked());
		
		JButton btnAdd = new JButton("添加图书");
		panelOpNorth.add(btnAdd, BorderLayout.SOUTH);
		btnAdd.addActionListener(new onBtnAddClicked());
		
		JButton btnDelete = new JButton("删除选中图书");
		panelOpCenter.add(btnDelete, BorderLayout.NORTH);
		btnDelete.addActionListener(new onBtnDeleteClicked());

		searchPanel = new SearchPanel(tableViewHandler);
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(searchPanel, BorderLayout.CENTER);
		bottomPanel.add(bookAmount, BorderLayout.SOUTH);
		
		panelOpCenter.add(bottomPanel, BorderLayout.SOUTH);
		
		panelOperations.add(panelOpNorth, BorderLayout.NORTH);
		panelOperations.add(panelOpCenter, BorderLayout.CENTER);
		
		
		add(panelOperations, BorderLayout.EAST);
	}
	
	public static void setBookAmount(int amount){
		bookAmount.setText("当前页面的图书总数：" + amount);
		bookAmount.setForeground(Color.blue);
	}
	
	
	class onMenuItemClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("显示所有图书")) {
				tableViewHandler.initializeTables();
				tableViewHandler.tabbedPane.setSelectedIndex(0);
			}else if (e.getActionCommand().equals("新建分类")) {
				tableViewHandler.createTable();
				searchPanel.repaintTableComboBox();
			}else if (e.getActionCommand().equals("删除此分类")) {
				tableViewHandler.dropTable();
				searchPanel.repaintTableComboBox();
			}
		}
		
	}
	
	
	class onBtnAddClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			AddBookFrame addBookFrame = new AddBookFrame(tableViewHandler);
		}
		
	}
	
	
	class onBtnDeleteClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			tableViewHandler.deleteRows();
		}
		
	}
	
	class onBtnSubmitClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			tableViewHandler.submit();
		}
		
	}
}
