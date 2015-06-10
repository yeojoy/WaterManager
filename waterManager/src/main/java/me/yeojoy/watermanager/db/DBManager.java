package me.yeojoy.watermanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.Toast;

import me.yeojoy.watermanager.BuildConfig;
import me.yeojoy.watermanager.model.MyWater;
import my.lib.MyLog;


public class DBManager implements DBConstants {
    
    private static final String TAG = DBManager.class.getSimpleName();
    
    private static DBManager mManager;
    
    private static Context mContext;
    
    private static DBHelper mDBHelper;
    
    public static DBManager getInstance(Context context) {
        if (mManager == null)
            mManager = new DBManager();
        
        init(context); 
        return mManager;
    }
 
    private static void init(Context context) {
        MyLog.i(TAG, "init()");
        mContext = context;
        mDBHelper = new DBHelper(mContext);
    }

    public synchronized void saveData(MyWater data) {
        MyLog.i(TAG, "saveData()");
        if (data != null) {
            DBInsertAsyncTask task = new DBInsertAsyncTask();
            task.execute(data);
        }
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
     * @param MyWater
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
}