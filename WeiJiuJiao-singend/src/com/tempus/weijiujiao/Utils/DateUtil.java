package com.tempus.weijiujiao.Utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public static String FormatYear(long time) {
		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy年");
		String date = SimpleDateFormat.format(new Date(time));
		return date;
	}

	public static String FormatMounth(long time) {
		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy年MM月");
		String date = SimpleDateFormat.format(new Date(time));
		return date;
	}

	public static String FormatDay(long time) {
		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
		String date = SimpleDateFormat.format(new Date(time));
		return date;
	}
	public static String FormatSeconds(long time) {
		SimpleDateFormat SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = SimpleDateFormat.format(new Date(time));
		return date;
	}
	public static int getMonthMaxDays(int month, int year) {
		boolean run = false;
		if (year % 400 == 0 || year % 4 == 0) {
			run = true;
		}
		if(month==1||month==3||month==5||month==7||month==8||month==10||month==12){
			return 31;
		}else if(month==4||month==6||month==9||month==11){
			return 30;
		}else{
			if (run) {
				return 29;
			} else {
				return 28;
			}
		}
	}
}
