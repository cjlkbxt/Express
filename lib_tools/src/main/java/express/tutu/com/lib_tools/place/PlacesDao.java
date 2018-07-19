package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import express.tutu.com.lib_tools.utils.CleanUtils;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlacesDao {
    private static PlacesDao instance = new PlacesDao();

    public static PlacesDao getInstance() {
        return instance;
    }

    public long getMaxUpdateTimeInDB(Context ctx) {
        long updateTime = 0;
        SQLiteDatabase sqLiteDatabase = PlaceDatabaseSource.get().getWritableDatabase();
        if (sqLiteDatabase == null) return updateTime;
        Cursor c = sqLiteDatabase.rawQuery("select max(" + Place.COLUMN_UPDATE_TIME + ") from " + Place.TABLE_NAME, null);
        try {
            if (c != null && c.moveToNext()) {
                updateTime = c.getLong(c.getColumnIndex("max(" + Place.COLUMN_UPDATE_TIME + ")"));
            }
            return updateTime;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CleanUtils.closeCursor(c);
        }
        return updateTime;
    }

    public static List<Place> loadCitysFromCursor(Cursor c) {
        List<Place> citys = new ArrayList<>();
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                Place city = loadCityFromCursor(c);
                if (city.isValid()) {
                    citys.add(city);
                }
            }
            Collections.sort(citys, new Comparator<Place>() {
                @Override
                public int compare(Place o1, Place o2) {
                    return o1.getCode() - o2.getCode();
                }
            });
            return citys;
        }
        return citys;
    }

    public static Place loadCityFromCursor(Cursor c) {
        if (c != null && c.getCount() > 0) {
            return new Place(c);
        }
        return Place.getInvalidPlace();
    }
}
