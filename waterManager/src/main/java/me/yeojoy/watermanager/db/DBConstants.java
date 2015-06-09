package me.yeojoy.watermanager.db;

import android.net.Uri;
import android.provider.BaseColumns;

import me.yeojoy.watermanager.BuildConfig;


public interface DBConstants {
    public static final String DB_NAME = "my_water.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_NAME = "WATER_MNG";
    
    public static final String ID               = BaseColumns._ID;
    public static final String DATE             = "DATE";
    public static final String TIME             = "TIME";
    public static final String QUANTITY         = "QUANTITY";

    public static final int INDEX_ID               = 0;
    public static final int INDEX_DATE             = 1;
    public static final int INDEX_TIME             = 2;
    public static final int INDEX_QUANTITY         = 3;

    // CursorLoader
    public static final int AIR_QUALITY_INDEX_CURSOR_LOADER = 1212;

    public static final String[] PROJECTION = {

    };

    public static final String SELECTION = DATE + " = ?";

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID;

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    public static final Uri AIR_QUALITY_SELECT_ALL_QUERY_URI =
            Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);

    public static final String CONTENT_TYPE = "vnd.watermanager.yeojoy.me.cursor.dir/" + TABLE_NAME;
}
