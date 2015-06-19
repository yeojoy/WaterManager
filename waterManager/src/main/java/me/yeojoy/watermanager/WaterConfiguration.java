package me.yeojoy.watermanager;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.util.CommonUtils;
import me.yeojoy.watermanager.util.PreferencesUtil;
import me.yeojoy.watermanager.widget.WaterWidgetViewManager;
import my.lib.MyLog;


public class WaterConfiguration extends Activity implements Consts,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    
    private static final String TAG = WaterConfiguration.class.getSimpleName();
    
    private int mAppWidgetId;
    private Context mContext;

    private AppWidgetManager appWidgetManager;

    private Button mBtnFinish;

    private RadioGroup mRgMaleOrFemale;
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
        
        // First, get the App Widget IDX from the Intent that launched the Activity:
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

        mRgMaleOrFemale = (RadioGroup) findViewById(R.id.rg_male_female);
        mRgMaleOrFemale.setOnCheckedChangeListener(mRadioButtonClickListener);

        mEtUserAge = (EditText) findViewById(R.id.et_age);

        mLlForFemale = (LinearLayout) findViewById(R.id.ll_for_female);

        mCbBeExpectingABaby = (CheckBox) findViewById(R.id.cb_be_expecting_a_baby);
        mCbFeedingABaby = (CheckBox) findViewById(R.id.cb_feeding_a_baby);
        mCbBeExpectingABaby.setOnCheckedChangeListener(this);
        mCbFeedingABaby.setOnCheckedChangeListener(this);

        mTvResultDesc = (TextView) findViewById(R.id.tv_result_desc);
        mTvResult = (TextView) findViewById(R.id.tv_result);

    }

    private void initQuantity() {
        MyLog.i(TAG, "initQuantity()");
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
        MyLog.i(TAG, "showViewForFemale()");
        if (b) {
            mLlForFemale.setVisibility(View.VISIBLE);
        } else {
            mLlForFemale.setVisibility(View.GONE);
        }
    }

    private void showResult() {
        mTvResult.setVisibility(View.VISIBLE);
        mTvResultDesc.setVisibility(View.VISIBLE);

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
                WaterWidgetViewManager.setWidgetViews(mContext, views,
                        appWidgetManager, mAppWidgetId);
                MyLog.i(TAG, "onCreate(), App Widget ID is valid.");
            }

            WaterManagerApplication.DAILY_GOAL_WATER_QUANTITY = mQuantityOfWater;
            PreferencesUtil.getInstance(mContext).putInt(PREFS_KEY_DAILY_GOAL_QUANTITY,
                    mQuantityOfWater);

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

        MyLog.i(TAG, "onCheckedChanged()");
        if (buttonView.getId() == R.id.cb_feeding_a_baby) {
            if (isChecked) {
                mQuantityOfWater += 700;
                if (mCbBeExpectingABaby.isChecked()) {
                    mCbBeExpectingABaby.setChecked(false);
                }
            } else
                mQuantityOfWater -= 700;

        } else if (buttonView.getId() == R.id.cb_be_expecting_a_baby) {
            if (isChecked) {
                mQuantityOfWater += 200;
                if (mCbFeedingABaby.isChecked()) {
                    mCbFeedingABaby.setChecked(false);
                }
            } else
                mQuantityOfWater -= 200;
        }

        showResult();
    }

    private RadioGroup.OnCheckedChangeListener mRadioButtonClickListener
            = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            MyLog.i(TAG, "onCheckedChanged()");
            int age = 0;
            boolean isMale = false;

            CommonUtils.hideKeyboard(mContext, mEtUserAge);

            try {
                age = Integer.parseInt(mEtUserAge.getText().toString());

                if (age > 120 || age < 0) {
                    Toast.makeText(mContext, R.string.warning_age_invalid,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                MyLog.d(TAG, "age : " + age);
            } catch (NumberFormatException e) {
                Toast.makeText(mContext, R.string.warning_input_age,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            initQuantity();

            if (checkedId == R.id.rb_male) {
                showViewForFemale(false);
                isMale = true;
            } else if (checkedId == R.id.rb_female) {
                showViewForFemale(true);
            }

            setQuantityByAgeAndSex(age, isMale);
            showResult();
        }
    };

    private void setQuantityByAgeAndSex(int age, boolean isMale) {
        MyLog.i(TAG, "setQuantityByAgeAndSex()");
        MyLog.d(TAG, "age : " + age + ", isMale : " + isMale);
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
        MyLog.d(TAG, "mQuantityOfWater : " + mQuantityOfWater);
    }
}
