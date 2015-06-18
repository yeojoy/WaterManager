package me.yeojoy.watermanager.config;


public interface Consts {
    /** SharedPreferences name */
    String PREFS_NAME = "water_mgr";
    
    /** SharedPreferences key name */
    String PREFS_KEY_COUNTS = "w_counts";

    String PREFS_KEY_DAILY_GOAL_QUANTITY = "quantity";
    
    String BIG_CUP_ACTION = "me.yeojoy.action.BIG_CUP";
    String MEDIUM_CUP_ACTION = "me.yeojoy.action.MEDIUM_CUP";
    String SMALL_CUP_ACTION = "me.yeojoy.action.SMALL_CUP";

    int BIG_CUP_ID          = 0x000111;
    int MEDIUM_CUP_ID       = 0x000011;
    int SMALL_CUP_ID        = 0x000001;

}
