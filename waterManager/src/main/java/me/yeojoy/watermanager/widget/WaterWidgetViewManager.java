package me.yeojoy.watermanager.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.WaterManagerApplication;
import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.db.AsyncQueryResultListener;
import me.yeojoy.watermanager.db.DBManager;
import me.yeojoy.watermanager.manager.DataManager;
import me.yeojoy.watermanager.model.MyWater;
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
    public void setWidgetViews(final Context context, final RemoteViews views,
                              final AppWidgetManager appWidgetManager, final int widgetId) {
        MyLog.i(TAG, "setWidgetViews()");

        // PendingIntent의 requestCode가 같아지면 바로 가장 마지막 PendingIntent의
        // Intent로 강제 update되는 사태가 발생함.
        Intent smallCupIntent = new Intent(SMALL_CUP_ACTION);
        PendingIntent smallCupPendingIntent = PendingIntent.getBroadcast(context,
                SMALL_CUP_ID, smallCupIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent mediumCupIntent = new Intent(MEDIUM_CUP_ACTION);
        PendingIntent mediumCupPendingIntent = PendingIntent.getBroadcast(context,
                MEDIUM_CUP_ID, mediumCupIntent, PendingIntent.FLAG_ONE_SHOT);

        Intent bigCupIntent = new Intent(BIG_CUP_ACTION);
        PendingIntent bigCupPendingIntent = PendingIntent.getBroadcast(context,
                BIG_CUP_ID, bigCupIntent, PendingIntent.FLAG_ONE_SHOT);

        views.setOnClickPendingIntent(R.id.btn_small, smallCupPendingIntent);
        views.setOnClickPendingIntent(R.id.btn_medium, mediumCupPendingIntent);
        views.setOnClickPendingIntent(R.id.btn_big, bigCupPendingIntent);

        final String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());


        DBManager.getInstance(context).getDringkingDataByDate(today,
                new AsyncQueryResultListener() {
            @Override
            public void onQueryResult(List<MyWater> myWaterResult) {
                MyLog.i(TAG, "onQueryResult()");

                Intent smallCupIntent = new Intent(SMALL_CUP_ACTION);
                PendingIntent smallCupPendingIntent = PendingIntent.getBroadcast(context,
                        SMALL_CUP_ID, smallCupIntent, PendingIntent.FLAG_ONE_SHOT);

                Intent mediumCupIntent = new Intent(MEDIUM_CUP_ACTION);
                PendingIntent mediumCupPendingIntent = PendingIntent.getBroadcast(context,
                        MEDIUM_CUP_ID, mediumCupIntent, PendingIntent.FLAG_ONE_SHOT);

                Intent bigCupIntent = new Intent(BIG_CUP_ACTION);
                PendingIntent bigCupPendingIntent = PendingIntent.getBroadcast(context,
                        BIG_CUP_ID, bigCupIntent, PendingIntent.FLAG_ONE_SHOT);

                views.setOnClickPendingIntent(R.id.btn_small, smallCupPendingIntent);
                views.setOnClickPendingIntent(R.id.btn_medium, mediumCupPendingIntent);
                views.setOnClickPendingIntent(R.id.btn_big, bigCupPendingIntent);

                String quantity = String.format("%dml",
                        DataManager.getInstance(context)
                                .getTodayDrinkingWaterQuantity(today, myWaterResult));

                if (quantity != null && !quantity.isEmpty()) {
                    views.setTextViewText(R.id.tv_today_quantity, quantity);
                }

                String percent = DataManager.getInstance(context)
                                .getTodayDrinkingWaterPercentage(today, myWaterResult) + "%";

                if (percent != null && !percent.isEmpty()) {
                    views.setTextViewText(R.id.tv_today_percentage, percent);
                }

                // Tell the AppWidgetManager to perform an update on the current app widget
                if (widgetId != -1) {
                    appWidgetManager.updateAppWidget(widgetId, views);
                } else {
                    ComponentName myWidget = new ComponentName(context, WaterWidgetProvider.class);
                    appWidgetManager.updateAppWidget(myWidget, views);
                }
            }
        });

        // Tell the AppWidgetManager to perform an update on the current app widget
        if (widgetId != -1) {
            appWidgetManager.updateAppWidget(widgetId, views);
        } else {
            ComponentName myWidget = new ComponentName(context, WaterWidgetProvider.class);
            appWidgetManager.updateAppWidget(myWidget, views);
        }
    }
}
