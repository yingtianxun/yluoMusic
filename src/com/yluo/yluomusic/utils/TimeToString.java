package com.yluo.yluomusic.utils;

public class TimeToString {
	public static String millisecondToStr(int millisecond) {
		
		int seconds = millisecond / 1000;
		
		int minute = seconds / 60;
		
		seconds %= 60;
		
		String timeStr = minute + ":";
		
		if(seconds < 10) {
			timeStr += ("0" + seconds);
		} else {
			timeStr += seconds;
		}
		
		return timeStr;
	}
}
