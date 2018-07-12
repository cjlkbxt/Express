package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.All;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnCreate;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnDestroy;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnPause;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnResume;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnSaveInstanceState;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnStart;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnStop;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ActivityLifecycleListen implements LifecycleListen{
    private OnCreate onCreate;
    private OnStart onStart;
    private OnResume onResume;
    private OnPause onPause;
    private OnStop onStop;
    private OnSaveInstanceState onSaveInstanceState;
    private OnDestroy onDestroy;
    private Filter<? super Activity> filter;
    private All impl;

    public ActivityLifecycleListen() {
        this.onCreate = ACTIVITIES.DEFAULT_ALL;
        this.onStart = ACTIVITIES.DEFAULT_ALL;
        this.onResume = ACTIVITIES.DEFAULT_ALL;
        this.onPause = ACTIVITIES.DEFAULT_ALL;
        this.onStop = ACTIVITIES.DEFAULT_ALL;
        this.onSaveInstanceState = ACTIVITIES.DEFAULT_ALL;
        this.onDestroy = ACTIVITIES.DEFAULT_ALL;
        this.filter = Filters.none();
    }

    public ActivityLifecycleListen onAll() {
        this.filter = Filter.ALL;
        return this;
    }

    public ActivityLifecycleListen on(@NonNull Activity activity) {
        this.filter = Filters.instance(activity);
        return this;
    }

    public ActivityLifecycleListen on(@NonNull Class<? extends Activity> clazz) {
        this.filter = Filters.type(clazz);
        return this;
    }

    public ActivityLifecycleListen on(@NonNull Filter<? super Activity> filter) {
        this.filter = filter;
        return this;
    }

    public ActivityLifecycleListen with(OnCreate onCreate) {
        this.onCreate = onCreate;
        return this;
    }

    public ActivityLifecycleListen with(OnStart onStart) {
        this.onStart = onStart;
        return this;
    }

    public ActivityLifecycleListen with(OnResume onResume) {
        this.onResume = onResume;
        return this;
    }

    public ActivityLifecycleListen with(OnPause onPause) {
        this.onPause = onPause;
        return this;
    }

    public ActivityLifecycleListen with(OnStop onStop) {
        this.onStop = onStop;
        return this;
    }

    public ActivityLifecycleListen with(OnSaveInstanceState onSaveInstanceState) {
        this.onSaveInstanceState = onSaveInstanceState;
        return this;
    }

    public ActivityLifecycleListen with(OnDestroy onDestroy) {
        this.onDestroy = onDestroy;
        return this;
    }

    public ActivityLifecycleListen with(All all) {
        this.onCreate = all;
        this.onStart = all;
        this.onResume = all;
        this.onPause = all;
        this.onStop = all;
        this.onSaveInstanceState = all;
        this.onDestroy = all;
        return this;
    }

    public LifecycleListen listen() {
        if(this.impl == null) {
            this.impl = new ActivityLifecycleListen.ActivityLifecycleImpl(this);
        }

        ActivityLifecycleProxy.getInstance().addListen(this.impl, this.filter);
        return this;
    }

    public void quit() {
        if(this.impl != null) {
            ActivityLifecycleProxy.getInstance().removeListen(this.impl);
        }
    }

    private static class ActivityLifecycleImpl implements All {
        private ActivityLifecycleListen listen;

        private ActivityLifecycleImpl(ActivityLifecycleListen listen) {
            this.listen = listen;
        }

        public void onCreate(Activity activity, Bundle savedInstanceState) {
            this.listen.onCreate.onCreate(activity, savedInstanceState);
        }

        public void onStart(Activity activity) {
            this.listen.onStart.onStart(activity);
        }

        public void onResume(Activity activity) {
            this.listen.onResume.onResume(activity);
        }

        public void onPause(Activity activity) {
            this.listen.onPause.onPause(activity);
        }

        public void onStop(Activity activity) {
            this.listen.onStop.onStop(activity);
        }

        public void onSaveInstanceState(Activity activity, Bundle outState) {
            this.listen.onSaveInstanceState.onSaveInstanceState(activity, outState);
        }

        public void onDestroy(Activity activity) {
            this.listen.onDestroy.onDestroy(activity);
        }
    }
}
