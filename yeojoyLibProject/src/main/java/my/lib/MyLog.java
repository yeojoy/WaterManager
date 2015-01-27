package my.lib;

import android.util.Log;

/**
 * Log를 위한 Library
 * 계속 만들기 귀찮아서...
 * @author yeojoy
 *
 */
public class MyLog implements UtilConstants {
	
	// Debug
	public static void d(String tag, String message) {
		Log.d(tag, message);
	}
	
	public static void d(String message) {
		Log.d(LOG_TAG_1, message);
	}
	
	public static void d(CharSequence message) {
		Log.d(LOG_TAG_1, message.toString());
	}
	
	// Warning
	public static void w(String tag, String message) {
		Log.w(tag, message);
	}
	
	public static void w(String message) {
		Log.w(LOG_TAG_1, message);
	}

	public static void w(CharSequence message) {
		Log.w(LOG_TAG_1, message.toString());
	}
	
	// Info
	public static void i(String tag, String message) {
		Log.i(tag, message);
	}
	
	public static void i(String message) {
		Log.i(LOG_TAG_1, message);
	}

	public static void i(CharSequence message) {
		Log.i(LOG_TAG_1, message.toString());
	}
	
	// Error
	public static void e(String tag, String message) {
		Log.e(tag, message);
	}
	
	public static void e(String message) {
		Log.e(LOG_TAG_1, message);
	}
	
	public static void e(CharSequence message) {
		Log.e(LOG_TAG_1, message.toString());
	}
}
