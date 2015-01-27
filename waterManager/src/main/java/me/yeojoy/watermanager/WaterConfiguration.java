package me.yeojoy.watermanager;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.widget.WaterWidgetProvider;
import my.lib.MyLog;


public class WaterConfiguration extends Activity implements Consts {
    
    private static final String TAG = WaterConfiguration.class.getSimpleName();
    
    private int mAppWidgetId;
    private Context mContext;
    
    private AppWidgetManager appWidgetManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.i(TAG, "onCreate()");
        setContentView(R.layout.activity_water);
        mContext = this;
        
        Intent intent = getIntent();
        
        // First, get the App Widget ID from the Intent that launched the Activity:
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            // AppWidgetManager.INVALID_APPWIDGET_ID is 0.
            MyLog.i(TAG, "onCreate(), App Widget ID is " + mAppWidgetId);
        }
        
        // When the configuration is complete, get an instance 
        // of the AppWidgetManager by calling
        appWidgetManager = AppWidgetManager.getInstance(this);

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID)
            finish();
        
        // Update the App Widget with a RemoteViews layout by calling
        // updateAppWidget();
        if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            RemoteViews views = new RemoteViews(getPackageName(), 
                    R.layout.water_widget);
            
            // TODO 변경 필요.
            // Configuration Activity에서도 appWidgetManager.update를 호출해서
            // 불가피하게 Provider 내에 setWidgetViews를 static으로 변경하고
            // 여기에서도 호출하게 함. 
            WaterWidgetProvider.setWidgetViews(mContext, views,
                    appWidgetManager, mAppWidgetId);
            MyLog.i(TAG, "onCreate(), App Widget ID is valid.");
        }
        
        
        // Finally, create the return Intent, set it with the Activity
        // result, and finish the Activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                mAppWidgetId);
        resultIntent.putExtra("aaa", "hello world!");
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
