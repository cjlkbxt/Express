package express.tutu.com.express.ui.activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import express.tutu.com.express.constants.Constant;

/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class BaseActivity extends AppCompatActivity {

    public String TAG;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        TAG = getComponentName().getShortClassName();
    }

    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, permissions, code);
        }
    }

    /**
     * 判断是否有指定的权限
     */
    public boolean hasPermission(String... permissions) {

        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(this, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.WRITE_READ_EXTERNAL_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    doSDCardPermission();
                }
                break;
        }
    }

    /**
     * 处理整个应用用中的SDCard业务
     */
    public void doSDCardPermission() {
    }
}
