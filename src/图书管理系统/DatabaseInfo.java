package 图书管理系统;

public final class DatabaseInfo {
	
	public static final String[] hintColumnName = 
		{"分类", "书名", "作者", "出版社", "出版时间", "价格", "购书年月", "书签", "备注"};
	
	public static final String[] columName = 
		{"编号", "分类", "书名", "作者", "出版社", "出版时间", "价格", "购书年月", "书签", "备注"};
	
	public static final int DefaultAddBookTableRowAcount = 10;
	
	public static final int DefaultAddBookTableRowHeight = 40;
	
	public static final String[][] searchTable = new String[1][5];

}
