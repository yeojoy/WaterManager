package me.yeojoy.watermanager;

import android.app.Application;

import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.util.PreferencesUtil;

/**
 * Created by yeojoy on 15. 2. 5..
 */
public class WaterManagerApplication extends Application implements Consts {

    public static int COUNT = -1;

    public static int DAILY_GOAL_WATER_QUANTITY = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        DAILY_GOAL_WATER_QUANTITY = PreferencesUtil.getInstance(this)
                .getInt(PREFS_KEY_DAILY_GOAL_QUANTITY);

        COUNT = PreferencesUtil.getInstance(this).getInt(PREFS_KEY_COUNTS, 0);
    }

    public void initDailyGoalQuantity() {
        DAILY_GOAL_WATER_QUANTITY = 0;
        PreferencesUtil.getInstance(this).putInt(PREFS_KEY_DAILY_GOAL_QUANTITY, 0);
    }
}
