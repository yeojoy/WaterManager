package me.yeojoy.watermanager.manager;

import android.content.Context;

import java.util.List;

import me.yeojoy.watermanager.WaterManagerApplication;
import me.yeojoy.watermanager.model.MyWater;
import my.lib.MyLog;

/**
 * Created by yeojoy on 15. 6. 18..
 */
public class DataManager {
    private static final String TAG = DataManager.class.getSimpleName();

    private static DataManager mDataManager;

    private Context mContext;

    public static DataManager getInstance(Context context) {
        if (mDataManager == null) {
            mDataManager = new DataManager(context);
        }

        return mDataManager;
    }

    public DataManager(Context context) {
        mContext = context;
    }


    public int getTodayDrinkingWaterQuantity(String today, List<MyWater> water) {
        MyLog.i(TAG, "getTodayDrinkingWaterQuantity()");
        if (water == null || water.size() < 1) return 0;

        int quantity = 0;
        for (MyWater w : water) {
            if (w.getDrinkingDate().equals(today)) {
                quantity += w.getDrinkingQuantity();
            }
        }

        MyLog.d(TAG, "getTodayDrinkingWaterQuantity(), Quantity : " + quantity);
        return quantity;
    }

    public int getTodayDrinkingWaterPercentage(String today, List<MyWater> water) {
        MyLog.i(TAG, "getTodayDrinkingWaterPercentage()");
        if (water == null || water.size() < 1) return 0;

        int percent = 0;
        int quantity = getTodayDrinkingWaterQuantity(today, water);
        if (quantity > 0)
            percent = quantity * 100 / WaterManagerApplication.DAILY_GOAL_WATER_QUANTITY;

        MyLog.d(TAG, "getTodayDrinkingWaterPercentage(), Percent : " + percent);
        return percent;
    }
}
