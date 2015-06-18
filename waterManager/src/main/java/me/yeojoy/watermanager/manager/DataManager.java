package me.yeojoy.watermanager.manager;

import android.content.Context;

/**
 * Created by yeojoy on 15. 6. 18..
 */
public class DataManager {
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


}
