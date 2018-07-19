package express.tutu.com.lib_tools.place;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.utils.ContextUtil;
import express.tutu.com.lib_tools.utils.LogUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class ValidPlaceManager extends PlaceManager{
    private static final String TAG = "ValidPlaceManager";

    @SuppressLint("StaticFieldLeak")
    private volatile static ValidPlaceManager instance;

    protected ValidPlaceManager(Context ctx) {
        super(ctx);
    }

    static ValidPlaceManager get() {
        if (instance == null) {
            synchronized (ValidPlaceManager.class) {
                if (instance == null) {
                    instance = new ValidPlaceManager(ContextUtil.get());
                }
            }
        }
        return instance;
    }

    public static ValidPlaceManager getInstance(Context ctx) {
        return PlaceManagerFactory.getValid();
    }

    @Override
    public List<Place> getChildren(int parentCode) {
        LogUtil.i(TAG + "===getChildren, code=" + parentCode);
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

    @Override
    public List<Place> getCityList(int deep, int parentCodeId) {
        LogUtil.i(TAG + "===getCityList,code=" + parentCodeId);
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
            }

            if (cursor != null) {
                List<Place> citys = PlacesDao.loadCitysFromCursor(cursor);

                return citys;
            }
            return placeList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<Place> getPlaceListByKey(String deep, String key) {
        LogUtil.i(TAG + "===getPlaceListByKey, key=" + key);
        List<Place> placeList = new ArrayList<>();
        if (TextUtils.isEmpty(deep) || TextUtils.isEmpty(key) || key.contains(",") || key.contains("%")) {
            return placeList;
        }
//        if (db == null || !db.isOpen()) {
//            db = PlaceDatabaseHelper.initPlaceDatabase(ctx);
//        }
        if (getReadableDatabase() == null) {
            return placeList;
        }
        Cursor cursor = null;
        try {
            switch (deep) {
                case "2":
                    cursor = getReadableDatabase().query(Place.TABLE_NAME, null, Place.COLUMN_DEEP + "=? and (" + Place.COLUMN_SHORT_NAME + " like ? or " + Place.COLUMN_PY_NAME + " like ?)",
                            new String[]{String.valueOf(Place.DEPTH_CITY), "%" + key + "%", "%" + key + "%"}, null, null, null);
                    break;
                case "3":
                    cursor = getReadableDatabase().query(Place.TABLE_NAME, null, Place.COLUMN_DEEP + "=? and (" + Place.COLUMN_SHORT_NAME + " like ? or " + Place.COLUMN_PY_NAME + " like ?)",
                            new String[]{String.valueOf(Place.DEPTH_TOWN), "%" + key + "%", "%" + key + "%"}, null, null, null);
                    break;
            }
            if (cursor != null) {
                List<Place> citys = PlacesDao.loadCitysFromCursor(cursor);

                return citys;
            }
            return placeList;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
