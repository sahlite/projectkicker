package com.mrsahlite.projectkicker;

public class Utils {
	public static final int POSITION_YEAR = 0;
	public static final int POSITION_MONTH = 1;
	public static final int POSITION_DAY = 2;
	public static final int POSITION_NUM = 3;
	
	
	static String covDateToString(int year, int monthOfYear, int dayOfMonth){
		return String.valueOf(year)+"/"+String.valueOf(monthOfYear+1)+"/"+String.valueOf(dayOfMonth);
	}
	
	static int[] covStringToDate(String strDate){
		int[] result = new int[POSITION_NUM];
		String[] strDateArray = strDate.split("/");
		result[POSITION_YEAR] = Integer.parseInt(strDateArray[POSITION_YEAR]);
		result[POSITION_MONTH] = Integer.parseInt(strDateArray[POSITION_MONTH])-1;
		result[POSITION_DAY] = Integer.parseInt(strDateArray[POSITION_DAY]);
		return result;
	}
}
