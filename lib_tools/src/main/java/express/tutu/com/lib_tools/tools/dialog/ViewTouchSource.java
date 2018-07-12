package express.tutu.com.lib_tools.tools.dialog;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ViewTouchSource extends LogSource{
    private static final ViewTouchSource INSTANCE = new ViewTouchSource();

    public static ViewTouchSource getInstance() {
        return INSTANCE;
    }

    public static final String NAME = "view_touch";

    public static final String KEY_EVENT_TYPE = "event_type";
    public static final String EVENT_TYPE_TOUCH = "touch";
    public static final String EVENT_TYPE_CLICK = "click";
    public static final String EVENT_TYPE_CLICK_ITEM = "click_item";
    public static final String EVENT_TYPE_SCROLL = "scroll";

    public static final String KEY_PAGE_NAME = "page_name";
    public static final String KEY_PAGE_TYPE = "page_type";
    public static final String KEY_PAGE_DATA = "page_data";
    public static final String KEY_AREA_NAME = "area_name";
    public static final String KEY_AREA_TYPE = "area_type";
    public static final String KEY_AREA_DATA = "area_data";
    public static final String KEY_VIEW_NAME = "view_name";
    public static final String KEY_VIEW_TYPE = "view_type";
    public static final String KEY_WINDOW_NAME = "window_name";

    public static final String KEY_PARENT_NAME = "parent_name";
    public static final String KEY_PARENT_TYPE = "parent_type";
    public static final String KEY_ADAPTER_NAME = "adapter_name";
    public static final String ADAPTER_RECYCLER = "recycler";
    public static final String ADAPTER_LIST = "list";
    public static final String ADAPTER_ADAPTER = "adapter";

    public static final String KEY_ITEM_POS = "item_pos";
    public static final String KEY_ITEM_DATA = "item_data";

//    public static final String KEY_HIERARCHY = "hierarchy";
//    public static final String KEY_DISTANCE = "distance";
//    public static final String KEY_ORIENT = "orient";

    private StrComposer<? super Activity> activityName = new Composers.NULL();
    private StrComposer<? super Fragment> fragmentName = new Composers.FragmentTagName();
    private StrComposer<? super Activity> activityType = new Composers.SimpleName();
    private StrComposer<? super Fragment> fragmentType = new Composers.SimpleName();
    private StrComposer<? super View> viewName = new Composers.ViewIDName();
    private StrComposer<? super View> viewType = new Composers.SimpleName();

    private ItemComposer<ListView> listComposer = new Composers.DefList();
    private ItemComposer<AdapterView> adapterComposer = new Composers.DefAdapter();
    private ItemComposer<RecyclerView> recyclerComposer = new Composers.DefRecycler();

    private FragmentFinder fragmentFinder = new FragmentFinder();

    private ViewTouchSource() {
        super(NAME);
    }

    @Override
    public void onConsumer(Consumer<? super LogInfo> consumer) {
        Lifecycle.activity().onAll().with(new ActivityCallback()).listen();
        Lifecycle.fragment().onAll().with(new FragmentCreateView()).with(new FragmentDestroyView()).listen();
    }

    public void setActivityName(StrComposer<? super Activity> activityName) {
        this.activityName = activityName;
    }

    public void setFragmentName(StrComposer<? super Fragment> fragmentName) {
        this.fragmentName = fragmentName;
    }

    public void setListComposer(ItemComposer<ListView> listComposer) {
        this.listComposer = listComposer;
    }

    public void setAdapterComposer(ItemComposer<AdapterView> adapterComposer) {
        this.adapterComposer = adapterComposer;
    }

    public void setRecyclerComposer(ItemComposer<RecyclerView> recyclerComposer) {
        this.recyclerComposer = recyclerComposer;
    }

    private class ActivityCallback implements ACTIVITIES.OnResume {
        @Override
        public void onResume(Activity activity) {
            View view = activity.getWindow().peekDecorView();
            if (!(view instanceof ViewGroup)) {
                return;
            }
            ViewGroup root = (ViewGroup) view;
            if (root.getChildCount() <= 0) {
                return;
            }
            View child = root.getChildAt(0);
            if (child instanceof TouchHack) {
                return;
            }
            root.removeView(child);
            TouchHack hack = new TouchHack(activity);
            hack.setOnHackTouchL(hackTouchL);
            root.addView(hack, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            hack.addView(child, new TouchHack.LayoutParams(child.getLayoutParams()));
        }
    }

    private class FragmentCreateView implements FRAGMENTS.OnViewCreate {

        @Override
        public void onViewCreate(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
            fragmentFinder.addPage(view, fragment);
        }
    }

    private class FragmentDestroyView implements FRAGMENTS.OnViewDestroy {

        @Override
        public void onViewDestroy(FragmentManager fragmentManager, Fragment fragment) {
            fragmentFinder.removePage(fragment.getView());
        }
    }

    private boolean logClickItem(TouchHack hack, View target, MotionEvent event) {
        ViewGroup adapterView;
        adapterView = ViewUtil.findTheParent(target, AdapterView.class, TouchHack.class);
        if (adapterView == null) {
            adapterView = ViewUtil.findTheParent(target, RecyclerView.class, TouchHack.class);
        }
        if (adapterView == null) {
            return false;
        }
        Rect hackLayout = new Rect();
        hack.getGlobalVisibleRect(hackLayout);
        int posX = (int) (event.getX() + hackLayout.left);
        int posY = (int) (event.getY() + hackLayout.top);
        int itemIndex = ViewUtil.findChildIndexAt(adapterView, posX, posY);
        if (itemIndex < 0) {
            return false;
        }
        if (adapterView instanceof RecyclerView) {
            return logClickRecyclerItem(hack, target, (RecyclerView) adapterView, itemIndex);
        } else if (adapterView instanceof ListView) {
            return logClickListItem(hack, target, (ListView) adapterView, itemIndex);
        } else {
            return logClickAdapterItem(hack, target, (AdapterView) adapterView, itemIndex);
        }
    }

    private boolean logClickRecyclerItem(TouchHack hack, View target, RecyclerView parent, int itemIndex) {
        LogBuilder builder = create(EVENT_TYPE_CLICK_ITEM, target, hack);
        builder.put(KEY_PARENT_NAME, viewName.compose(parent));
        builder.put(KEY_PARENT_TYPE, viewType.compose(parent));
        builder.put(KEY_ADAPTER_NAME, ADAPTER_RECYCLER);
        View itemView = parent.getChildAt(itemIndex);
        int itemPosition = parent.getChildAdapterPosition(itemView);
        builder.put(KEY_ITEM_POS, String.valueOf(itemPosition));
        builder.put(KEY_ITEM_DATA, recyclerComposer.compose(parent, itemPosition));
        notifyNext(builder.build());
        return true;
    }

    private boolean logClickListItem(TouchHack hack, View target, ListView parent, int itemIndex) {
        LogBuilder builder = create(EVENT_TYPE_CLICK_ITEM, target, hack);
        builder.put(KEY_PARENT_NAME, viewName.compose(parent));
        builder.put(KEY_PARENT_TYPE, viewType.compose(parent));
        builder.put(KEY_ADAPTER_NAME, ADAPTER_LIST);
        int itemPosition = itemIndex + parent.getFirstVisiblePosition();
        builder.put(KEY_ITEM_POS, String.valueOf(itemPosition));
        builder.put(KEY_ITEM_DATA, listComposer.compose(parent, itemPosition));
        notifyNext(builder.build());
        return true;
    }

    private boolean logClickAdapterItem(TouchHack hack, View target, AdapterView parent, int itemIndex) {
        LogBuilder builder = create(EVENT_TYPE_CLICK_ITEM, target, hack);
        builder.put(KEY_PARENT_NAME, viewName.compose(parent));
        builder.put(KEY_PARENT_TYPE, viewType.compose(parent));
        builder.put(KEY_ADAPTER_NAME, ADAPTER_ADAPTER);
        int itemPosition = itemIndex + parent.getFirstVisiblePosition();
        builder.put(KEY_ITEM_POS, String.valueOf(itemPosition));
        builder.put(KEY_ITEM_DATA, adapterComposer.compose(parent, itemPosition));
        notifyNext(builder.build());
        return true;
    }

    private boolean logClick(TouchHack hack, TouchHack.TouchRecord down, TouchHack.TouchRecord up) {
        if (down == null || up == null || down.getTarget() != up.getTarget()) {
            return false;
        }
        View target = down.getTarget();
        if (!target.isClickable()) {
            return false;
        }
        float distance = (float) Math.hypot((down.getEvent().getX() - up.getEvent().getX()),
                (down.getEvent().getY() - up.getEvent().getY()));
        if (distance > ViewConfiguration.get(down.getTarget().getContext()).getScaledTouchSlop()) {
            return false;
        }
        if (logClickItem(hack, target, down.getEvent())) {
            return true;
        }
        LogBuilder builder = create(EVENT_TYPE_CLICK, target, hack);
        notifyNext(builder.build());
        return true;
    }

    private boolean logScroll(TouchHack hack, TouchHack.TouchRecord down, TouchHack.TouchRecord up) {
        if (down == null || up == null) {
            return false;
        }
        View target = up.getTarget();
        float distanceX = Math.abs((down.getEvent().getX() - up.getEvent().getX()));
        float distanceY = Math.abs((down.getEvent().getY() - up.getEvent().getY()));
        float distance;
        if (target instanceof RecyclerView) {
            distance = Math.max(distanceX, distanceY);
        } else if (target instanceof ScrollView || target instanceof AbsListView) {
            distance = distanceY;
        } else if (target instanceof HorizontalScrollView || target instanceof ViewPager) {
            distance = distanceX;
        } else {
            return false;
        }
        float slop = ViewConfiguration.get(target.getContext()).getScaledTouchSlop();
        if (distance < slop) {
            return false;
        }
        LogBuilder builder = create(EVENT_TYPE_SCROLL, target, hack);
        notifyNext(builder.build());
        return true;
    }

    public TouchHack.OnHackTouchListener hackTouchL = new TouchHack.OnHackTouchListener() {
        @Override
        public void onHackTouch(TouchHack touchHack, TouchHack.TouchRecord down, TouchHack.TouchRecord up) {
//            long nano = Debug.threadCpuTimeNanos();
//            try {
            if (logClick(touchHack, down, up)) {
                return;
            }
            if (logScroll(touchHack, down, up)) {
                return;
            }
            if (down != null) {
                View target = down.getTarget();
                LogBuilder builder = create(EVENT_TYPE_TOUCH, target, touchHack);
                notifyNext(builder.build());
            }
//            } finally {
//                hackTime.add(Debug.threadCpuTimeNanos() - nano);
//            }
        }
    };

    private LogBuilder create(String type, View view, TouchHack hack) {
//        long nano = Debug.threadCpuTimeNanos();
//        try {
        Activity host = ComponentUtil.findWrapperActivity(view.getContext());
        Fragment area = fragmentFinder.findParentFragment(view);
        LogBuilder builder = newBuilder().put(KEY_EVENT_TYPE, type)
                .put(KEY_VIEW_NAME, viewName.compose(view))
                .put(KEY_VIEW_TYPE, viewType.compose(view))
                .put(KEY_WINDOW_NAME, hack.getWindowName());
        if (host != null) {
            builder.put(KEY_PAGE_NAME, activityName.compose(host));
            builder.put(KEY_PAGE_TYPE, activityType.compose(host));
            builder.put(KEY_PAGE_DATA, host);
        }
        if (area != null) {
            builder.put(KEY_AREA_NAME, fragmentName.compose(area));
            builder.put(KEY_AREA_TYPE, fragmentType.compose(area));
            builder.put(KEY_AREA_DATA, area);
        }
        return builder;
//        } finally {
//            createTime.add(Debug.threadCpuTimeNanos() - nano);
//        }
    }

    /**
     * Created by felix.dai on 2017/6/14.
     */
    public static class FragmentFinder {

        private WeakHashMap<View, WeakReference<Fragment>> pageMap = new WeakHashMap<>();

        public void addPage(View view, Fragment fragment) {
            pageMap.put(view, new WeakReference<>(fragment));
        }

        public void removePage(View view) {
            pageMap.remove(view);
        }

        public Fragment findParentFragment(View view) {
            while (view != null) {
                WeakReference<Fragment> ref = pageMap.get(view);
                if (ref != null) {
                    return ref.get();
                }
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
            return null;
        }

//        public String getPagePath(View view) {
//            List<String> names = new ArrayList<>();
//            while (view != null) {
//                String name = pageMap.get(view);
//                if (name != null) {
//                    names.add(0, name);
//                }
//                ViewParent parent = view.getParent();
//                view = parent instanceof View ? (View) parent : null;
//            }
//            StringBuilder sb = new StringBuilder();
//            for (String name : names) {
//                sb.append("/");
//                sb.append(name);
//            }
//            return sb.toString();
//        }
    }
}
