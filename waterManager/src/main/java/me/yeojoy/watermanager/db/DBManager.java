package me.yeojoy.watermanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.yeojoy.watermanager.BuildConfig;
import me.yeojoy.watermanager.R;
import me.yeojoy.watermanager.config.Consts;
import me.yeojoy.watermanager.model.MyWater;
import my.lib.MyLog;


public class DBManager implements DBConstants {
    
    private static final String TAG = DBManager.class.getSimpleName();
    
    private static DBManager mManager;
    
    private static Context mContext;
    
    private static DBHelper mDBHelper;

    public static DBManager getInstance(Context context) {
        if (mManager == null)
            mManager = new DBManager(context);
        
        return mManager;
    }

    public DBManager(Context context) {
        mContext = context;
        mDBHelper = new DBHelper(mContext);
    }

    public void saveData(MyWater data) {
        MyLog.i(TAG, "saveData()");
        if (data != null) {
            DBInsertAsyncTask task = new DBInsertAsyncTask();
            task.execute(data);
        }
    }

    public void getDringkingDataByDate(String date, AsyncQueryResultListener listener) {
        DBSelectAsyncTask task = new DBSelectAsyncTask(date, listener);
        task.execute();
    }

    public void updateDrinkingWater(MyWater water) {
        DBUpdateAsyncTask task = new DBUpdateAsyncTask(water);
        task.execute();
    }

    public void deleteDrinkingWater(MyWater water) {
        DBDeleteAsyncTask task = new DBDeleteAsyncTask(water);
        task.execute();

    }

    private void insertData(MyWater myWater) {
        MyLog.i(TAG, "insertData()");
        
        if (myWater == null) {
            MyLog.d(TAG, "myWater is null");
            return;
        }

        if (isAleadySaved(myWater)) {
            MyLog.d(TAG, "myWater is already saved.");
            return;
        }

        SQLiteDatabase db = null;
        try {
            // 1. get reference to writable DB
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();

            MyLog.i(TAG, "insertData(), beginTransaction");
            // 2. create ContentValues to add key "column"/value
            ContentValues values = new ContentValues();
            values.put(DATE, myWater.getDrinkingDate());
            values.put(TIME, myWater.getDrinkingTime());
            values.put(QUANTITY, myWater.getDrinkingQuantity());

            // 3. insert
            long id = db.insert(TABLE_NAME, // table
                    null, //nullColumnHack
                    values); // key/value -> keys = column names/ values = column values
            if (id > -1) {
                MyLog.i(TAG, "insertData(), insert data successfuly. Id is " + id);
                db.setTransactionSuccessful();
            }

            db.endTransaction();
            MyLog.i(TAG, "insertData(), endTransaction");
        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            // 4. close
            if (db != null) db.close();
        }
    }

