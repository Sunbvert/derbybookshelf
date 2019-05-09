package 图书管理系统;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class AddBookFrame extends JFrame implements TableModelListener, ActionListener{

	JScrollPane scrollPane;
	JTable table;
	DefaultTableModel model;
	String[] blankRow;
	
	JButton btnConfirm;
	JButton btnCancel;
	
	JComboBox<String> comboBox;
	
	TableViewHandler tableViewHandler;
	
	public AddBookFrame(TableViewHandler tableViewHandler){
		this.tableViewHandler = tableViewHandler;
		
	 	String[][] values = null;
		blankRow = null;
		model = new DefaultTableModel(values, DatabaseInfo.hintColumnName);
		model.setRowCount(DatabaseInfo.DefaultAddBookTableRowAcount);
		table = new JTable(model);
		model.addTableModelListener(this);
		table.setRowHeight(DatabaseInfo.DefaultAddBookTableRowHeight);
		
		//add ComboBox to table
		comboBox = new JComboBox<>();
		for(int i = 0; i < IDGenerator.sortings.size(); i++){
			comboBox.addItem(IDGenerator.sortings.get(i));
		}
		table.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboBox));
		
		scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		
		btnConfirm = new JButton("    添加    ");
		btnConfirm.addActionListener(new onBtnConfirmClicked());
		btnCancel = new JButton("    取消    ");
		btnCancel.addActionListener(this);
		
		JPanel panel = new JPanel(new FlowLayout());
		Box box = Box.createHorizontalBox();
		box.add(btnConfirm);
		box.add(Box.createHorizontalStrut(20));
		box.add(btnCancel);
		panel.add(box);
		add(panel, BorderLayout.SOUTH);
		
		setSize(1200, 500);
		setLocationRelativeTo(getOwner());
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	//逐个读取表格中的数据，生成编号，然后一行一行的将数据上传到数据库，当遇到没有填写的表格时终止，并放弃正在检索的这一行。
	private void add(){
		int i = 0;
		int m = 0;
		boolean move = true;
		while(move){
			String[] values = new String[10];
			String sorting = (String) model.getValueAt(i, 0);
			if(sorting == null){
				break;
			}else{
				values[0] = IDGenerator.generateId(sorting);
				for(int j = 0; j < 9; j++){
					String value = (String) model.getValueAt(i, j);
					if(value == null && (j == 0 || j == 1)){
						move = false;
						break;
					}else{
						values[j + 1] = value;
					}
				}
				if (move) {
					MainView.databaseHandler.insertValues(sorting, values);
				}else{
					JOptionPane.showMessageDialog(this, "最后一行出现错误", "添加出现错误", JOptionPane.WARNING_MESSAGE);
					m++;
				}
				i++;
			}
		}
		JOptionPane.showMessageDialog(this, "成功添加" + (i - m) + 
				"行", "添加成功", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getFirstRow() == model.getRowCount() - 2){
			model.addRow(new String[9]);
			table.validate();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
	
	class onBtnConfirmClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			add();
			tableViewHandler.initializeTables();  //重新加载所有表格
			tableViewHandler.tabbedPane.setSelectedIndex(0);
			dispose();
		}
		
	}
}
