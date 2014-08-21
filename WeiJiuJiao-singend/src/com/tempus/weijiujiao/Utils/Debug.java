package com.tempus.weijiujiao.Utils;

public class Debug {
	private static boolean debug = true;
	private static String Default_tag = "TAG_DEFAULT";

	public static void Log(String message) {
		Log(null, message);
	}

	public static void Log(String tag, String message) {
		if (debug) {
			String tag1 = (tag == null ? Default_tag : tag);
			android.util.Log.d(tag1, message);
		}
	}

	public static void Out(String message) {
		Out(null, message);
	}

	public static void Out(String tag, String message) {
		if (debug) {
			String tag1 = tag == null ? Default_tag : tag;
			System.out.println(tag1 + "=" + message);
		}
	}
}
