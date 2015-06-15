package me.yeojoy.watermanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import my.lib.MyLog;

public class DBHelper extends SQLiteOpenHelper implements DBConstants {

    private static final String TAG = DBHelper.class.getSimpleName();

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MyLog.i(TAG, "onCreate()");
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ").append(TABLE_NAME).append(" ( ");
        sb.append(ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(DATE).append(" TEXT, ");             // 저장시각
        sb.append(TIME).append(" TEXT, ");             // 시도 이름
        sb.append(QUANTITY).append(" INTEGER").append(" );");

        MyLog.e(TAG, "SQL CREATE -> " + sb.toString());
        db.execSQL(sb.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MyLog.i(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
