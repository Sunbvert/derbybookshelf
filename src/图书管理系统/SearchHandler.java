package 图书管理系统;

import java.util.ArrayList;

import javax.swing.JTable;

public class SearchHandler {

	ArrayList<String> selectedField;
	ArrayList<String> changedField;
	String[] field;
	
	TableViewHandler viewHandler;
	SearchPanel panel;
	
	boolean like = false;
	
	public SearchHandler(TableViewHandler viewHandler, SearchPanel panel){
		this.viewHandler = viewHandler;
		this.panel = panel;
		
		field = new String[10];
		
		selectedField = new ArrayList<String>();
		changedField = new ArrayList<String>();
	}
	
	public void search(){
		panel.addValuesToFields();
		String tableName = field[1];
		String selectStatement = selectStatement();
		String conditionStatement = conditionStatement();
		String sortingStatement = sortingStatement();
		if(tableName != null){
			String statement = "select " + selectStatement + " from " +
					tableName + " " + conditionStatement + " " + sortingStatement;
			DatabaseResultAdapter adapter = MainView.databaseHandler.executeQuery(statement);
			viewHandler.setMainTable(adapter);
		}else{
			String statement = "select " + selectStatement + " from view1 "
					+ conditionStatement + " " + sortingStatement;
			DatabaseResultAdapter adapter = MainView.databaseHandler.unionAllTables(statement);
			viewHandler.setMainTable(adapter);
		}
	}
	
	private String selectStatement(){
		String statement = "";
		
		int selectNum = selectedField.size();
		if(selectNum > 1){
			for(int i = 0; i < selectNum - 1; i++){
				statement += selectedField.get(i) + ",";
			}
			statement += selectedField.get(selectNum - 1);
		}else if(selectNum == 1){
			statement = selectedField.get(0);
		}else if (selectNum == 0) {
			statement = "*";
		}
		
		return statement;
	}
	
	private String conditionStatement(){
		String statement = "";
		
		if(panel.btngSearchPrecision.isSelected(panel.fuzzySearch.getModel())){
			like = true;
		}
		
		for(int i = 0; i < DatabaseInfo.columName.length; i++){
			String changedValue = field[i];
			if(i != 1){
				if (changedValue != null && !changedValue.equals("")) {
					if(like && (i == 2 || i == 3 || i == 4 || i == 8 || i == 9)){
						field[i] = DatabaseInfo.columName[i] + " LIKE " + "'%" + changedValue + "%'";
						changedField.add(field[i]);
					}else if(i != 6){
						field[i] = DatabaseInfo.columName[i] + "=" + "'" + changedValue + "'";
						changedField.add(field[i]);
					}else {
						field[i] = DatabaseInfo.columName[i] + "=" + changedValue;
						changedField.add(field[i]);
					}
				}
			}
		}
	
		int j = changedField.size();
				
		if (j != 0) {
			statement = "where ";
		}
		for(int i = 0; i < j - 1; i++){
			statement += changedField.get(i) + " and ";
		}
		if(j != 0){
			statement += changedField.get(j - 1);
		}
		
		String text = panel.requirement.getText();
		if(!text.equals("")){
			if ( selectedField.size() != 0 && j != 0) {
				statement += " and " + text;
			}else{
				statement += "where " + text;
			}
		}
		return statement;
	}
	
	private String sortingStatement(){
		String statement = "";
		if(panel.btngSorting.isSelected(panel.ascending.getModel())){
			statement = "order by " + (String) panel.comboBox.getSelectedItem() + " ASC";
		}else if (panel.btngSorting.isSelected(panel.descending.getModel())) {
			statement = "order by " + (String) panel.comboBox.getSelectedItem() + " DESC";
		}
		return statement;
	}
	
}
