package express.tutu.com.lib_tools.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import express.tutu.com.lib_tools.place.DaoMaster;
import express.tutu.com.lib_tools.place.DaoSession;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class DbUtil {

    private static final String DB_NAME = "history.db";
    private static final int DB_VERSION = 1;
    private static final String DB_AUTH = "com.ymm.biz.cargo.impl.driver.history.contentprovider";
    private volatile static DaoSession sDaoSession;

    /**
     * 获得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (sDaoSession == null) {
            synchronized (DbUtil.class) {
                if (sDaoSession == null) {
                    DaoMaster daoMaster = new DaoMaster(new DaoMaster.OpenHelper(context, DB_NAME,null ){
                        @Override
                        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                        }
                    }.getWritableDatabase());
                    sDaoSession = daoMaster.newSession();
                }
            }
        }
        return sDaoSession;
    }
}
