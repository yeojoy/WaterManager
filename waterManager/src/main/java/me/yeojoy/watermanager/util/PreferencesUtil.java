package me.yeojoy.watermanager.util;

import me.yeojoy.watermanager.config.Consts;
import my.lib.MyLog;
import android.content.Context;
import android.content.SharedPreferences;


public class PreferencesUtil implements Consts {
    private static final String TAG = PreferencesUtil.class.getSimpleName();
    
    private static PreferencesUtil util;
    
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;
    
    public static PreferencesUtil getInstance(Context context) {
        MyLog.i(TAG, "getInstance()");
        if (util == null) {
            util = new PreferencesUtil();
            init(context);
        }
        return util;
    }
    
    private static void init(Context context) {
        MyLog.i(TAG, "init()");
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    // SETTER
    public synchronized boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }
    
    public synchronized boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }
    public synchronized boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }
    
    // GETTER
    public synchronized String getString(String key) {
        return prefs.getString(key, null);
    }
    
    public synchronized String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }
    
    public synchronized int getInt(String key) {
        return prefs.getInt(key, -1);
    }
    
    public synchronized int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }
    
    public synchronized boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }
    
    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }
}
