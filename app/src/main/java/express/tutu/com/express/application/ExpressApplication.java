package express.tutu.com.express.application;

import android.app.Application;


/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class ExpressApplication extends Application {

    private static ExpressApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }

    public static ExpressApplication getInstance(){
        return  mApplication;
    }
}
