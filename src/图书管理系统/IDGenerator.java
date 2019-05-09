package 图书管理系统;

import java.util.ArrayList;

public class IDGenerator {
	
	public static ArrayList<String> sortings = new ArrayList<String>();

	public static String generateId(String sorting){
		int sort = sortings.indexOf(sorting) + 1;
		int numId = MainView.databaseHandler.getUsableId(sorting);
		String id = "#";
		
		//"2"和"3"用来计算编号中的零的数量
		for(int i = 0; i < 2 - sort / 10; i++){
			id += "0";
		}
		id += sort;
		for(int j = 0; j < 3 - numId / 10; j++){
			id += "0";
		}
		id += numId;
		
		return id;
	}
}
