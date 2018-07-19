package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import express.tutu.com.lib_tools.utils.ContextUtil;
import express.tutu.com.lib_tools.utils.IOUtils;
import express.tutu.com.lib_tools.R;
/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class PlaceDatabaseSource extends SQLiteOpenHelper{
    private static final String DB_NAME = "place3";

    private static final PlaceDatabaseSource INSTANCE = new PlaceDatabaseSource(ContextUtil.get());

    static {
        copyDbFile(ContextUtil.get(), false);
    }

    public static PlaceDatabaseSource get() {
        return INSTANCE;
    }

    private PlaceDatabaseSource(Context context) {
        super(context, DB_NAME, null, 2018, new DatabaseErrorHandler() {

            private DefaultDatabaseErrorHandler defHandler = new DefaultDatabaseErrorHandler();

            @Override
            public void onCorruption(SQLiteDatabase dbObj) {
                defHandler.onCorruption(dbObj);
                copyDbFile(ContextUtil.get(), true);
            }
        });
    }

//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//        Cursor cursor = null;
//        try {
//            String sql = "select count(*) from sqlite_master where type ='table' and name ='" + Place.TABLE_NAME + "' ";
//            cursor = db.rawQuery(sql, null);
//            if (cursor.moveToFirst()) {
//                int count = cursor.getInt(0);
//                if (count == 0) {
//                    copyDbFile(context, true);
//                }
//            }
//        } catch (Exception e) {
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private static synchronized void copyDbFile(Context context, boolean force) {
        final File dbFile = context.getDatabasePath(DB_NAME);
        File parentFile = dbFile.getParentFile();
//        if (!(parentFile.exists() || parentFile.mkdirs())) {
//            YmmLogger.monitorLog().error().model("place").scenario("copy").param("error_msg", "fail to create dir: " + parentFile).enqueue();
//        }
        if (!dbFile.exists() || force) {
            copyDatabase(context, dbFile);
        }
    }

    private static void copyDatabase(Context context, File dbFile) {
//        if (dbFile.exists() && !dbFile.delete()) {
//            YmmLogger.monitorLog().error().model("place").scenario("copy").param("error_msg", "fail to delete: " + dbFile.getAbsolutePath() + ":" + dbFile.length()).enqueue();
//        }
        InputStream is = context.getResources().openRawResource(R.raw.place);
        FileOutputStream os = null;
        try {
            int count = 0;
            os = new FileOutputStream(dbFile);
            byte[] buffer = new byte[4096 * 3];
            int len;
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
                count += len;
            }
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
    }

}
