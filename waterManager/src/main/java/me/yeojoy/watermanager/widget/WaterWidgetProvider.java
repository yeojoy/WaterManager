
package me.yeojoy.watermanager.widget;

import me.yeojoy.watermanager.R;
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
    
    private static int COUNT = -1;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        MyLog.i(TAG, "onReceive()");
        super.onReceive(context, intent);
        if (intent == null) return;
        
        COUNT = PreferencesUtil.getInstance(context).getInt(PREFS_KEY_COUNTS, 0);
        if (COUNT == -1) {
            MyLog.d(TAG, "onReceive(), COUNT is -1.");
            Toast.makeText(context, "헐...", Toast.LENGTH_SHORT).show();
            return;
        }
        
        final String action = intent.getAction();
        MyLog.d(TAG, "onReceive(), action is " + action);
        
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.water_widget);
        
        if (action.equals(PLUS_ACTION)) {
            COUNT++;
            if (COUNT > MAX) {
                COUNT = MAX;
            }
            PreferencesUtil.getInstance(context).putInt(PREFS_KEY_COUNTS, COUNT);
            
            setWidgetViews(context, views, null, -1);
        } else if (action.equals(MINUS_ACTION)) {
            COUNT--;
            if (COUNT < MIN) {
                COUNT = MIN;
            }
            PreferencesUtil.getInstance(context).putInt(PREFS_KEY_COUNTS, COUNT);
            
            setWidgetViews(context, views, null, -1);
        }
        
        Toast.makeText(context, "ACTION ::: " + action, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        MyLog.i(TAG, "onUpdate()");
        final int widgetCount = appWidgetIds.length;
        
        for (int i = 0; i < widgetCount; i++) {
            MyLog.d(TAG, "onUpdate(), AppWidget ID : " + appWidgetIds[i]);
            
            // PendingIntent의 id값이 같아지면 바로 가장 마지막 PendingIntent의
            // Intent로 강제 update되는 사태가 발생함.
            Intent plusIntent = new Intent("me.yeojoy.PLUS");
            PendingIntent plusPIntent = PendingIntent.getBroadcast(context, 2222, plusIntent, 0);
            
            Intent minusIntent = new Intent("me.yeojoy.MINUS");
            PendingIntent minusPIntent = PendingIntent.getBroadcast(context, 1111, minusIntent, 0);
            
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.water_widget);
            views.setOnClickPendingIntent(R.id.btn_plus, plusPIntent);
            views.setOnClickPendingIntent(R.id.btn_minus, minusPIntent);
            
            setWidgetViews(context, views, appWidgetManager, appWidgetIds[i]);
        }
    }

    private int[] colorList = {
            R.color.first, R.color.second, R.color.third, R.color.fourth,
            R.color.fifth, R.color.sixth, R.color.seventh, R.color.eighth, R.color.nineth
    };
    
    private void setWidgetViews(Context context, RemoteViews views, 
            AppWidgetManager appWidgetManager, int widgetId) {
        MyLog.i(TAG, "setWidgetViews()");
        // View가 업데이트 됐음을 알린다.
        if (appWidgetManager == null) {
            appWidgetManager = AppWidgetManager.getInstance(context);
        }
        
        if (COUNT == -1)
            COUNT = PreferencesUtil.getInstance(context).getInt(PREFS_KEY_COUNTS, 0);
        
        MyLog.d(TAG, "setWidgetViews(), COUNT is " + COUNT);
        
        views.setImageViewResource(R.id.iv_cup, colorList[COUNT]);
        if (COUNT == 0) {
            views.setTextViewText(R.id.tv_percentage, "0");
            views.setProgressBar(R.id.pb_percentage, 100, 0, false);
        } else {
            views.setTextViewText(R.id.tv_percentage, String.valueOf((int) (100 / COUNT)));
            views.setProgressBar(R.id.pb_percentage, 100, (int) (100 / COUNT), false);
        }
        // Tell the AppWidgetManager to perform an update on the current app widget
        if (widgetId != -1) {
            appWidgetManager.updateAppWidget(widgetId, views);
        } else {
            ComponentName myWidget = new ComponentName(context, WaterWidgetProvider.class);
            appWidgetManager.updateAppWidget(myWidget, views);
        }
    }
    
    
}
