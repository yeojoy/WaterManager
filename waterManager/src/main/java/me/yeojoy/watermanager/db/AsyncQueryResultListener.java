package me.yeojoy.watermanager.db;

import java.util.List;

import me.yeojoy.watermanager.model.MyWater;

/**
 * Created by yeojoy on 15. 6. 11..
 */
public interface AsyncQueryResultListener {
    public void onQueryResult(List<MyWater> myWaterResult);
}
