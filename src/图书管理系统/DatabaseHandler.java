package 图书管理系统;

import java.sql.*;

import javax.swing.JOptionPane;

public class DatabaseHandler {

	String databaseName;
	String databasePath;
	
	public DatabaseHandler(String databaseName, String databasePath){
		this.databaseName = databaseName;
		this.databasePath = databasePath;
		
		try{
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	
	public DatabaseResultAdapter showTables(){
		String selectStatement = "select tablename from sys.systables where tabletype='T'";
		return executeQuery(selectStatement);
	}
	
	
	public DatabaseResultAdapter unionAllTables(String conditions){
		DatabaseResultAdapter adapter = new DatabaseResultAdapter();
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			
			String statement = "create view view1 as ";
			for(int i = 0; i < IDGenerator.sortings.size(); i++){
				if (i == 0){
					statement += "select * from " + IDGenerator.sortings.get(i);
				}else{
					statement += " union all select * from " + IDGenerator.sortings.get(i);
				}
			}
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepareStatement.executeUpdate();
			
			adapter = executeQuery(conditions);
			
			String statement2 = "drop view view1";
			PreparedStatement prepareStatement2 = connector.prepareStatement(statement2, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepareStatement2.executeUpdate();
			
			connector.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e + "加载失败", "错误", JOptionPane.WARNING_MESSAGE);
		}
		return adapter;
	}
	
	public DatabaseResultAdapter creatTable(String tableName){
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			//TODO 建表
			String statement = "create table " + tableName + " (" + 
					DatabaseInfo.columName[0] + " char(8) primary key, " +
					DatabaseInfo.columName[1] + " varchar(50), " +
					DatabaseInfo.columName[2] + " varchar(50), " +
					DatabaseInfo.columName[3] + " varchar(50), " +
					DatabaseInfo.columName[4] + " varchar(50), " +
					DatabaseInfo.columName[5] + " varchar(20), " +
					DatabaseInfo.columName[6] + " decimal(8,2), " +
					DatabaseInfo.columName[7] + " date, " +
					DatabaseInfo.columName[8] + " varchar(20), " +
					DatabaseInfo.columName[9] + " varchar(50))";
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepareStatement.executeUpdate();
			connector.close();
			return showResult(tableName);
		}
		catch (SQLException e) {
			//System.out.println(e);
			JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.WARNING_MESSAGE);
		}
		return null;
	}
	
	public void dropTable(String tableName){
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			String statement = "drop table " + tableName;
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepareStatement.executeUpdate();
			connector.close();
		}
		catch (SQLException e) {
			//System.out.println(e);
			JOptionPane.showMessageDialog(null, e + "删除失败", "错误", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	public void insertValues (String tableName, String[] values){
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			String statement = "insert into " + tableName + " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			for(int i = 0; i < values.length; i++){
				if (i != 6) {
					prepareStatement.setString(i + 1, values[i]);
				}else{
					if(values[i] == null){
						prepareStatement.setString(i + 1, null);
					}else{
						prepareStatement.setDouble(7, Double.parseDouble(values[6]));
					}
				}
			}
			
			prepareStatement.executeUpdate();
			connector.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	public void deleteRows(String tableName, String id){
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			String statement = "delete from " + tableName + " where " + DatabaseInfo.columName[0]
					+ "= '" + id + "'";
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			prepareStatement.executeUpdate();
			connector.close();
		}
		catch (SQLException e) {
			//System.out.println(e);
			JOptionPane.showMessageDialog(null,"删除失败" + e, "错误", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	public void submit(String tableName,String columnName, String value, String id){
		try{
			String uri = "jdbc:derby: " + databaseName + ";create=true";
			Connection connector = DriverManager.getConnection(uri);
			
			String statement;
			if(columnName == DatabaseInfo.columName[6]){
				statement = 
						"update " + tableName + " set " + columnName +
						"=" + value + " where " + DatabaseInfo.columName[0] +" ='" + id + "'";
			}else{
				statement = 
						"update " + tableName + " set " + columnName +
						"= '" + value + "' where " + DatabaseInfo.columName[0] +" ='" + id + "'";
			}
			
			PreparedStatement prepareStatement = connector.prepareStatement(statement, 
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			prepareStatement.executeUpdate();
			connector.close();
		}
		catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "更新失败" + e, "错误", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	public DatabaseResultAdapter showResult(String tableName){
		String selectStatement = "select * from " + tableName + " ";	
		return executeQuery(selectStatement);
	}
	
	
	public DatabaseResultAdapter executeQuery(String statement){
		DatabaseResultAdapter adapter = new DatabaseResultAdapter();
		
		try{
			String uri = "jdbc:derby: " + databaseName + " ;create=true";
			Connection connector = DriverManager.getConnection(uri);
			
			PreparedStatement prepareStatement = connector.prepareStatement(statement,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = prepareStatement.executeQuery();
			
			ResultSetMetaData metaData = resultSet.getMetaData();
			int columnCount = metaData.getColumnCount();
			String[][] values;
			String[] columnNames;
			
			resultSet.last();
			int rowAmount = resultSet.getRow();
			values = new String[rowAmount][columnCount];
			columnNames = new String[columnCount];
			
			for (int i = 1; i <= columnCount; i++){
				columnNames[i - 1] = metaData.getColumnName(i);
			}
			
			int m = 0;
			resultSet.beforeFirst();
			while(resultSet.next()){
				for (int n = 1; n <= columnCount; n++){
					values[m][n - 1] = resultSet.getString(n);
				}
				m++;
			}
			adapter.values = values;
			adapter.columnNames = columnNames;
			
			connector.close();
		}
		catch (SQLException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "条件不合法" + e, "错误", JOptionPane.WARNING_MESSAGE);
		}
		
		return adapter;
	}
	
	public int getUsableId(String tableName){
		try{
			String uri = "jdbc:derby: " + databaseName + " ;create=true";
			Connection connector = DriverManager.getConnection(uri);
			String statement = "select 编号 from " + tableName;
			
			PreparedStatement prepareStatement = connector.prepareStatement(statement,
					ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet resultSet = prepareStatement.executeQuery();
			
			resultSet.last();
			int rowAmount = resultSet.getRow();
			if (rowAmount == 0) {
				connector.close();
				return 1;
			}
			
			int m = 1;
			resultSet.beforeFirst();
			while(resultSet.next()){
				String id = resultSet.getString(1);
				id = id.substring(id.length() - 4, id.length());
				int numId = Integer.valueOf(id);
				if (numId != m) {
					connector.close();
					return m;
				}
				m++;
			}
			
			connector.close();
			return m;
		}
		catch (SQLException e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(null, "编号生成失败" + e, "错误", JOptionPane.WARNING_MESSAGE);
		}
		
		//TODO: 更改return值
		return -1;
	}

}
