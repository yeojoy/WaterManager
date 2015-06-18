
package me.yeojoy.watermanager.widget;

import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.WaterManagerApplication;
import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.db.DBHelper;
import me.yeojoy.watermanager.db.DBManager;
import me.yeojoy.watermanager.model.MyWater;
import me.yeojoy.watermanager.util.PreferencesUtil;
import my.lib.MyLog;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class WaterWidgetProvider extends AppWidgetProvider implements Consts {

    private static final String TAG = WaterWidgetProvider.class.getSimpleName();
    
    private static final int SMALL_CUP = 150;
    private static final int MEDIUM_CUP = 250;
    private static final int BIG_CUP = 500;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        MyLog.i(TAG, "onEnabled()");
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyLog.i(TAG, "onUpdate()");
        final int widgetCount = appWidgetIds.length;

        for (int i = 0; i < widgetCount; i++) {
            MyLog.d(TAG, "onUpdate(), AppWidget ID : " + appWidgetIds[i]);

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.water_widget);

            WaterWidgetViewManager.getInstance(context)
                    .setWidgetViews(context, views, appWidgetManager, appWidgetIds[i]);
        }
    }

    
    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i(TAG, "onReceive()");
        super.onReceive(context, intent);
        if (intent == null) return;
        
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            Set<String> keySet = bundle.keySet();
            Iterator<String> i = keySet.iterator();
            
            MyLog.d(TAG, "======================================================");
            while (i.hasNext()) {
                String key = i.next();
                MyLog.d(TAG, "key : " + key + ", value : " + String.valueOf(bundle.get(key)));
            }
            MyLog.d(TAG, "======================================================");
        }
        
        final String action = intent.getAction();
        MyLog.d(TAG, "onReceive(), action is " + action);


        MyWater myWater = null;
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String now = new SimpleDateFormat("HH:mm:ss").format(new Date());

        if (action.equals(SMALL_CUP_ACTION)) {
            myWater = new MyWater(-1, today, now, SMALL_CUP);
        } else if (action.equals(MEDIUM_CUP_ACTION)) {
            myWater = new MyWater(-1, today, now, MEDIUM_CUP);
        } else if (action.equals(BIG_CUP_ACTION)) {
            myWater = new MyWater(-1, today, now, BIG_CUP);
        }

        if (myWater != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.water_widget);
            DBManager.getInstance(context).saveData(myWater);

            WaterWidgetViewManager.getInstance(context).setWidgetViews(context, views,
                    AppWidgetManager.getInstance(context), -1);
        }
    }
}
