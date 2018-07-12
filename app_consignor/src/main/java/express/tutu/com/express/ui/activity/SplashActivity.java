package express.tutu.com.express.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import express.tutu.com.express.R;
import express.tutu.com.express.constants.Constant;
import express.tutu.com.lib_tools.utils.SpUtil;

/**
 * Created by cjlkbxt on 2018/7/11/011.
 * 闪屏页面
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 判断是否是第一次开启应用
        final boolean isFirstOpen = SpUtil.getBoolean(this, Constant.FIRST_OPEN, true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (isFirstOpen) {
                    // 如果是第一次启动app，则正常显示引导页面
                    enterWelcomeGuideActivity();
                } else {
                    // 如果不是第一次启动app，则直接进入主页面
                    enterHomeActivity();
                }

            }
        }, 2000);
    }

    private void enterWelcomeGuideActivity() {
        Intent intent = new Intent(this, WelcomeGuideActivity.class);
        startActivity(intent);
        finish();
    }

    private void enterHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