    /**
     * 동일한 시간대에 데이터가 저장 됐는지 확인한다.
     * @param dto
     * @return 이미 같은 시간이 저장 됐으면 true
     *         없으면 false
     */
    private boolean isAleadySaved(MyWater dto) {
        MyLog.i(TAG, "isAleadySaved()");

        if (dto == null) return false;

        Cursor cursor = null;

        // TODO select last data
        SQLiteDatabase db = null;
        try {
            db = mDBHelper.getReadableDatabase();

            String[] selectionArgs = { dto.getDrinkingDate(), dto.getDrinkingTime() };
            cursor = db.query(TABLE_NAME, null, DATE + " = ? AND " + TIME + " = ?", selectionArgs,
                    null, null, null);

            if (cursor != null && cursor.getCount() > 0) return true;
        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
        
        return false;
    }

    /**
     * date를 받아서 date에 마신 물의 양을 DB에 저장한 데이터를 가져온다.
     * @param date
     * @return
     */
    private List<MyWater> selectMyWater(String date) {
        MyLog.i(TAG, "selectMyWater()");

        Cursor cursor = null;

        List<MyWater> list = new ArrayList<MyWater>();

        // TODO select last data
        SQLiteDatabase db = null;
        try {

            db = mDBHelper.getReadableDatabase();
            db.beginTransaction();
            if (date == null || date.isEmpty()) {
                cursor = db.query(TABLE_NAME, null, null, null,
                        null, null, null);
            } else {
                String[] selectionArgs = { date };
                cursor = db.query(TABLE_NAME, null, DATE + " = ?", selectionArgs,
                        null, null, null);
            }


            if (cursor == null && cursor.getCount() < 1) {
                MyLog.d(TAG, mContext.getString(R.string.warning_no_data));
                return null;
            }
            list = convertCursorToList(cursor);

            db.endTransaction();
        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return list;
    }

    /**
     * id 와 MyWater object를 받아서 물 마신양을 update 해줌.
     * @param water
     * @return
     */
    private int updateMyWater(MyWater water) {
        MyLog.i(TAG, "updateMyWater()");
        int count = 0;

        // TODO select last data
        SQLiteDatabase db = null;
        try {

            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();

            ContentValues values = new ContentValues();
            MyLog.d(TAG, "update quantity : " + water.getDrinkingQuantity());
            values.put(QUANTITY, water.getDrinkingQuantity());

            count = db.update(TABLE_NAME, values,
                    IDX + " = ?", new String[] {String.valueOf(water.getId())});

            if (BuildConfig.DEBUG) {
                Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
                if (c != null) {
                    c.moveToFirst();
                    MyLog.d(TAG, "++++++++++++++++++++++++++++++++++++++++++++++++");
                    do {
                        MyWater w = new MyWater(c.getInt(INDEX_ID),
                                c.getString(INDEX_DATE),
                                c.getString(INDEX_TIME),
                                c.getInt(INDEX_QUANTITY));

                        MyLog.d(TAG, "after update(), MyWater obj : " + w.toString());

                    } while (c.moveToNext());
                    MyLog.d(TAG, "++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }

            if (count > 0) {
                MyLog.d(TAG, "updating a row is successful.");
                db.setTransactionSuccessful();
            } else {
                MyLog.d(TAG, "updating a row fails.");
            }
            db.endTransaction();
            MyLog.d(TAG, "update transaction ends.");

        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    /**
     * id에 해당하는 data를 삭제
     * @param water
     * @return
     */
    private int deleteMyWater(MyWater water) {
        MyLog.i(TAG, "deleteMyWater()");
        int count = 0;

        SQLiteDatabase db = null;
        try {
            MyLog.d(TAG, "deleteMyWater(), MyWater obj : " + water.toString());
            db = mDBHelper.getWritableDatabase();
            db.beginTransaction();

            count = db.delete(TABLE_NAME,
                    IDX + " = ?", new String[] {String.valueOf(water.getId())});

            if (BuildConfig.DEBUG) {
                Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
                if (c != null) {
                    c.moveToFirst();
                    MyLog.d(TAG, "++++++++++++++++++++++++++++++++++++++++++++++++");
                    do {
                        MyWater w = new MyWater(c.getInt(INDEX_ID),
                                c.getString(INDEX_DATE),
                                c.getString(INDEX_TIME),
                                c.getInt(INDEX_QUANTITY));

                        MyLog.d(TAG, "after delete(), MyWater obj : " + w.toString());

                    } while (c.moveToNext());
                    MyLog.d(TAG, "++++++++++++++++++++++++++++++++++++++++++++++++");
                }
            }

            if (count > 0) {
                MyLog.d(TAG, "deleting a row is successful.");
                db.setTransactionSuccessful();
            } else {
                MyLog.d(TAG, "deleting a row fails.");
            }
            db.endTransaction();
            MyLog.d(TAG, "delete transaction ends.");
        } catch (SQLiteException e) {
            MyLog.e(TAG, e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }

        return count;
    }

    private List<MyWater> convertCursorToList(Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) return null;

        List<MyWater> list = new ArrayList<MyWater>(cursor.getCount());

        MyWater water;
        cursor.moveToFirst();
        do {
            water = new MyWater(cursor.getInt(INDEX_ID),
                    cursor.getString(INDEX_DATE),
                    cursor.getString(INDEX_TIME),
                    cursor.getInt(INDEX_QUANTITY));
            list.add(water);
        } while (cursor.moveToNext());

        MyLog.d(TAG, "=====================================================");
        for (MyWater w : list) {
            MyLog.d(TAG, w.toString());
        }
        MyLog.d(TAG, "=====================================================");

        return list;
    }

    /**
     * AsyncTask를 사용해서 DB에 데이터를 입력한다.
     */
    private class DBInsertAsyncTask extends AsyncTask<MyWater, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(MyWater... params) {
            MyLog.i(TAG, "DBInsertAsyncTask, doInBackground()");
            insertData(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (BuildConfig.DEBUG)
                Toast.makeText(mContext, "DB Insert Finished.",
                        Toast.LENGTH_SHORT).show();
        }
    }

    private class DBSelectAsyncTask extends AsyncTask<Void, Void, List<MyWater>> {

        AsyncQueryResultListener listener;
        private String date;

        public DBSelectAsyncTask(String date, AsyncQueryResultListener l) {
            this.date = date;
            listener = l;
        }

        @Override
        protected List<MyWater> doInBackground(Void... params) {
            MyLog.i(TAG, "DBSelectAsyncTask, doInBackground()");
            return selectMyWater(date);
        }

        @Override
        protected void onPostExecute(List<MyWater> list) {
            super.onPostExecute(list);
            if (listener != null) {
                if (list != null)
                    MyLog.d(TAG, "DBSelectAsyncTask, list size is " + list.size());
                listener.onQueryResult(list);
            }
        }
    }

    private class DBUpdateAsyncTask extends AsyncTask<Void, Void, Integer> {

        private MyWater mWater;

        public DBUpdateAsyncTask(MyWater water) {
            mWater = water;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            MyLog.i(TAG, "DBUpdateAsyncTask, doInBackground()");
            return updateMyWater(mWater);
        }

        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);

            if (count > 0) {
                MyLog.d(TAG, "DBUpdateAsyncTask, " + count + "개의 data update.");
            } else {
                MyLog.d(TAG, "DBUpdateAsyncTask, no data update.");
            }
        }
    }

    private class DBDeleteAsyncTask extends AsyncTask<Void, Void, Integer> {

        private MyWater water;

        public DBDeleteAsyncTask(MyWater water) {
            this.water = water;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            MyLog.i(TAG, "DBDeleteAsyncTask, doInBackground()");
            return deleteMyWater(water);
        }

        @Override
        protected void onPostExecute(Integer count) {
            super.onPostExecute(count);
            if (count > 0) {
                MyLog.d(TAG, "DBDeleteAsyncTask, " + count + "개의 data delete.");
            } else {
                MyLog.d(TAG, "DBDeleteAsyncTask, no data delete.");
            }
        }
    }
}
