package com.imooc.guessmusic.util;

import android.util.Log;

public class MyLog {
	// Log�Ŀ���
	public static final boolean DEBUG = true;
	// D����
	public static void d(String tag, String message) {
		if (DEBUG) {
			Log.d(tag, message);
		}
	}
	// W����
	public static void w(String tag, String message) {
		if (DEBUG) {
			Log.w(tag, message);
		}
	}
	// E����
	
	public static void e(String tag, String message) {
		if (DEBUG) {
			Log.e(tag, message);
		}
	}
	// I����
	public static void i(String tag, String message) {
		if (DEBUG) {
			Log.i(tag, message);
		}
	}
}
