package me.yeojoy.watermanager.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.WaterManagerApplication;
import me.yeojoy.watermanager.config.Consts;
import my.lib.MyLog;

/**
 * Created by yeojoy on 15. 2. 5..
 */
public class WaterWidgetViewManager implements Consts {
    private static final String TAG = WaterWidgetViewManager.class.getSimpleName();

    private static int[] colorList = {
            R.color.first, R.color.second, R.color.third, R.color.fourth,
            R.color.fifth, R.color.sixth, R.color.seventh, R.color.eighth, R.color.nineth
    };

    private static WaterWidgetViewManager mManager;

    private Context mContext;

    public WaterWidgetViewManager(Context context) {
        mContext = context;
    }

    public static WaterWidgetViewManager getInstance(Context context) {
        if (mManager == null) {
            mManager = new WaterWidgetViewManager(context);
        }

        return mManager;
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
    public void setWidgetViews(Context context, RemoteViews views,
                                      AppWidgetManager appWidgetManager, int widgetId) {
        MyLog.i(TAG, "setWidgetViews()");
        // View가 업데이트 됐음을 알린다.
        if (appWidgetManager == null) {
            appWidgetManager = AppWidgetManager.getInstance(context);
        }

        MyLog.d(TAG, "setWidgetViews(), count is " + WaterManagerApplication.COUNT);

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

        views.setImageViewResource(R.id.iv_cup, colorList[WaterManagerApplication.COUNT]);

        if (WaterManagerApplication.COUNT == 0) {
            views.setTextViewText(R.id.tv_percentage, "0");
            views.setProgressBar(R.id.pb_percentage, 100, 0, false);
        } else {
            int percentage = (int) (WaterManagerApplication.COUNT * ONE_SHOT);
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
