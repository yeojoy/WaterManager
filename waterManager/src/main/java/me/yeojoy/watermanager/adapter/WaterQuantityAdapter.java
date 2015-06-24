package me.yeojoy.watermanager.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.WaterActivity;
import me.yeojoy.watermanager.db.DBManager;
import me.yeojoy.watermanager.model.MyWater;
import me.yeojoy.watermanager.util.CommonUtils;
import my.lib.MyLog;

/**
 * Created by yeojoy on 15. 6. 18..
 */
public class WaterQuantityAdapter extends BaseAdapter {

    private static final String TAG = WaterQuantityAdapter.class.getSimpleName();

    private Context mContext;

    private List<MyWater> mMyWaterList;


    public WaterQuantityAdapter(Context context) {
        mContext = context;
    }

    public WaterQuantityAdapter(Context context, List<MyWater> list) {
        mContext = context;
        mMyWaterList = list;
    }

    @Override
    public int getCount() {
        if (mMyWaterList != null) return mMyWaterList.size();
        return 0;
    }

    @Override
    public MyWater getItem(int position) {
        if (mMyWaterList != null && position < mMyWaterList.size())
            return mMyWaterList.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mMyWaterList != null)
            return mMyWaterList.get(position).getId();

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.row_water, null);
            TextView tvTime = (TextView) view.findViewById(R.id.tv_time);
            EditText etQuantity = (EditText) view.findViewById(R.id.et_water_quantity);
            Button btnEdit = (Button) view.findViewById(R.id.btn_edit);
            Button btnDelete = (Button) view.findViewById(R.id.btn_delete);
            viewHolder = new ViewHolder(tvTime, etQuantity, btnEdit, btnDelete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final MyWater water = getItem(position);

        viewHolder.mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewHolder.mBtnEdit.getText().toString()
                        .equals(mContext.getText(R.string.btn_edit))) {
                    CommonUtils.showKeyboard(mContext, viewHolder.mEtQuantity);

                    viewHolder.mEtQuantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    viewHolder.mEtQuantity.setEnabled(true);
                    viewHolder.mEtQuantity.setFocusable(true);
                    viewHolder.mEtQuantity.setFocusableInTouchMode(true);


                    viewHolder.mEtQuantity.requestFocus();
                    viewHolder.mEtQuantity.setSelection(viewHolder.mEtQuantity.getText().toString().length());
                    viewHolder.mBtnEdit.setText(R.string.btn_edit_clicked);


                } else {
                    try {
                        water.setDrinkingQuantity(Integer.parseInt(viewHolder.mEtQuantity.getText().toString()));

                    } catch (NumberFormatException e) {
                        viewHolder.mEtQuantity.setText("");
                        Toast.makeText(mContext, R.string.warning_number_format,
                                Toast.LENGTH_SHORT).show();

                        return;
                    }

                    MyLog.d(TAG, "after : " + water.toString());
                    updateDrinkingWater(water);

                    ((WaterActivity) mContext).showTotalQuantity(mMyWaterList);

                    viewHolder.mEtQuantity.setFocusableInTouchMode(false);
                    viewHolder.mEtQuantity.setFocusable(false);
                    viewHolder.mEtQuantity.setEnabled(false);

                    viewHolder.mBtnEdit.setText(R.string.btn_edit);

                    CommonUtils.hideKeyboard(mContext, viewHolder.mEtQuantity);
                    ((WaterActivity) mContext).updateAppWidget();
                }
            }
        });

        viewHolder.mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyWaterList.remove(water);
                ((WaterActivity) mContext).showTotalQuantity(mMyWaterList);
                deleteDrinkingWater(water);

                ((WaterActivity) mContext).updateAppWidget();
            }
        });

        viewHolder.mEtQuantity.setText(String.valueOf(water.getDrinkingQuantity()));
        viewHolder.mTvTime.setText(water.getDrinkingTime());

        return view;

    }

    private class ViewHolder {
        TextView mTvTime;
        EditText mEtQuantity;
        Button mBtnEdit, mBtnDelete;

        public ViewHolder(TextView tv, EditText et, Button btnEdit,
                          Button btnDelete) {
            mTvTime = tv;
            mEtQuantity = et;
            mBtnEdit = btnEdit;
            mBtnDelete = btnDelete;
        }
    }

    public void setMyWaterList(List<MyWater> waterList) {
        if (mMyWaterList == null) {
            mMyWaterList = new ArrayList<MyWater>();
        }

        mMyWaterList.clear();
        mMyWaterList.addAll(waterList);

        notifyDataSetChanged();
    }

    private void updateDrinkingWater(MyWater water) {
        if (water == null) return;
        DBManager.getInstance(mContext).updateDrinkingWater(water);
        notifyDataSetChanged();
    }

    private void deleteDrinkingWater(MyWater water) {
        if (water == null) return;
        DBManager.getInstance(mContext).deleteDrinkingWater(water);
        notifyDataSetChanged();
    }
}
