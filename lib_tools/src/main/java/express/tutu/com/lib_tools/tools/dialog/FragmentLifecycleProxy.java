package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.All;
import express.tutu.com.lib_tools.tools.dialog.ACTIVITIES.OnCreate;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class FragmentLifecycleProxy implements LifecycleListen, All {
    private static FragmentLifecycleProxy instance = new FragmentLifecycleProxy();
    private HashMap<All, Filter<? super Fragment>> map = new HashMap();
    private WeakHashMap<Fragment, List<All>> deadMap = new WeakHashMap();
    private LifecycleListen activityListen;
    private FragmentLifecycleProxy.CallbacksDelegate callbacksDelegate;

    public FragmentLifecycleProxy() {
    }

    public static FragmentLifecycleProxy getInstance() {
        return instance;
    }

    void addListen(All lifecycle, Filter<? super Fragment> filter) {
        this.map.put(lifecycle, filter);
    }

    void removeListen(All lifecycle) {
        this.map.remove(lifecycle);
    }

    public LifecycleListen listen() {
        if(this.activityListen == null) {
            this.activityListen = (new ActivityLifecycleListen()).onAll().with(new OnCreate() {
                public void onCreate(Activity activity, Bundle savedInstanceState) {
                    if(activity instanceof FragmentActivity) {
                        if(FragmentLifecycleProxy.this.callbacksDelegate == null) {
                            FragmentLifecycleProxy.this.callbacksDelegate = new FragmentLifecycleProxy.CallbacksDelegate(FragmentLifecycleProxy.this);
                        }

                        ((FragmentActivity)activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(FragmentLifecycleProxy.this.callbacksDelegate, true);
                    }
                }
            }).listen();
        }

        return this;
    }

    public void quit() {
        if(this.activityListen != null) {
            this.activityListen.quit();
            this.activityListen = null;
        }

    }

    private List<All> getHitList(FragmentManager fm, Fragment fragment) {
        List<All> list = new ArrayList();
        Iterator var4 = this.map.entrySet().iterator();

        while(var4.hasNext()) {
            Map.Entry<All, Filter<? super Fragment>> entry = (Map.Entry)var4.next();
            if(((Filter)entry.getValue()).hit(fragment)) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    private List<All> getDeadList(Fragment fragment) {
        List<All> list = new ArrayList();
        Iterator var3 = this.map.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<All, Filter<? super Fragment>> entry = (Map.Entry)var3.next();
            if(((Filter)entry.getValue()).deadWith(fragment)) {
                list.add(entry.getKey());
            }
        }

        return list;
    }

    public void onPreAttach(FragmentManager fm, Fragment f, Context context) {
        Iterator var4 = this.getHitList(fm, f).iterator();

        while(var4.hasNext()) {
            All listen = (All)var4.next();
            listen.onPreAttach(fm, f, context);
        }

    }

    public void onAttach(FragmentManager fm, Fragment f, Context context) {
        Iterator var4 = this.getHitList(fm, f).iterator();

        while(var4.hasNext()) {
            All listen = (All)var4.next();
            listen.onAttach(fm, f, context);
        }

    }

    public void onCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        Iterator var4 = this.getHitList(fm, f).iterator();

        while(var4.hasNext()) {
            All listen = (All)var4.next();
            listen.onCreate(fm, f, savedInstanceState);
        }

    }

    public void onActivityCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        Iterator var4 = this.getHitList(fm, f).iterator();

        while(var4.hasNext()) {
            All listen = (All)var4.next();
            listen.onActivityCreate(fm, f, savedInstanceState);
        }

    }

    public void onViewCreate(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        Iterator var5 = this.getHitList(fm, f).iterator();

        while(var5.hasNext()) {
            All listen = (All)var5.next();
            listen.onViewCreate(fm, f, v, savedInstanceState);
        }

    }

    public void onStart(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onStart(fm, f);
        }

    }

    public void onResume(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onResume(fm, f);
        }

    }

    public void onPause(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onPause(fm, f);
        }

    }

    public void onStop(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onStop(fm, f);
        }

    }

    public void onSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        Iterator var4 = this.getHitList(fm, f).iterator();

        while(var4.hasNext()) {
            All listen = (All)var4.next();
            listen.onSaveInstanceState(fm, f, outState);
        }

    }

    public void onViewDestroy(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onViewDestroy(fm, f);
        }

    }

    public void onDestroy(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onDestroy(fm, f);
        }

        List<All> deadList = (List)this.deadMap.get(f);
        if(deadList == null) {
            deadList = new ArrayList();
        }

        ((List)deadList).addAll(this.getDeadList(f));
        this.deadMap.put(f, deadList);
    }

    public void onDetach(FragmentManager fm, Fragment f) {
        Iterator var3 = this.getHitList(fm, f).iterator();

        while(var3.hasNext()) {
            All listen = (All)var3.next();
            listen.onDetach(fm, f);
        }

        List<All> deadList = (List)this.deadMap.get(f);
        if(deadList != null) {
            Iterator var7 = deadList.iterator();

            while(var7.hasNext()) {
                All listen = (All)var7.next();
                this.map.remove(listen);
            }

        }
    }

    static class Bound extends FragmentLifecycleProxy {
        private FragmentManager fm;
        private boolean recursive;
        private FragmentLifecycleProxy.CallbacksDelegate callbacksDelegate;

        public Bound(FragmentManager fm, boolean recursive) {
            this.fm = fm;
            this.recursive = recursive;
        }

        public FragmentLifecycleProxy.Bound listen() {
            if(this.callbacksDelegate == null) {
                this.callbacksDelegate = new FragmentLifecycleProxy.CallbacksDelegate(this);
            }

            this.fm.registerFragmentLifecycleCallbacks(this.callbacksDelegate, this.recursive);
            return this;
        }

        public void quit() {
            this.fm.unregisterFragmentLifecycleCallbacks(this.callbacksDelegate);
        }
    }

    private static class CallbacksDelegate extends FragmentManager.FragmentLifecycleCallbacks {
        private All lifecycle;

        public CallbacksDelegate(All lifecycle) {
            this.lifecycle = lifecycle;
        }

        public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentPreAttached(fm, f, context);
            this.lifecycle.onPreAttach(fm, f, context);
        }

        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentAttached(fm, f, context);
            this.lifecycle.onAttach(fm, f, context);
        }

        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            this.lifecycle.onCreate(fm, f, savedInstanceState);
        }

        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState);
            this.lifecycle.onActivityCreate(fm, f, savedInstanceState);
        }

        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            this.lifecycle.onViewCreate(fm, f, v, savedInstanceState);
        }

        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            super.onFragmentStarted(fm, f);
            this.lifecycle.onStart(fm, f);
        }

        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);
            this.lifecycle.onResume(fm, f);
        }

        public void onFragmentPaused(FragmentManager fm, Fragment f) {
            super.onFragmentPaused(fm, f);
            this.lifecycle.onPause(fm, f);
        }

        public void onFragmentStopped(FragmentManager fm, Fragment f) {
            super.onFragmentStopped(fm, f);
            this.lifecycle.onStop(fm, f);
        }

        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);
            this.lifecycle.onSaveInstanceState(fm, f, outState);
        }

        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            this.lifecycle.onViewDestroy(fm, f);
        }

        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            this.lifecycle.onDestroy(fm, f);
        }

        public void onFragmentDetached(FragmentManager fm, Fragment f) {
            super.onFragmentDetached(fm, f);
            this.lifecycle.onDetach(fm, f);
        }
    }
}

