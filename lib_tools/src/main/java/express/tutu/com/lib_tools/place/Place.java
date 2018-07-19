package express.tutu.com.lib_tools.place;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class Place implements Serializable {
    private static final long serialVersionUID = -6001899584729146107L;
    public static final String TABLE_NAME = "city";
    public static final String STATUS_NORMAL = "1";
    public static final String STATUS_REMOVED = "-1";//5.0&3.0

    public static final int DEPTH_COUNTRY = 0;
    public static final int DEPTH_PROVINCE = 1;
    public static final int DEPTH_CITY = 2;
    public static final int DEPTH_TOWN = 3;

    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_PARENT_ID = "ParentId";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_SHORT_NAME = "ShortName";
    public static final String COLUMN_DEEP = "Deep";
    public static final String COLUMN_GRAND_ID = "GrandId";
    public static final String COLUMN_UPDATE_TIME = "UpdateTime";
    public static final String COLUMN_LON = "Lon";
    public static final String COLUMN_LAT = "Lat";
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_PY_NAME = "PyName";

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"
            + Place.class.getName();

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"
            + Place.class.getName();

//    @SerializedName("id")
    private int code = 0;
//    @SerializedName("parentId")
    private int parentCode;
    private String name;
    private String shortName;
    private int deep;
    private double lon;
    private double lat;
    private String updateTime;
    private String status;
    private String grandId;
    private String pyName;

    public Place(int code, int parentCode, String name, String shortName,
                 int depth, double lon, double lat) {
        super();
        this.code = code;
        this.parentCode = parentCode;
        this.name = name;
        this.shortName = shortName;
        this.deep = depth;
        this.lon = lon;
        this.lat = lat;
    }

    private static Place UNKNOWN = null;

    public static Place getInvalidPlace() {
        if (UNKNOWN == null) {
            synchronized (Place.class) {
                if (UNKNOWN == null) {
                    UNKNOWN = new Place(-999, -999, "未知地点", "未知地点", 0, 0, 0);
                }
            }
        }
        return UNKNOWN;
    }

    //判断是否此城市有效。
    public boolean isValid() {
        return code >= 0 && code != getInvalidPlace().getCode();
    }

    public Place(Cursor c) {
        code = c.getInt(c.getColumnIndex(COLUMN_ID));
        parentCode = c.getInt(c.getColumnIndex(COLUMN_PARENT_ID));
        name = c.getString(c.getColumnIndex(COLUMN_NAME));
        shortName = c.getString(c.getColumnIndex(COLUMN_SHORT_NAME));
        deep = c.getInt(c.getColumnIndex(COLUMN_DEEP));
        lon = c.getDouble(c.getColumnIndex(COLUMN_LON));
        lat = c.getDouble(c.getColumnIndex(COLUMN_LAT));
        updateTime = c.getString(c.getColumnIndex(COLUMN_UPDATE_TIME));
        status = c.getString(c.getColumnIndex(COLUMN_STATUS));
        grandId = c.getString(c.getColumnIndex(COLUMN_GRAND_ID));
        pyName = c.getString(c.getColumnIndex(COLUMN_PY_NAME));
    }

    public Place(JSONObject o) {
        code = o.optInt("id");
        parentCode = o.optInt("parentId");
        name = o.optString("name");
        shortName = o.optString("shortName");
        deep = o.optInt("deep");
        grandId = o.optInt("grandId") + "";
        updateTime = o.optLong("updateTime") + "";
        status = o.optString("status");
        lat = o.optDouble("lat");
        lon = o.optDouble("lon");
        pyName = o.optString("pyName");
    }

    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, code);
        cv.put(COLUMN_PARENT_ID, parentCode);
        cv.put(COLUMN_SHORT_NAME, shortName);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_GRAND_ID, grandId);
        cv.put(COLUMN_DEEP, deep);
        cv.put(COLUMN_LAT, lat);
        cv.put(COLUMN_LON, lon);
        cv.put(COLUMN_UPDATE_TIME, updateTime);
        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_PY_NAME, pyName);
        return cv;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getParentCode() {
        return parentCode;
    }

    public void setParentCode(int parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getDepth() {
        return deep;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof Place) {
            Place targetPlace = (Place) o;
            return code == targetPlace.getCode();
        } else {
            return false;
        }
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrandId() {
        return grandId;
    }

    public void setGrandId(String grandId) {
        this.grandId = grandId;
    }

    public String getPyName() {
        return pyName;
    }

    public void setPyName(String pyName) {
        this.pyName = pyName;
    }

    @Override
    public int hashCode() {
        return code;
    }

}
