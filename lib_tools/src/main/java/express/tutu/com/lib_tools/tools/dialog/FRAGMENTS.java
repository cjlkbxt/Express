package express.tutu.com.lib_tools.tools.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class FRAGMENTS {
    public static final FRAGMENTS.All DEFAULT_ALL = new FRAGMENTS.All() {
        public void onActivityCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onAttach(FragmentManager fm, Fragment f, Context context) {
        }

        public void onCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        }

        public void onDestroy(FragmentManager fm, Fragment f) {
        }

        public void onDetach(FragmentManager fm, Fragment f) {
        }

        public void onPause(FragmentManager fm, Fragment f) {
        }

        public void onPreAttach(FragmentManager fm, Fragment f, Context context) {
        }

        public void onResume(FragmentManager fm, Fragment f) {
        }

        public void onSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        }

        public void onStart(FragmentManager fm, Fragment f) {
        }

        public void onStop(FragmentManager fm, Fragment f) {
        }

        public void onViewCreate(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        }

        public void onViewDestroy(FragmentManager fm, Fragment f) {
        }
    };

    public FRAGMENTS() {
    }

    public interface All extends FRAGMENTS.OnPreAttach, FRAGMENTS.OnAttach, FRAGMENTS.OnCreate, FRAGMENTS.OnActivityCreate, FRAGMENTS.OnViewCreate, FRAGMENTS.OnStart, FRAGMENTS.OnResume, FRAGMENTS.OnPause, FRAGMENTS.OnStop, FRAGMENTS.OnSaveInstanceState, FRAGMENTS.OnViewDestroy, FRAGMENTS.OnDestroy, FRAGMENTS.OnDetach {
    }

    public interface OnDetach {
        void onDetach(FragmentManager var1, Fragment var2);
    }

    public interface OnDestroy {
        void onDestroy(FragmentManager var1, Fragment var2);
    }

    public interface OnViewDestroy {
        void onViewDestroy(FragmentManager var1, Fragment var2);
    }

    public interface OnSaveInstanceState {
        void onSaveInstanceState(FragmentManager var1, Fragment var2, Bundle var3);
    }

    public interface OnStop {
        void onStop(FragmentManager var1, Fragment var2);
    }

    public interface OnPause {
        void onPause(FragmentManager var1, Fragment var2);
    }

    public interface OnResume {
        void onResume(FragmentManager var1, Fragment var2);
    }

    public interface OnStart {
        void onStart(FragmentManager var1, Fragment var2);
    }

    public interface OnViewCreate {
        void onViewCreate(FragmentManager var1, Fragment var2, View var3, Bundle var4);
    }

    public interface OnActivityCreate {
        void onActivityCreate(FragmentManager var1, Fragment var2, Bundle var3);
    }

    public interface OnCreate {
        void onCreate(FragmentManager var1, Fragment var2, Bundle var3);
    }

    public interface OnAttach {
        void onAttach(FragmentManager var1, Fragment var2, Context var3);
    }

    public interface OnPreAttach {
        void onPreAttach(FragmentManager var1, Fragment var2, Context var3);
    }
}
