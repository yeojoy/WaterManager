package me.yeojoy.watermanager.db;

import android.net.Uri;
import android.provider.BaseColumns;

import me.yeojoy.watermanager.BuildConfig;


public interface DBConstants {
    String DB_NAME = "my_water.db";
    int DB_VERSION = 1;

    String TABLE_NAME = "WATER_MNG";
    
    String IDX = BaseColumns._ID;
    String DATE             = "DATE";
    String TIME             = "TIME";
    String QUANTITY         = "QUANTITY";

    int INDEX_ID               = 0;
    int INDEX_DATE             = 1;
    int INDEX_TIME             = 2;
    int INDEX_QUANTITY         = 3;

    // CursorLoader
    int AIR_QUALITY_INDEX_CURSOR_LOADER = 1212;

    String[] PROJECTION = {

    };

    String SELECTION = DATE + " = ?";

    // The URI scheme used for content URIs
    String SCHEME = "content";

    // The provider's authority
    String AUTHORITY = BuildConfig.APPLICATION_ID;

    /**
     * The DataProvider content URI
     */
    Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    Uri WATER_QUANTITY_SELECT_ALL_QUERY_URI =
            Uri.withAppendedPath(CONTENT_URI, TABLE_NAME);

    String CONTENT_TYPE = "vnd.watermanager.yeojoy.me.cursor.dir/" + TABLE_NAME;
}
