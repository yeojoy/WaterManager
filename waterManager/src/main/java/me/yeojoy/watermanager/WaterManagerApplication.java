package me.yeojoy.watermanager;

import android.app.Application;

import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.util.PreferencesUtil;

/**
 * Created by yeojoy on 15. 2. 5..
 */
public class WaterManagerApplication extends Application implements Consts {

    public static int COUNT = -1;

    public static final int DAILY_GOAL_WATER_QUANTITY = 2000;

    @Override
    public void onCreate() {
        super.onCreate();

        COUNT = PreferencesUtil.getInstance(this).getInt(PREFS_KEY_COUNTS, 0);
    }
}
