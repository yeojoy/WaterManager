
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

    private static int[] colorList = {
            R.color.first, R.color.second, R.color.third, R.color.fourth,
            R.color.fifth, R.color.sixth, R.color.seventh, R.color.eighth, R.color.nineth
    };

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

            setWidgetViews(context, views, appWidgetManager, appWidgetIds[i]);
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
        String msg = "ACTION ::: " + action + ", COUNT : " + COUNT;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * onReceive, onUpdate, Configuration Activity에서
     * AppWidgetManager.updateAppWidget()을 호출하므로 여기 한 곳으로 몰아서\
     * View와 Event를 생성 및 등록해 줘야 한다.
     * Configuration Activity에서 받기 위해 불가피하게 static으로 변경
     * ViewManager를 만들어서 처리의 일원화가 필요함.
     * @param context
     * @param views
     * @param appWidgetManager
     * @param widgetId
     */
    public static void setWidgetViews(Context context, RemoteViews views,
            AppWidgetManager appWidgetManager, int widgetId) {
        MyLog.i(TAG, "setWidgetViews()");
        // View가 업데이트 됐음을 알린다.
        if (appWidgetManager == null) {
            appWidgetManager = AppWidgetManager.getInstance(context);
        }
        
        if (COUNT == -1)
            COUNT = PreferencesUtil.getInstance(context).getInt(PREFS_KEY_COUNTS, 0);
        
        MyLog.d(TAG, "setWidgetViews(), COUNT is " + COUNT);

        // PendingIntent의 requestCode가 같아지면 바로 가장 마지막 PendingIntent의
        // Intent로 강제 update되는 사태가 발생함.
        Intent plusIntent = new Intent(PLUS_ACTION);
        PendingIntent plusPIntent = PendingIntent.getBroadcast(context,
                PLUS_ID, plusIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent minusIntent = new Intent(MINUS_ACTION);
        PendingIntent minusPIntent = PendingIntent.getBroadcast(context,
                MINUS_ID, minusIntent, PendingIntent.FLAG_ONE_SHOT);
        views.setOnClickPendingIntent(R.id.btn_plus, plusPIntent);
        views.setOnClickPendingIntent(R.id.btn_minus, minusPIntent);
        
        views.setImageViewResource(R.id.iv_cup, colorList[COUNT]);
        
        if (COUNT == 0) {
            views.setTextViewText(R.id.tv_percentage, "0");
            views.setProgressBar(R.id.pb_percentage, 100, 0, false);
        } else {
            int percentage = (int) (COUNT * ONE_SHOT);
            views.setTextViewText(R.id.tv_percentage, String.valueOf(percentage));
            views.setProgressBar(R.id.pb_percentage, 100, percentage, false);
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
