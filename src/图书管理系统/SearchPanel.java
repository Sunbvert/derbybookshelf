package 图书管理系统;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class SearchPanel  extends JPanel{

	JLabel labelTitle;
	
	JTable table1, table2, table3, table4, table5;
	String[] columnName;
	
	JLabel labelDisplay;
	
	Box comboBoxBox;
	JCheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6;
	JCheckBox checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12;
	JCheckBox[] checkBoxs = new JCheckBox[10];
	
	JRadioButton preciseSrarch;
	JRadioButton fuzzySearch;
	ButtonGroup btngSearchPrecision;
	
	JRadioButton ascending;
	JRadioButton descending;
	ButtonGroup btngSorting;
	
	JComboBox<String> comboBox;
	JComboBox<String> tableComboBox;
	
	JLabel labelRequirement;
	
	JTextField requirement;
	
	JButton btnSearch;
	
	SearchHandler searchHandler;
	
	TableViewHandler view;
	
	public SearchPanel(TableViewHandler view){
		this.view = view;
		searchHandler = new SearchHandler(view, this);
		
		setLayout(new BorderLayout());
		
		JPanel panel1 = new JPanel(new BorderLayout());
		labelTitle = new JLabel("查找图书");
		labelDisplay = new JLabel("选择要显示的信息");
		
		panel1.add(labelTitle, BorderLayout.NORTH);
		panel1.add(initTableBox(), BorderLayout.CENTER);
		panel1.add(labelDisplay, BorderLayout.SOUTH);
		
		JPanel panel2 = new JPanel(new BorderLayout());
		labelRequirement = new JLabel("输入搜索条件：");
		requirement = new JTextField();
		
		panel2.add(initCheckBox(), BorderLayout.NORTH);
		panel2.add(labelRequirement, BorderLayout.CENTER);
		panel2.add(requirement, BorderLayout.SOUTH);
		
		JPanel panel3 = new JPanel(new BorderLayout());
		btnSearch = new JButton("查找");
		btnSearch.addActionListener(new onBtnSearchClicked());
		Box blankBox1 = Box.createVerticalBox();
		blankBox1.add(Box.createVerticalStrut(2));
		Box blankBox2 = Box.createVerticalBox();
		blankBox2.add(Box.createVerticalStrut(2));
		
		panel3.add(blankBox1, BorderLayout.NORTH);
		panel3.add(btnSearch, BorderLayout.CENTER);
		panel3.add(blankBox2, BorderLayout.SOUTH);
		
		add(panel1, BorderLayout.NORTH);
		add(panel2, BorderLayout.CENTER);
		add(panel3, BorderLayout.SOUTH);
		
		btngSearchPrecision.setSelected(preciseSrarch.getModel(), true);
	}
	
	private Box initTableBox(){
		Box tableBox = Box.createVerticalBox();
		
		table1 = new JTable(new String[1][2], new String[]{DatabaseInfo.columName[0], DatabaseInfo.columName[1]});
		tableComboBox = new JComboBox<>();
		for(int i = 0; i < IDGenerator.sortings.size(); i++){
			tableComboBox.addItem(IDGenerator.sortings.get(i));
		}
		table1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(tableComboBox));
		
		table2 = new JTable(new String[1][2], new String[]{DatabaseInfo.columName[2], DatabaseInfo.columName[3]});
		table3 = new JTable(new String[1][2], new String[]{DatabaseInfo.columName[4], DatabaseInfo.columName[5]});
		table4 = new JTable(new String[1][2], new String[]{DatabaseInfo.columName[6], DatabaseInfo.columName[7]});
		table5 = new JTable(new String[1][2], new String[]{DatabaseInfo.columName[8], DatabaseInfo.columName[9]});
		
		tableBox.add(table1.getTableHeader());
		tableBox.add(table1);
		tableBox.add(table2.getTableHeader());
		tableBox.add(table2);
		tableBox.add(table3.getTableHeader());
		tableBox.add(table3);
		tableBox.add(table4.getTableHeader());
		tableBox.add(table4);
		tableBox.add(table5.getTableHeader());
		
		tableBox.add(table5);
		
		return tableBox;
	}
	
	private Box initCheckBox(){
		Box checkBox = Box.createVerticalBox();
		checkBox.add(Box.createVerticalStrut(2));
		checkBox.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		checkBox1 = new JCheckBox("全选");
		checkBox1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox1.isSelected()){
					for(int i = 0; i < checkBoxs.length; i++){
						checkBoxs[i].setSelected(true);
					}
					checkBox2.setSelected(false);
					searchHandler.selectedField = new ArrayList<>();
					Collections.addAll(searchHandler.selectedField, DatabaseInfo.columName);
					repaintComboBox();
				}
			}
		});
		
		checkBox2 = new JCheckBox("全不选");
		checkBox2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(checkBox2.isSelected()){
					for(int i = 0; i < checkBoxs.length; i++){
						checkBoxs[i].setSelected(false);
					}
					checkBox1.setSelected(false);
					searchHandler.selectedField = new ArrayList<>();
					repaintComboBox();
				}
			}
		});
		
		checkBox3 = new JCheckBox(DatabaseInfo.columName[0]);
		checkBox4 = new JCheckBox(DatabaseInfo.columName[1]);
		checkBox5 = new JCheckBox(DatabaseInfo.columName[2]);
		checkBox6 = new JCheckBox(DatabaseInfo.columName[3]);
		checkBox7 = new JCheckBox(DatabaseInfo.columName[4]);
		checkBox8 = new JCheckBox(DatabaseInfo.columName[5]);
		checkBox9 = new JCheckBox(DatabaseInfo.columName[6]);
		checkBox10 = new JCheckBox(DatabaseInfo.columName[7]);
		checkBox11 = new JCheckBox(DatabaseInfo.columName[8]);
		checkBox12 = new JCheckBox(DatabaseInfo.columName[9]);
		
		Box boxGroup1 = Box.createHorizontalBox();
		boxGroup1.add(checkBox1);
		boxGroup1.add(Box.createHorizontalGlue());
		boxGroup1.add(checkBox2);
		boxGroup1.add(Box.createHorizontalStrut(14));
		Box boxGroup2 = Box.createHorizontalBox();
		boxGroup2.add(checkBox3);
		boxGroup2.add(Box.createHorizontalGlue());
		boxGroup2.add(checkBox4);
		boxGroup2.add(Box.createHorizontalStrut(26));
		Box boxGroup3 = Box.createHorizontalBox();
		boxGroup3.add(checkBox5);
		boxGroup3.add(Box.createHorizontalGlue());
		boxGroup3.add(checkBox6);
		boxGroup3.add(Box.createHorizontalStrut(26));
		Box boxGroup4 = Box.createHorizontalBox();
		boxGroup4.add(checkBox7);
		boxGroup4.add(Box.createHorizontalGlue());
		boxGroup4.add(checkBox8);
		Box boxGroup5 = Box.createHorizontalBox();
		boxGroup5.add(checkBox9);
		boxGroup5.add(Box.createHorizontalGlue());
		boxGroup5.add(checkBox10);
		Box boxGroup6 = Box.createHorizontalBox();
		boxGroup6.add(checkBox11);
		boxGroup6.add(Box.createHorizontalGlue());
		boxGroup6.add(checkBox12);
		boxGroup6.add(Box.createHorizontalStrut(26));
		
		checkBox.add(boxGroup1);
		checkBox.add(boxGroup2);
		checkBox.add(boxGroup3);
		checkBox.add(boxGroup4);
		checkBox.add(boxGroup5);
		checkBox.add(boxGroup6);
		
		checkBoxs[0] = checkBox3;
		checkBoxs[1] = checkBox4;
		checkBoxs[2] = checkBox5;
		checkBoxs[3] = checkBox6;
		checkBoxs[4] = checkBox7;
		checkBoxs[5] = checkBox8;
		checkBoxs[6] = checkBox9;
		checkBoxs[7] = checkBox10;
		checkBoxs[8] = checkBox11;
		checkBoxs[9] = checkBox12;
		
		for(int i = 0; i < checkBoxs.length; i++){
			int j = i;
			checkBoxs[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(((JCheckBox) e.getSource()).isSelected()){
						searchHandler.selectedField.add(DatabaseInfo.columName[j]);
					}else{
						searchHandler.selectedField.remove(DatabaseInfo.columName[j]);
					}
					repaintComboBox();
				}
			});
		}
		
		checkBox.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		Box searchPrecisionBox = Box.createHorizontalBox();
		preciseSrarch = new JRadioButton("精确查找");
		fuzzySearch = new JRadioButton("模糊查找");
		 btngSearchPrecision = new ButtonGroup();
		btngSearchPrecision.add(preciseSrarch);
		btngSearchPrecision.add(fuzzySearch);
		searchPrecisionBox.add(preciseSrarch);
		searchPrecisionBox.add(fuzzySearch);
		checkBox.add(searchPrecisionBox);
		
		Box sortingBox = Box.createHorizontalBox();
		ascending = new JRadioButton("升序");
		descending = new JRadioButton("降序");
		btngSorting = new ButtonGroup();
		btngSorting.add(ascending);
		btngSorting.add(descending);
		sortingBox.add(ascending);
		sortingBox.add(Box.createHorizontalGlue());
		sortingBox.add(descending);
		sortingBox.add(Box.createHorizontalStrut(26));
		checkBox.add(sortingBox);
		
		comboBox = new JComboBox<>();
		comboBoxBox = Box.createHorizontalBox();
		comboBoxBox.add(comboBox);
		checkBox.add(comboBoxBox);
		
		return checkBox;
	}
	
	private void repaintComboBox(){
		String[] checks = new String[searchHandler.selectedField.size()];
		for(int i = 0; i < searchHandler.selectedField.size(); i++){
			checks[i] = searchHandler.selectedField.get(i);
		}
		comboBoxBox.remove(comboBox);
		comboBox = new JComboBox<>(checks);
		comboBoxBox.add(comboBox);
		comboBoxBox.validate();
		comboBoxBox.repaint();
	}
	
	public void repaintTableComboBox(){
		tableComboBox = new JComboBox<>();
		for(int i = 0; i < IDGenerator.sortings.size(); i++){
			tableComboBox.addItem(IDGenerator.sortings.get(i));
		}
		table1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(tableComboBox));
	}
	
	class onBtnSearchClicked implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			searchHandler.search();
			view.tabbedPane.setSelectedIndex(0);
			clearSelection();
		}
		
	}

	public void addValuesToFields(){
		searchHandler.field[0] = (String) table1.getValueAt(0, 0);
		searchHandler.field[1] = (String) table1.getValueAt(0, 1);
		searchHandler.field[2] = (String) table2.getValueAt(0, 0);
		searchHandler.field[3] = (String) table2.getValueAt(0, 1);
		searchHandler.field[4] = (String) table3.getValueAt(0, 0);
		searchHandler.field[5] = (String) table3.getValueAt(0, 1);
		searchHandler.field[6] = (String) table4.getValueAt(0, 0);
		searchHandler.field[7] = (String) table4.getValueAt(0, 1);
		searchHandler.field[8] = (String) table5.getValueAt(0, 0);
		searchHandler.field[9] = (String) table5.getValueAt(0, 1);
	}
	
	private void clearSelection(){
		searchHandler.selectedField.clear();
		searchHandler.changedField.clear();
		searchHandler.field = new String[10];
		
		for(int i = 0; i < checkBoxs.length; i++){
			checkBoxs[i].setSelected(false);
			checkBox1.setSelected(false);
			checkBox2.setSelected(false);
		}
		
		table1.setValueAt(null, 0, 0);
		table1.setValueAt(null, 0, 1);
		table2.setValueAt(null, 0, 0);
		table2.setValueAt(null, 0, 1);
		table3.setValueAt(null, 0, 0);
		table3.setValueAt(null, 0, 1);
		table4.setValueAt(null, 0, 0);
		table4.setValueAt(null, 0, 1);
		table5.setValueAt(null, 0, 0);
		table5.setValueAt(null, 0, 1);
		
		requirement.setText(null);
		
		btngSearchPrecision.setSelected(preciseSrarch.getModel(), true);
		btngSorting.clearSelection();
		repaintComboBox();
	}
}
