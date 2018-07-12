package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.All;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ActivityLifecycleProxy implements LifecycleListen, All {
    private static final String CLASS_ACTIVITY_THREAD = "android.app.ActivityThread";
    private static final String METHOD_CURRENT_ACTIVITY_THREAD = "currentActivityThread";
    private static final String FIELD_INSTRUMENTATION = "mInstrumentation";
    private static ActivityLifecycleProxy instance = new ActivityLifecycleProxy();
    private HashMap<All, Filter<? super Activity>> map = new HashMap();
    private ActivityLifecycleProxy.CallbacksDelegate callbacksDelegate;
    private ActivityLifecycleProxy.InstrumentationDelegate instrumentationDelegate;

    public ActivityLifecycleProxy() {
    }

    public static ActivityLifecycleProxy getInstance() {
        return instance;
    }

    void addListen(All lifecycle, Filter<? super Activity> filter) {
        this.map.put(lifecycle, filter);
    }

    void removeListen(All lifecycle) {
        this.map.remove(lifecycle);
    }

    private List<All> getHitList(Activity activity) {
        List<All> list = new ArrayList();
        Iterator var3 = this.map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<All, Filter<? super Activity>> entry = (Map.Entry)var3.next();
            if(((Filter)entry.getValue()).hit(activity)) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    private void clearDeadListen(Activity activity) {
        List<All> list = new ArrayList();
        Iterator var3 = this.map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<All, Filter<? super Activity>> entry = (Map.Entry)var3.next();
            if(((Filter)entry.getValue()).deadWith(activity)) {
                list.add(entry.getKey());
            }
        }

        var3 = list.iterator();

        while(var3.hasNext()) {
            All key = (All)var3.next();
            this.map.remove(key);
        }

    }

    public void onCreate(Activity activity, Bundle savedInstanceState) {
        Iterator var3 = this.getHitList(activity).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onCreate(activity, savedInstanceState);
        }

    }

    public void onStart(Activity activity) {
        Iterator var2 = this.getHitList(activity).iterator();

        while(var2.hasNext()) {
            All listen = (All)var2.next();
            listen.onStart(activity);
        }

    }

    public void onResume(Activity activity) {
        Iterator var2 = this.getHitList(activity).iterator();

        while(var2.hasNext()) {
            All listen = (All)var2.next();
            listen.onResume(activity);
        }

    }

    public void onPause(Activity activity) {
        Iterator var2 = this.getHitList(activity).iterator();

        while(var2.hasNext()) {
            All listen = (All)var2.next();
            listen.onPause(activity);
        }

    }

    public void onStop(Activity activity) {
        Iterator var2 = this.getHitList(activity).iterator();

        while(var2.hasNext()) {
            All listen = (All)var2.next();
            listen.onStop(activity);
        }

    }

    public void onSaveInstanceState(Activity activity, Bundle outState) {
        Iterator var3 = this.getHitList(activity).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onSaveInstanceState(activity, outState);
        }

    }

    public void onDestroy(Activity activity) {
        Iterator var2 = this.getHitList(activity).iterator();

        while(var2.hasNext()) {
            All listen = (All)var2.next();
            listen.onDestroy(activity);
        }

        this.clearDeadListen(activity);
    }

    public LifecycleListen listen() {
        if(Build.VERSION.SDK_INT >= 14) {
            if(this.callbacksDelegate == null) {
                this.callbacksDelegate = new ActivityLifecycleProxy.CallbacksDelegate(this);
                Lifecycle.getApplication().registerActivityLifecycleCallbacks(this.callbacksDelegate);
            }
        } else if(this.instrumentationDelegate == null) {
            try {
                Object activityThread = HookUtil.invoke("android.app.ActivityThread", "currentActivityThread", new Object[0]);
                Instrumentation instrumentation = (Instrumentation)HookUtil.get(activityThread, "mInstrumentation");
                this.instrumentationDelegate = new ActivityLifecycleProxy.InstrumentationDelegate(instrumentation, this);
                HookUtil.set(activityThread, "mInstrumentation", this.instrumentationDelegate);
            } catch (Exception var3) {
                ;
            }
        }

        return this;
    }

    public void quit() {
        if(Build.VERSION.SDK_INT >= 14) {
            if(this.callbacksDelegate != null) {
                Lifecycle.getApplication().unregisterActivityLifecycleCallbacks(this.callbacksDelegate);
                this.callbacksDelegate = null;
            }
        } else if(this.instrumentationDelegate != null) {
            try {
                Object activityThread = HookUtil.invoke("android.app.ActivityThread", "currentActivityThread", new Object[0]);
                HookUtil.set(activityThread, "mInstrumentation", this.instrumentationDelegate.getInstrumentation());
                this.instrumentationDelegate = null;
            } catch (Exception var2) {
                ;
            }
        }

    }

    private static class InstrumentationDelegate extends Instrumentation {
        private Instrumentation instrumentation;
        private All lifecycle;

        public InstrumentationDelegate(Instrumentation instrumentation, All lifecycle) {
            this.instrumentation = instrumentation;
            this.lifecycle = lifecycle;
        }

        public Instrumentation getInstrumentation() {
            return this.instrumentation;
        }

        public void callActivityOnCreate(Activity activity, Bundle icicle) {
            this.instrumentation.callActivityOnCreate(activity, icicle);
            this.lifecycle.onCreate(activity, icicle);
        }

        @RequiresApi(
                api = 21
        )
        public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
            this.instrumentation.callActivityOnCreate(activity, icicle, persistentState);
            this.lifecycle.onCreate(activity, icicle);
        }

        public void callActivityOnRestart(Activity activity) {
            this.instrumentation.callActivityOnRestart(activity);
        }

        public void callActivityOnStart(Activity activity) {
            this.instrumentation.callActivityOnStart(activity);
            this.lifecycle.onStart(activity);
        }

        public void callActivityOnResume(Activity activity) {
            this.instrumentation.callActivityOnResume(activity);
            this.lifecycle.onResume(activity);
        }

        public void callActivityOnPause(Activity activity) {
            this.instrumentation.callActivityOnPause(activity);
            this.lifecycle.onPause(activity);
        }

        public void callActivityOnStop(Activity activity) {
            this.instrumentation.callActivityOnStop(activity);
            this.lifecycle.onStop(activity);
        }

        public void callActivityOnDestroy(Activity activity) {
            this.instrumentation.callActivityOnDestroy(activity);
            this.lifecycle.onDestroy(activity);
        }

        public void callActivityOnNewIntent(Activity activity, Intent intent) {
            this.instrumentation.callActivityOnNewIntent(activity, intent);
        }

        public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
            this.instrumentation.callActivityOnPostCreate(activity, icicle);
        }

        @RequiresApi(
                api = 21
        )
        public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
            this.instrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
        }

        public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
            this.instrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState);
            this.lifecycle.onSaveInstanceState(activity, savedInstanceState);
        }
    }

    @RequiresApi(
            api = 14
    )
    private static class CallbacksDelegate implements Application.ActivityLifecycleCallbacks {
        private All lifecycle;

        public CallbacksDelegate(All lifecycle) {
            this.lifecycle = lifecycle;
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            this.lifecycle.onCreate(activity, savedInstanceState);
        }

        public void onActivityStarted(Activity activity) {
            this.lifecycle.onStart(activity);
        }

        public void onActivityResumed(Activity activity) {
            this.lifecycle.onResume(activity);
        }

        public void onActivityPaused(Activity activity) {
            this.lifecycle.onPause(activity);
        }

        public void onActivityStopped(Activity activity) {
            this.lifecycle.onStop(activity);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            this.lifecycle.onSaveInstanceState(activity, outState);
        }

        public void onActivityDestroyed(Activity activity) {
            this.lifecycle.onDestroy(activity);
        }
    }
}
