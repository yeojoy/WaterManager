
package me.yeojoy.watermanager.widget;

import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.WaterManagerApplication;
import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.util.PreferencesUtil;
import my.lib.MyLog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

public class WaterWidgetProvider extends AppWidgetProvider implements Consts {

    private static final String TAG = WaterWidgetProvider.class.getSimpleName();
    
    private static final int MAX = 8;
    private static final int MIN = 0;
    

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
        
        if (intent != null && intent.getStringExtra("aaa") != null) {
            MyLog.i(TAG, "onReceive(), aaa : " + intent.getStringExtra("aaa"));
        }
        
        final String action = intent.getAction();
        MyLog.d(TAG, "onReceive(), action is " + action);
        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.water_widget);
        
        if (action.equals(PLUS_ACTION)) {
            WaterManagerApplication.COUNT++;
            if (WaterManagerApplication.COUNT > MAX) {
                WaterManagerApplication.COUNT = MAX;
            }
            PreferencesUtil.getInstance(context).putInt(PREFS_KEY_COUNTS,
                    WaterManagerApplication.COUNT);

            WaterWidgetViewManager.getInstance(context)
                    .setWidgetViews(context, views, null, -1);
        } else if (action.equals(MINUS_ACTION)) {
            WaterManagerApplication.COUNT--;
            if (WaterManagerApplication.COUNT < MIN) {
                WaterManagerApplication.COUNT = MIN;
            }
            PreferencesUtil.getInstance(context).putInt(PREFS_KEY_COUNTS,
                    WaterManagerApplication.COUNT);

            WaterWidgetViewManager.getInstance(context)
                    .setWidgetViews(context, views, null, -1);
        }

        String msg = "ACTION ::: " + action + ", COUNT : " + WaterManagerApplication.COUNT;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    
}
