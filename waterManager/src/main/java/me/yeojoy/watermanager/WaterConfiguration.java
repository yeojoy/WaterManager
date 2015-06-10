package me.yeojoy.watermanager;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.widget.WaterWidgetViewManager;
import my.lib.MyLog;


public class WaterConfiguration extends Activity implements Consts,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    
    private static final String TAG = WaterConfiguration.class.getSimpleName();
    
    private int mAppWidgetId;
    private Context mContext;

    private AppWidgetManager appWidgetManager;

    private Button mBtnFinish;

    private RadioButton mRbMale, mRbFemale;
    private EditText mEtUserAge;

    private LinearLayout mLlForFemale;

    private CheckBox mCbBeExpectingABaby, mCbFeedingABaby;

    private TextView mTvResultDesc, mTvResult;

    private int mQuantityOfWater = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.i(TAG, "onCreate()");
        setContentView(R.layout.activity_config);

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

//        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
//            finish();
//        }

        mBtnFinish = (Button) findViewById(R.id.btn_finish);
        mBtnFinish.setOnClickListener(this);

        mRbMale = (RadioButton) findViewById(R.id.rb_male);
        mRbFemale = (RadioButton) findViewById(R.id.rb_female);
        mRbMale.setOnClickListener(mRadioButtonClickListener);
        mRbFemale.setOnClickListener(mRadioButtonClickListener);

        mEtUserAge = (EditText) findViewById(R.id.et_age);

        mLlForFemale = (LinearLayout) findViewById(R.id.ll_for_female);

        mCbBeExpectingABaby = (CheckBox) findViewById(R.id.cb_be_expecting_a_baby);
        mCbFeedingABaby = (CheckBox) findViewById(R.id.cb_feeding_a_baby);
        mCbBeExpectingABaby.setOnCheckedChangeListener(this);
        mCbFeedingABaby.setOnCheckedChangeListener(this);

        mTvResultDesc = (TextView) findViewById(R.id.tv_result_desc);
        mTvResult = (TextView) findViewById(R.id.tv_result);

    }

    View.OnClickListener mRadioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            boolean isChecked = ((RadioButton) view).isChecked();
            int age = 0;
            try {
                age = Integer.parseInt(mEtUserAge.getText().toString());

                if (age > 120 || age < 0) {
                    Toast.makeText(mContext, R.string.warning_age_invalid,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(mContext, R.string.warning_input_age,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            initQuantity();

            if (isChecked) {
                boolean isMale = false;
                if (view.getId() == R.id.rb_male) {
                    // 남자
                    showViewForFemale(false);
                    isMale = true;
                } else {
                    // 여자
                    showViewForFemale(true);

                }

                showResult(age, isMale);
            }

        }
    };

    private void initQuantity() {
        // CheckBox check 상태 초기화
        mCbBeExpectingABaby.setOnCheckedChangeListener(null);
        mCbFeedingABaby.setOnCheckedChangeListener(null);
        mCbBeExpectingABaby.setChecked(false);
        mCbFeedingABaby.setChecked(false);
        // 물의 양 초기화
        mQuantityOfWater = 0;

        // Checkbox에 listener 등록
        mCbBeExpectingABaby.setOnCheckedChangeListener(this);
        mCbFeedingABaby.setOnCheckedChangeListener(this);

        // 결과 화면 가림
        mTvResult.setVisibility(View.GONE);
        mTvResultDesc.setVisibility(View.GONE);

    }

    private void showViewForFemale(boolean b) {
        if (b) {
            mLlForFemale.setVisibility(View.VISIBLE);
        } else {
            mLlForFemale.setVisibility(View.GONE);
        }
    }

    private void showResult(int age, boolean isMale) {
        mTvResult.setVisibility(View.VISIBLE);
        mTvResultDesc.setVisibility(View.VISIBLE);

        setQuantityByAgeAndSex(age, isMale);

        mTvResult.setText(String.format("%d ml", mQuantityOfWater));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_finish) {
            // Update the App Widget with a RemoteViews layout by calling
            // updateAppWidget();
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.water_widget);

                // TODO 변경 필요.
                // Configuration Activity에서도 appWidgetManager.update를 호출해서
                // 불가피하게 Provider 내에 setWidgetViews를 static으로 변경하고
                // 여기에서도 호출하게 함.
                // 설정한 내용을 적용할 땐 뭔가 model로 넘겨주면 된다.
                WaterWidgetViewManager.getInstance(this).setWidgetViews(mContext, views,
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_feeding_a_baby) {
            if (isChecked)
                mQuantityOfWater += 700;
            else
                mQuantityOfWater -= 700;

        } else if (buttonView.getId() == R.id.cb_be_expecting_a_baby) {
            if (isChecked)
                mQuantityOfWater += 200;
            else
                mQuantityOfWater -= 200;
        }
    }


    private void setQuantityByAgeAndSex(int age, boolean isMale) {
        if (isMale) {
            if (age < 1) mQuantityOfWater = 800;
            else if (age < 3) mQuantityOfWater = 1100;
            else if (age < 6) mQuantityOfWater = 1400;
            else if (age < 9) mQuantityOfWater = 1800;
            else if (age < 12) mQuantityOfWater = 2000;
            else if (age < 15) mQuantityOfWater = 2300;
            else if (age < 30) mQuantityOfWater = 2600;
            else if (age < 50) mQuantityOfWater = 2500;
            else if (age < 65) mQuantityOfWater = 2200;
            else mQuantityOfWater = 2000;
        } else {
            if (age < 1) mQuantityOfWater = 800;
            else if (age < 3) mQuantityOfWater = 1100;
            else if (age < 6) mQuantityOfWater = 1400;
            else if (age < 9) mQuantityOfWater = 1700;
            else if (age < 12) mQuantityOfWater = 1800;
            else if (age < 15) mQuantityOfWater = 2000;
            else if (age < 30) mQuantityOfWater = 2100;
            else if (age < 50) mQuantityOfWater = 1900;
            else if (age < 65) mQuantityOfWater = 1800;
            else mQuantityOfWater = 2000;
        }
    }
}
