
package me.yeojoy.watermanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.yeojoy.watermanager.adapter.WaterQuantityAdapter;
import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.db.AsyncQueryResultListener;
import me.yeojoy.watermanager.db.DBManager;
import me.yeojoy.watermanager.model.MyWater;
import my.lib.MyLog;

public class WaterActivity extends Activity implements View.OnClickListener, Consts {

    private static final String TAG = WaterActivity.class.getSimpleName();

    private Context mContext;

    private Button mBtnTheDayBefore, mBtnTheDayAfter;

    private TextView mTvDate, mTvNoData;

    private ListView mLvList;

    private WaterQuantityAdapter mAdapter;

    private static final String TODAY
            = new SimpleDateFormat(DATE_FORMAT).format(new Date());

    private String mDate = TODAY;

    private List<MyWater> mMyWaterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        mContext = this;

        mBtnTheDayAfter = (Button) findViewById(R.id.btn_the_day_after);
        mBtnTheDayBefore = (Button) findViewById(R.id.btn_the_day_before);

        mTvDate = (TextView) findViewById(R.id.tv_date);
        mTvNoData = (TextView) findViewById(R.id.tv_no_data);

        mLvList = (ListView) findViewById(R.id.lv_list);

        mAdapter = new WaterQuantityAdapter(mContext);
        mLvList.setAdapter(mAdapter);

        mBtnTheDayAfter.setOnClickListener(this);
        mBtnTheDayBefore.setOnClickListener(this);

        mTvDate.setText(TODAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDailyDrinkingWater(mDate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mDate = savedInstanceState.getString("today");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("today", mDate);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.water, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            MyLog.d(TAG, "go to WaterConfiguration activity.");

            Intent intent = new Intent(mContext, WaterConfiguration.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_the_day_after: {
                mDate = getDateByValue(mDate, 1);
                mTvDate.setText(mDate);
                displayDailyDrinkingWater(mDate);
                break;
            }

            case R.id.btn_the_day_before: {
                mDate = getDateByValue(mDate, -1);
                mTvDate.setText(mDate);
                displayDailyDrinkingWater(mDate);
                break;
            }
        }
    }

    private void displayDailyDrinkingWater(String date) {
        MyLog.i(TAG, "displayDailyDrinkingWater()");

        if (date == null || date.isEmpty()) {
            MyLog.d(TAG, "mDate 값이 올바르지 않습니다.");
        }

        DBManager.getInstance(mContext).getDringkingDataByDate(date, new AsyncQueryResultListener() {
            @Override
            public void onQueryResult(List<MyWater> myWaterResult) {
                mMyWaterList = myWaterResult;

                if (mMyWaterList == null || mMyWaterList.size() < 1) {
                    mTvNoData.setVisibility(View.VISIBLE);
                    mLvList.setVisibility(View.GONE);
                } else {
                    mTvNoData.setVisibility(View.GONE);
                    mLvList.setVisibility(View.VISIBLE);
                    mAdapter.setMyWaterList(mMyWaterList);
                }

            }
        });
    }

    /**
     * 1과 -1로 어제 혹은 내일 날짜를 가져온다.
     * @param value
     * @return
     */
    private String getDateByValue(String date, int value) {
        MyLog.i(TAG, "getDateByValue()");
        if (value != -1 && value != 1) {
            Toast.makeText(mContext, "1와 -1만 값으로 넣을 수 있습니다.", Toast.LENGTH_SHORT).show();
            return mDate;
        }

        if (mDate == null || mDate.isEmpty()) {
            MyLog.d(TAG, "mDate 값이 올바르지 않습니다.");
        }
        Date now = null;
        try {
            now = new SimpleDateFormat(DATE_FORMAT).parse(mDate);
        } catch (ParseException e) {

            e.printStackTrace();
            return mDate;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(now);
        MyLog.d(TAG, "getDateByValue(), today : " + new SimpleDateFormat(DATE_FORMAT).format(c.getTime()));
        c.add(Calendar.DATE, value);
        MyLog.d(TAG, "getDateByValue(), after add(), today : " + new SimpleDateFormat(DATE_FORMAT).format(c.getTime()));
        return new SimpleDateFormat(DATE_FORMAT).format(c.getTime());
    }
}
