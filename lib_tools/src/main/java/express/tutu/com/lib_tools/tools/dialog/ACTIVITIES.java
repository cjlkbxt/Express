package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ACTIVITIES {
    public static final ACTIVITIES.All DEFAULT_ALL = new ACTIVITIES.All() {
        public void onCreate(Activity activity, Bundle savedInstanceState) {
        }

        public void onDestroy(Activity activity) {
        }

        public void onPause(Activity activity) {
        }

        public void onResume(Activity activity) {
        }

        public void onSaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onStart(Activity activity) {
        }

        public void onStop(Activity activity) {
        }
    };

    public ACTIVITIES() {
    }

    public interface All extends ACTIVITIES.OnCreate, ACTIVITIES.OnStart, ACTIVITIES.OnResume, ACTIVITIES.OnPause, ACTIVITIES.OnStop, ACTIVITIES.OnSaveInstanceState, ACTIVITIES.OnDestroy {
    }

    public interface OnDestroy {
        void onDestroy(Activity var1);
    }

    public interface OnSaveInstanceState {
        void onSaveInstanceState(Activity var1, Bundle var2);
    }

    public interface OnStop {
        void onStop(Activity var1);
    }

    public interface OnPause {
        void onPause(Activity var1);
    }

    public interface OnResume {
        void onResume(Activity var1);
    }

    public interface OnStart {
        void onStart(Activity var1);
    }

    public interface OnCreate {
        void onCreate(Activity var1, Bundle var2);
    }
}
