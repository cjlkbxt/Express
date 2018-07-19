package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import express.tutu.com.lib_tools.utils.DbUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceModel {
    private Context mContext;
    private PlaceManager mPlaceManager;
    private DaoSession mDaoSession;

    public PlaceModel(Context context, boolean filterValidPlace, boolean isShowHistory) {
        this.mContext = context;
        mPlaceManager = filterValidPlace ? ValidPlaceManager.getInstance(context) : PlaceManager.getInstance(context);
        if(isShowHistory) {
            mDaoSession = DbUtil.getDaoSession(context);
        }
    }

    /**
     * @param place
     * @return place及所有上级
     */
    public List<Place> getLinealPlaceList(Place place) {
        return mPlaceManager.getLinealPlaceList(place);
    }

    public String getFullPlaceName(Place place) {
        return mPlaceManager.getFullPlaceName(place, "-");
    }

    public List<Place> getPlaceList(List<String> placeCodes) {
        return mPlaceManager.getPlaceList(placeCodes);
    }

    public Place getPlace(int placeCode) {
        return mPlaceManager.getPlace(placeCode);
    }

    public Place getNationRootPlace() {
        return mPlaceManager.getNationRootPlace();
    }

    /**
     * 保存此次选择到历史当中
     * @param position 标明不同的地址选择历史
     */
    public void storeToHistory(Place mCurrentPlace, String position) {
        //position不为空，有效城市，且不是全国。
        if (TextUtils.isEmpty(position)
                || mCurrentPlace == null
                || !mCurrentPlace.isValid()
                || mCurrentPlace.getCode() == 0) return;

        if (mDaoSession != null) {
            CityPickerHistory cph = new CityPickerHistory();
            cph.setCity(mCurrentPlace.getCode());
            cph.setPosition(position);
            cph.setPosition_city(String.format("%s:%s", position, mCurrentPlace.getCode() + ""));
            cph.setTime(new Date(System.currentTimeMillis()));
            mDaoSession.insertOrReplace(cph);
        }
    }

    /**
     * 获取最近的选择历史
     *
     * @param recentCount 最近的数量。
     * @return 最多返回最近recentCount条选择历史。
     */
    public List<Place> getRecentHistory(int recentCount, String position) {
        if (mDaoSession != null && mContext != null) {
            PlaceManager manager = PlaceManager.getInstance(mContext);
            List<CityPickerHistory> histories = mDaoSession.queryBuilder(CityPickerHistory.class)
                    .where(CityPickerHistoryDao.Properties.Position.eq(position))
                    .orderDesc(CityPickerHistoryDao.Properties.Time)
                    .limit(recentCount).list();
            ArrayList<Place> places = new ArrayList<>();
            for (CityPickerHistory history : histories) {
                Place p = manager.getPlace(history.getCity());
                if (p != null && p.isValid()) {
                    places.add(p);
                }
            }
            return places;
        }
        return null;
    }

    /**
     * 是否是全国
     * @param place
     * @return
     */
    public boolean isNation(Place place) {
        if(place != null && place.getCode() == 0) return true;
        return false;
    }
}
