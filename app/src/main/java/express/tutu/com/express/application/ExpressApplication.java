package express.tutu.com.express.application;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;


/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class ExpressApplication extends Application {

    private static ExpressApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initLeakCanary();
    }

    public static ExpressApplication getInstance(){
        return  mApplication;
    }

    private void initLeakCanary(){
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
