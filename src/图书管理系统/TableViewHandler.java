package 图书管理系统;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class TableViewHandler {

	DatabaseHandler databaseHandler;
	
	private ArrayList<JTable> tables;
	private ArrayList<JPanel> panels;
	
	private ArrayList<TableChangedPlace> places;
	
	private JTable mainTable;
	
	JTabbedPane tabbedPane;
	JPanel mainPanel;
	
	int currentTabbedPandIndex;
	
	public TableViewHandler(DatabaseHandler databaseHandler, JTabbedPane tabbedPane){
		this.databaseHandler = databaseHandler;
		this.tabbedPane = tabbedPane;
		tabbedPane.addChangeListener(new onTabbedPaneChanged());
		
		tables = new ArrayList<JTable>();
		panels = new ArrayList<JPanel>();
		places = new ArrayList<TableChangedPlace>();
		
		currentTabbedPandIndex = 0;
		initializeTables();
	}

	public void initializeTables() {
		DatabaseResultAdapter adapter = databaseHandler.showTables();
		
		IDGenerator.sortings.clear();
		tabbedPane.removeAll();
		tables.clear();
		panels.clear();
		
		mainPanel = new JPanel(new BorderLayout());
		tabbedPane.add("总览", mainPanel);
		
		if(adapter.values.length != 0){
			String[][] sumValues = null;
			for(int i = 0;  i < adapter.values.length; i++){
				String tableName = adapter.values[i][0];
				DatabaseResultAdapter resultAdapter = databaseHandler.showResult(tableName);
			 	JTable table = new JTable(resultAdapter.values, resultAdapter.columnNames);
			 	addTable(tableName, table);
			 	if(sumValues == null){
			 		sumValues = resultAdapter.values;
			 	}else if(resultAdapter.values != null){
			 		sumValues = addString(sumValues, resultAdapter.values);
			 	}
			}
			mainTable = new JTable(sumValues, DatabaseInfo.columName);
			mainTable.getModel().addTableModelListener(new onTableChanged());
			mainPanel.removeAll();
			JScrollPane scrollPane = new JScrollPane(mainTable);
			mainPanel.add(scrollPane, BorderLayout.CENTER);
			mainPanel.validate();
			

			int amount = 0;
			int currentSelectedTable = currentTabbedPandIndex;
			if(currentSelectedTable == 0){
				amount = mainTable.getRowCount();
			}else{
				currentSelectedTable = currentSelectedTable - 1;
				amount = tables.get(currentSelectedTable).getRowCount();
			}
			MainView.setBookAmount(amount);
			
			tabbedPane.setSelectedIndex(currentTabbedPandIndex);
			currentTabbedPandIndex = 0;
		}
	}
	
	
	public void addTable(String name, JTable table){
		
		tables.add(table);
		table.getModel().addTableModelListener(new onTableChanged());
		
		JPanel panel = new JPanel(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);
		panels.add(panel);
		
		tabbedPane.addTab(name, panel);
		IDGenerator.sortings.add(name);
		
		
	}
	
	public void createTable(){
		String name = JOptionPane.showInputDialog(null, "输入要新建分类名称", "新建分类", JOptionPane.INFORMATION_MESSAGE);
		if (!name.equals("")) {
			DatabaseResultAdapter adapter = databaseHandler.creatTable(name);
			JTable table = new JTable(adapter.values, adapter.columnNames);
			addTable(name, table);
		}
	}
	
	public void dropTable(){
		if(tabbedPane.getSelectedIndex() != 0){
			String sorting = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
			if(JOptionPane.showConfirmDialog(null, "确定要删除分类\"" + sorting + "\"吗：",
					"警告", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
				databaseHandler.dropTable(sorting);
				initializeTables();
			}
		}
	}
	
	public void deleteRows(){
		int tableIndex = tabbedPane.getSelectedIndex();
		if(tableIndex == 0){
			int[] selectedRows = mainTable.getSelectedRows();
			for(int i = 0; i < selectedRows.length; i++){
				String id = (String) mainTable.getValueAt(selectedRows[i], 0);
				String sorting = (String) mainTable.getValueAt(selectedRows[i], 1);
				databaseHandler.deleteRows(sorting, id);
			}
			initializeTables();
		}else{
			tableIndex--;
			JTable table = tables.get(tableIndex);
			int[] selectedRows = table.getSelectedRows();
			for(int i = 0; i < selectedRows.length; i++){
				String id = (String) table.getValueAt(selectedRows[i], 0);
				String sorting = (String) table.getValueAt(selectedRows[i], 1);
				databaseHandler.deleteRows(sorting, id);
			}
			currentTabbedPandIndex = tableIndex + 1;
			initializeTables();
		}
	}
	
	public void submit(){
		int tableIndex = tabbedPane.getSelectedIndex();
		if(tableIndex != 0){
			currentTabbedPandIndex = tableIndex;
		}
		
		for(int i = 0; i < places.size(); i++){
			int row = places.get(i).row;
			int column = places.get(i).column;
			if(tableIndex == 0){
				String tableName = (String) mainTable.getValueAt(row, 1);
				String columnName = DatabaseInfo.columName[column];
				String id = (String) mainTable.getValueAt(row, 0);
				String value = (String) mainTable.getValueAt(row, column);
				databaseHandler.submit(tableName, columnName, value, id);
			}else{
				tableIndex--;
				JTable table = tables.get(tableIndex);
				String tableName = (String) table.getValueAt(row, 1);
				String columnName = DatabaseInfo.columName[column];
				String id = (String) table.getValueAt(row, 0);
				String value = (String) table.getValueAt(row, column);
				databaseHandler.submit(tableName, columnName, value, id);
			}
		}
		initializeTables();
	}
	
	public void setMainTable(DatabaseResultAdapter adapter){
		mainTable = new JTable(adapter.values, adapter.columnNames);
		mainTable.getModel().addTableModelListener(new onTableChanged());
		mainPanel.removeAll();
		JScrollPane scrollPane = new JScrollPane(mainTable);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.validate();
	}
	
	public String[][] addString(String[][] valuesA, String[][] valuesB){
		List<String[]> sumList = new ArrayList<String[]>(Arrays.<String[]>asList(valuesB));
		sumList.addAll(Arrays.<String[]>asList(valuesA));
		return sumList.toArray(valuesA);
	}
	
	class onTableChanged implements TableModelListener{

		@Override
		public void tableChanged(TableModelEvent e) {
			int row = e.getLastRow();
			int column = e.getColumn();
			places.add(new TableChangedPlace(row, column));
		}
		
	}
	
	class onTabbedPaneChanged implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			places.clear();
			
			try{
				//显示新页面的图书总量
				int currentSelectedTable = tabbedPane.getSelectedIndex();
				int amount = 0;
				if(currentSelectedTable != -1){
					if(currentSelectedTable == 0){
						amount = mainTable.getRowCount();
					}else{
						currentSelectedTable = currentSelectedTable - 1;
						amount = tables.get(currentSelectedTable).getRowCount();
					}
					MainView.setBookAmount(amount);
				}
			}catch (NullPointerException error) {
			}
		}
		
	}
}
