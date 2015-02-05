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

    public PreferencesUtil(Context context) {
        MyLog.i(TAG, "init()");
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static PreferencesUtil getInstance(Context context) {
        MyLog.i(TAG, "getInstance()");
        if (util == null) {
            util = new PreferencesUtil(context);
        }
        return util;
    }

    // SETTER
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }
    
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }
    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }
    
    // GETTER
    public String getString(String key) {
        return prefs.getString(key, null);
    }
    
    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }
    
    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }
    
    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }
    
    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }
}
