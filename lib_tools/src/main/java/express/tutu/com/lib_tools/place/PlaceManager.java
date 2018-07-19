package express.tutu.com.lib_tools.place;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import express.tutu.com.lib_tools.utils.CleanUtils;
import express.tutu.com.lib_tools.utils.CollectionUtil;
import express.tutu.com.lib_tools.utils.ContextUtil;
import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceManager implements PlaceDataInterface{
    private static final String TAG = "PlaceManager";
    private static final int MAX_SIZE_CACHE = 255;

    @SuppressLint("StaticFieldLeak")
    private volatile static PlaceManager instance;

    private LruCache<Integer, Place> mLruCache;
//    private int mSearchCount = 0;
//    private int mHitCount = 0;

    protected Context ctx;

    protected PlaceManager(Context ctx) {
        init(ctx);
    }

    protected void init(Context context) {
        this.ctx = context.getApplicationContext();
        mLruCache = new LruCache<>(MAX_SIZE_CACHE);
    }

    static PlaceManager get() {
        if (instance == null) {
            synchronized (PlaceManager.class) {
                if (instance == null) {
                    instance = new PlaceManager(ContextUtil.get());
                }
            }
        }
        return instance;
    }

    public static PlaceManager getInstance(Context ctx) {
        return PlaceManagerFactory.getNormal();
    }

    /**
     * @param codes
     * @return
     */
    public List<Place> getPlaceList(List<String> codes) {
        if (CollectionUtil.isEmpty(codes)) return null;
        String[] codeAry = codes.toArray(new String[]{});
        StringBuilder selection = new StringBuilder();
        selection.append(Place.COLUMN_ID);
        selection.append(" in (");
        for (int i = 0; i < codes.size(); i++) {
            if (i > 0) selection.append(",");
            selection.append("?");
        }
        selection.append(")");

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(Place.TABLE_NAME, null, selection.toString(), codeAry, null, null, null);
            List<Place> placeList;
            placeList = PlacesDao.loadCitysFromCursor(cursor);
            return placeList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @NonNull
    public Place getPlace(int code) {
//        mSearchCount ++;

        Place cache = mLruCache.get(code);
        if (cache != null) {
//            mHitCount ++;
//            LogUtil.d(TAG, "search: " + mSearchCount + " hit: " + mHitCount + " hit rate: " + ((float) mHitCount / (float) mSearchCount));
            return cache;
        }

//        if (db == null || !db.isOpen()) {
//            db = PlaceDatabaseHelper.initPlaceDatabase(ctx);
//        }
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(Place.TABLE_NAME, null, Place.COLUMN_ID + "=?",
                    new String[]{code + ""}, null, null, null);
            Place city = Place.getInvalidPlace();
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst())
                city = PlacesDao.loadCityFromCursor(cursor);

            mLruCache.put(code, city);

//        LogUtil.d(TAG, "search: " + mSearchCount + " hit: " + mHitCount + " hit rate: " + ((float) mHitCount / (float) mSearchCount));
            return city;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected SQLiteDatabase getWritableDatabase() {
        return PlaceDatabaseSource.get().getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return PlaceDatabaseSource.get().getWritableDatabase();
//        return PlaceDatabaseSource.get().getReadableDatabase();
    }

    public Place getNationRootPlace() {
        return getPlace(0);
    }

    public Place getParent(Place child) {
        if (child == null) return Place.getInvalidPlace();

        if (child.getDepth() == 0) {
            return Place.getInvalidPlace();
        }

        return getPlace(child.getParentCode());
    }

    // This method is not recommended.
    // TODO: Use getParent() instead if possible.
    public Place getParentCity(Place child) {
        if (child == null) return getNationRootPlace();

        if (child.getDepth() == 0) {
            return getNationRootPlace();
        }

        if (child.getDepth() == 1) {
            return getNationRootPlace();
        }

        return getPlace(child.getParentCode());
    }

    public List<Place> getPlaceChain(Place place) {
        if (place.getDepth() == 0 || place.getDepth() == 1) {
            return new ArrayList<>();
        } else {
            ArrayList<Place> cityList = new ArrayList<>();
            Place parentCity;
            Place startCity = place;
            do {
                parentCity = getParentCity(startCity);
                if (parentCity != null && parentCity.isValid()) {
                    cityList.add(parentCity);
                }
                startCity = parentCity;
            } while (parentCity != null);
            return cityList;
        }
    }

    public List<Place> getSiblingsPlaces(Place p) {
        if (p.getCode() == 0) {
            return new ArrayList<>();
        } else {
            Place parent = getParent(p);
            if (parent != null && parent.isValid()) {
                return getChildren(parent.getCode());
            } else {
                return new ArrayList<>();
            }
        }
    }

    public boolean hasChildren(Place place) {
        return hasChildren(place.getCode());
    }

    public boolean hasChildren(int parentCode) {
        List<Place> list = getChildren(parentCode);
        return list != null && list.size() > 0;
    }

    public List<Place> getChildren(int parentCode) {
//        if (db == null || !db.isOpen()) {
//            db = PlaceDatabaseHelper.initPlaceDatabase(ctx);
//        }
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(Place.TABLE_NAME, null, Place.COLUMN_PARENT_ID + "=? and " + Place.COLUMN_STATUS + "=?",
                    new String[]{parentCode + "", Place.STATUS_NORMAL}, null, null, null);
            List<Place> children = PlacesDao.loadCitysFromCursor(cursor);
            return children;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Place> getCityList(int deep, int parentCodeId) {
        List<Place> placeList = new ArrayList<>();
        if (deep < 0) return placeList;

//        if (db == null || !db.isOpen()) {
//            db = PlaceDatabaseHelper.initPlaceDatabase(ctx);
//        }
        if (getReadableDatabase() == null) {
            return placeList;
        }
        Cursor cursor = null;
        try {
            switch (deep) {
                case Place.DEPTH_PROVINCE:
                    cursor = getReadableDatabase().query(Place.TABLE_NAME, null, Place.COLUMN_DEEP + "=? and " + Place.COLUMN_STATUS + "=?",
                            new String[]{String.valueOf(Place.DEPTH_PROVINCE), Place.STATUS_NORMAL}, null, null, null);
                    break;
                case Place.DEPTH_CITY:
                case Place.DEPTH_TOWN:
                    cursor = getReadableDatabase().query(Place.TABLE_NAME, null,
                            Place.COLUMN_DEEP + "=? and " + Place.COLUMN_PARENT_ID + "=? and " + Place.COLUMN_STATUS + "=?",
                            new String[]{deep + "", parentCodeId + "", Place.STATUS_NORMAL}, null, null, null
                    );
                    break;
                default:
                    return placeList;
            }

            if (cursor != null) {
                List<Place> citys = PlacesDao.loadCitysFromCursor(cursor);
                return citys;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return placeList;
    }

    /**
     * Method Name: @param province  省份名称
     * Method Name: @param city 城市名称
     * Method Name: @param district 地区
     */
    public Place getCityByString(String province, String city,
                                 String district) {
        if (TextUtils.isEmpty(province)) {
            return Place.getInvalidPlace();
        }
        Place provincePlace = null;
        Place cityPlace = null;
//		Place districtPlace = null;
        List<Place> mProvinces = getCityList(1, 0);
        if (mProvinces == null) {
            return Place.getInvalidPlace();
        }
        for (Place provinceItem : mProvinces) {
            String name = provinceItem.getName();
            String nameForShort = name.substring(0, 2);
            if (province.subSequence(0, 2).equals(nameForShort)) {
                provincePlace = provinceItem;
            }
        }
        if (provincePlace != null) {
            List<Place> mCities = getCityList(2, provincePlace.getCode());
            if (mCities.size() == 1) {
                cityPlace = mCities.get(0);
            } else {
                for (Place p : mCities) {
                    String name = p.getName();
                    //如果名字为空
                    if (TextUtils.isEmpty(name))
                        continue;
                    if (name.length() < 2)
                        continue;
                    String nameForShort = name.substring(0, 2);
                    if (city.substring(0, 2).equals(nameForShort)) {
                        cityPlace = p;
                        break;
                    }
                }
            }
        } else {
            return Place.getInvalidPlace();
        }
        if (cityPlace != null) {
//			List<Place> districts = getCityList(3, cityPlace.getCode());
//			for(Place p : districts){
//				String name = p.getName();
//				if (name.equals(district)) {
//					districtPlace = p;
//					break;
//				}
//			}
            return cityPlace;
        } else {
            return provincePlace;
        }
//		if (districtPlace != null) {
//			return districtPlace;
//		}else {
//			return cityPlace;
//		}
    }

    public String getPlaceName(int code) {
        return getPlace(code).getName();
    }

    public String getPlaceShortName(int code) {
        return getPlace(code).getShortName();
    }

    @Nullable
    public String getLonLatStr(int city) {
//        if (db == null || !db.isOpen()) {
//            db = PlaceDatabaseHelper.initPlaceDatabase(ctx);
//        }
        Cursor c = getReadableDatabase().query("city", null, "Id=?", new String[]{city + ""}, null, null, null);
        try {
            if (c != null && c.getCount() > 0 && c.moveToFirst()) {
                Place p = PlacesDao.loadCityFromCursor(c);
                return p.getLat() + "," + p.getLon();
            }
            return null;
        } finally {
            CleanUtils.closeCursor(c);
        }
    }

    /**
     * 获取父级-子级的地区名数组，如果是直辖市只返回1个
     */
    public String[] getParent_ChildName(int id) {
        String[] retArray;
        Place self = getPlace(id);
        int deep = self.getDepth();
        if (deep <= 1) {
            retArray = new String[1];
            retArray[0] = self.getShortName();
        } else {
            Place parent = getParent(self);
            if (parent.getDepth() == 0 || parent.getShortName().equals(self.getShortName()) || parent.getDepth() == 1) {
                retArray = new String[1];
                retArray[0] = self.getShortName();
            } else {
                retArray = new String[2];
                retArray[0] = parent.getShortName();
                retArray[1] = self.getShortName();
            }
        }
        return retArray;
    }

    public String getFullCityNameByCode(int code, String separator) {
        Place child = getPlace(code);
        Place parent = getParent(child);
        if ("未知".equals(parent.getName()) || parent.getName().equals(child.getName())) {
            return child.getShortName();
        } else {
            if (parent.getCode() == 0 || parent.getDepth() == Place.DEPTH_PROVINCE) {
                //如果上一级是全国，则只显示省份。
                return child.getShortName();
            } else {
                return parent.getShortName() + separator + child.getShortName();
            }
        }
    }

    /**
     * 真正的根据code获取 省份+城市 名称，其它方法都是骗人的
     *
     * @param code
     * @param separator
     * @return
     */
    public String getPlaceNameWithProvince(int code, String separator) {
        Place child = getPlace(code);
        Place parent = getParent(child);
        if ("未知".equals(parent.getName()) || parent.getName().equals(child.getName())) {
            return child.getName();
        } else {
            if (parent.getCode() == 0 || parent.getDepth() == Place.DEPTH_COUNTRY) {
                //如果上一级是全国，则只显示省份。
                return child.getName();
            } else {
                return parent.getName() + separator + child.getName();
            }
        }
    }

    /**
     * 获取从place开始以此获取到parent，从高到底塞入list
     *
     * @param place
     * @return
     */
    public List<Place> getLinealPlaceList(Place place) {
        ArrayList<Place> mList = new ArrayList<>();
        Place temp = place;
        do {
            mList.add(mList.size(), temp);
            temp = getParent(temp);
        } while (temp != null && temp.isValid());
        Collections.reverse(mList);
        return mList;
    }

    /**
     * 根据code获取所有省+市+区
     *
     * @param code
     * @param separator
     * @return
     */
    public String getFullPlaceName(int code, String separator) {
        return getFullPlaceName(getPlace(code), separator);
    }

    /**
     * 获取全省、市、区的shortName
     *
     * @param place
     * @param separator
     * @return
     */
    public String getFullPlaceName(Place place, String separator) {
        List<Place> mSelectedList = getLinealPlaceList(place);
        if (mSelectedList.size() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            boolean isFullWithCity = false;
            Place prePlace = null;
            for (Place city : mSelectedList) {
                // 说明这个是选项是不限
                if (city.getCode() == 0) {
                    continue;
                }
                if (prePlace != null && prePlace.getShortName().equals(city.getShortName())) {
                    //Do not add city.
                } else {
                    sBuilder.append(city.getShortName());
                    sBuilder.append(separator);
                }
                isFullWithCity = true;
                prePlace = city;
            }
            if (isFullWithCity) {
                return sBuilder.substring(0, sBuilder.length() - 1);
            } else {
                return ctx.getString(R.string.nationwide);
            }
        }
        return ctx.getString(R.string.nationwide);
    }

    /**
     * 根据code获取所有省+市+区
     *
     * @param code
     * @param separator
     * @return
     */
    public String getFullPlaceFullName(int code, String separator) {
        return getFullPlaceFullName(getPlace(code), separator);
    }

    /**
     * 获取全省、市、区的fullname
     *
     * @param place
     * @param separator
     * @return
     */
    public String getFullPlaceFullName(Place place, String separator) {
        List<Place> mSelectedList = getLinealPlaceList(place);
        if (mSelectedList.size() > 0) {
            StringBuilder sBuilder = new StringBuilder();
            boolean isFullWithCity = false;
            Place prePlace = null;
            for (Place city : mSelectedList) {
                // 说明这个是选项是不限
                if (city.getCode() == 0) {
                    continue;
                }
                if (prePlace != null && prePlace.getName().equals(city.getName())) {
                    //Do not add city.
                } else {
                    sBuilder.append(city.getName());
                    sBuilder.append(separator);
                }
                isFullWithCity = true;
                prePlace = city;
            }
            if (isFullWithCity) {
                return sBuilder.substring(0, sBuilder.length() - 1);
            } else {
                return ctx.getString(R.string.nationwide);
            }
        }
        return ctx.getString(R.string.nationwide);
    }

    public String getFullCityNameByCode(int code) {
        return getFullCityNameByCode(code, "");
    }

    /**
     * 市+区县(直辖市特别行政区只显示一级)
     */
    public String getFormattedPlaceName(Place place) {
        return getFormattedPlaceName(place, " ");
    }

    public String getFormattedPlaceName(Place place, String separator) {
        if (place == null || !place.isValid()) return "未知";

        String result;
        if (place.getDepth() == 2) {
            result = place.getShortName();
        } else {
            Place city = getParentCity(place);
            if (city != null) {
                result = city.getShortName() + separator + place.getShortName();
            } else {
                result = place.getShortName();
            }
        }
        return result;
    }

    public String getParentChildStr(int cityCode) {
        Place self = getPlace(cityCode);
        Place parent = getParent(self);
        if (self.getShortName().equals(parent.getShortName())) {
            return self.getShortName();
        }
        if (parent.isValid() && parent.getDepth() > 1) {
            return parent.getShortName().concat("-").concat(self.getShortName());
        }
        return self.getShortName();
    }

    public String getParentSpaceChildStr(int cityCode) {
        Place self = getPlace(cityCode);
        Place parent = getParent(self);
        if (self.getShortName().equals(parent.getShortName())) {
            return self.getShortName();
        }
        if (parent.isValid() && parent.getDepth() > 1) {
            return parent.getShortName().concat(" ").concat(self.getShortName());
        }
        return self.getShortName();
    }

    public String getFullName(int cityCode) {
        Place self = getPlace(cityCode);
        Place parent = getParent(self);
        if (self.getShortName().equals(parent.getShortName())) {
            return self.getShortName();
        }
        if (parent.isValid() && parent.getDepth() > Place.DEPTH_PROVINCE) {
            return parent.getShortName().concat(self.getShortName());
        }
        return self.getShortName();
    }

    /**
     * 判断是否是未知城市
     */
    public boolean isInvalidateCity(int city) {
        return getLonLatStr(city) == null;
    }
}
