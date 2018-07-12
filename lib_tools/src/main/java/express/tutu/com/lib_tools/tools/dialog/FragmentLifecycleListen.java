package express.tutu.com.lib_tools.tools.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.All;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnActivityCreate;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnAttach;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnCreate;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnDestroy;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnDetach;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnPause;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnPreAttach;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnResume;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnSaveInstanceState;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnStart;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnStop;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnViewCreate;
import express.tutu.com.lib_tools.tools.dialog.FRAGMENTS.OnViewDestroy;
import express.tutu.com.lib_tools.tools.dialog.FragmentLifecycleProxy.Bound;
/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class FragmentLifecycleListen implements LifecycleListen {
    private OnPreAttach onPreAttach;
    private OnAttach onAttach;
    private OnCreate onCreate;
    private OnActivityCreate onActivityCreate;
    private OnViewCreate onViewCreate;
    private OnStart onStart;
    private OnResume onResume;
    private OnPause onPause;
    private OnStop onStop;
    private OnSaveInstanceState onSaveInstanceState;
    private OnViewDestroy onViewDestroy;
    private OnDestroy onDestroy;
    private OnDetach onDetach;
    private Filter<? super Fragment> filter;
    private FragmentManager fm;
    private boolean recursive;
    private All impl;

    public FragmentLifecycleListen() {
        this.onPreAttach = FRAGMENTS.DEFAULT_ALL;
        this.onAttach = FRAGMENTS.DEFAULT_ALL;
        this.onCreate = FRAGMENTS.DEFAULT_ALL;
        this.onActivityCreate = FRAGMENTS.DEFAULT_ALL;
        this.onViewCreate = FRAGMENTS.DEFAULT_ALL;
        this.onStart = FRAGMENTS.DEFAULT_ALL;
        this.onResume = FRAGMENTS.DEFAULT_ALL;
        this.onPause = FRAGMENTS.DEFAULT_ALL;
        this.onStop = FRAGMENTS.DEFAULT_ALL;
        this.onSaveInstanceState = FRAGMENTS.DEFAULT_ALL;
        this.onViewDestroy = FRAGMENTS.DEFAULT_ALL;
        this.onDestroy = FRAGMENTS.DEFAULT_ALL;
        this.onDetach = FRAGMENTS.DEFAULT_ALL;
        this.filter = Filters.none();
    }

    public FragmentLifecycleListen onAll() {
        this.filter = Filter.ALL;
        return this;
    }

    public FragmentLifecycleListen on(@NonNull Fragment fragment) {
        this.filter = Filters.instance(fragment);
        return this;
    }

    public FragmentLifecycleListen on(@NonNull Class<? extends Fragment> clazz) {
        this.filter = Filters.type(clazz);
        return this;
    }

    public FragmentLifecycleListen on(@NonNull Filter<? super Fragment> filter) {
        this.filter = filter;
        return this;
    }

    public FragmentLifecycleListen on(@NonNull FragmentManager fm, boolean recursive) {
        this.fm = fm;
        this.recursive = recursive;
        return this;
    }

    public FragmentLifecycleListen with(OnPreAttach onPreAttach) {
        this.onPreAttach = onPreAttach;
        return this;
    }

    public FragmentLifecycleListen with(OnAttach onAttach) {
        this.onAttach = onAttach;
        return this;
    }

    public FragmentLifecycleListen with(OnCreate onCreate) {
        this.onCreate = onCreate;
        return this;
    }

    public FragmentLifecycleListen with(OnActivityCreate onActivityCreate) {
        this.onActivityCreate = onActivityCreate;
        return this;
    }

    public FragmentLifecycleListen with(OnViewCreate onViewCreate) {
        this.onViewCreate = onViewCreate;
        return this;
    }

    public FragmentLifecycleListen with(OnStart onStart) {
        this.onStart = onStart;
        return this;
    }

    public FragmentLifecycleListen with(OnResume onResume) {
        this.onResume = onResume;
        return this;
    }

    public FragmentLifecycleListen with(OnPause onPause) {
        this.onPause = onPause;
        return this;
    }

    public FragmentLifecycleListen with(OnStop onStop) {
        this.onStop = onStop;
        return this;
    }

    public FragmentLifecycleListen with(OnSaveInstanceState onSaveInstanceState) {
        this.onSaveInstanceState = onSaveInstanceState;
        return this;
    }

    public FragmentLifecycleListen with(OnViewDestroy onViewDestroy) {
        this.onViewDestroy = onViewDestroy;
        return this;
    }

    public FragmentLifecycleListen with(OnDestroy onDestroy) {
        this.onDestroy = onDestroy;
        return this;
    }

    public FragmentLifecycleListen with(OnDetach onDetach) {
        this.onDetach = onDetach;
        return this;
    }

    public FragmentLifecycleListen with(All all) {
        this.onPreAttach = all;
        this.onAttach = all;
        this.onCreate = all;
        this.onActivityCreate = all;
        this.onViewCreate = all;
        this.onStart = all;
        this.onResume = all;
        this.onPause = all;
        this.onStop = all;
        this.onSaveInstanceState = all;
        this.onViewDestroy = all;
        this.onDestroy = all;
        this.onDetach = all;
        return this;
    }

    public LifecycleListen listen() {
        if(this.impl == null) {
            this.impl = new FragmentLifecycleListen.FragmentLifecycleImpl(this);
        }

        if(this.fm == null) {
            FragmentLifecycleProxy.getInstance().addListen(this.impl, this.filter);
        } else {
            (new Bound(this.fm, this.recursive)).listen().addListen(this.impl, this.filter);
        }

        return this;
    }

    public void quit() {
        if(this.impl != null) {
            FragmentLifecycleProxy.getInstance().removeListen(this.impl);
        }
    }

    private static class FragmentLifecycleImpl implements All {
        FragmentLifecycleListen listen;

        public FragmentLifecycleImpl(FragmentLifecycleListen listen) {
            this.listen = listen;
        }

        public void onPreAttach(FragmentManager fm, Fragment f, Context context) {
            this.listen.onPreAttach.onPreAttach(fm, f, context);
        }

        public void onAttach(FragmentManager fm, Fragment f, Context context) {
            this.listen.onAttach.onAttach(fm, f, context);
        }

        public void onCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            this.listen.onCreate.onCreate(fm, f, savedInstanceState);
        }

        public void onActivityCreate(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            this.listen.onActivityCreate.onActivityCreate(fm, f, savedInstanceState);
        }

        public void onViewCreate(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            this.listen.onViewCreate.onViewCreate(fm, f, v, savedInstanceState);
        }

        public void onStart(FragmentManager fm, Fragment f) {
            this.listen.onStart.onStart(fm, f);
        }

        public void onResume(FragmentManager fm, Fragment f) {
            this.listen.onResume.onResume(fm, f);
        }

        public void onPause(FragmentManager fm, Fragment f) {
            this.listen.onPause.onPause(fm, f);
        }

        public void onStop(FragmentManager fm, Fragment f) {
            this.listen.onStop.onStop(fm, f);
        }

        public void onSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            this.listen.onSaveInstanceState.onSaveInstanceState(fm, f, outState);
        }

        public void onViewDestroy(FragmentManager fm, Fragment f) {
            this.listen.onViewDestroy.onViewDestroy(fm, f);
        }

        public void onDestroy(FragmentManager fm, Fragment f) {
            this.listen.onDestroy.onDestroy(fm, f);
        }

        public void onDetach(FragmentManager fm, Fragment f) {
            this.listen.onDetach.onDetach(fm, f);
        }
    }
}
