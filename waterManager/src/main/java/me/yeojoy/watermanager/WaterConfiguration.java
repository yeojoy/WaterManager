package me.yeojoy.watermanager;

import my.lib.MyLog;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;


public class WaterConfiguration extends Activity {
    
    private static final String TAG = WaterConfiguration.class.getSimpleName();
    
    private int mAppWidgetId;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.i(TAG, "onCreate()");
        setContentView(R.layout.activity_water);
        
        Intent intent = getIntent();
        
        // First, get the App Widget ID from the Intent that launched the Activity:
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            // AppWidgetManager.INVALID_APPWIDGET_ID is 0.
            MyLog.i(TAG, "onCreate(), App Widget ID is " + mAppWidgetId);
        }
        
        // When the configuration is complete, get an instance of the AppWidgetManager by calling
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        
        // Update the App Widget with a RemoteViews layout by calling updateAppWidget();
        if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.water_widget);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);
            MyLog.i(TAG, "onCreate(), App Widget ID is valid.");
        }
        
        // Finally, create the return Intent, set it with the Activity result, and finish the Activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  mAppWidgetId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
