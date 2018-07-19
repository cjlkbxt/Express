package express.tutu.com.lib_tools.place;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.PopupWindow;

import express.tutu.com.lib_tools.tools.popupwindow.BasePopupWindow;
import express.tutu.com.lib_tools.utils.LifecycleUtils;
import express.tutu.com.lib_tools.utils.ScreenUtils;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public abstract class BasePickerHelper {
    protected Context mContext;
    protected BasePopupWindow mPopupWindow;
    private ListenedDrawRelativeLayout mContentView;

    private boolean isFocusable = true;

    private Activity rawCallback;

    public BasePickerHelper(Context context, Activity rawCallback) {
        this.mContext = context;
        this.rawCallback = rawCallback;
    }

    public void setFocusable(boolean focusable) {
        isFocusable = focusable;
    }

    public boolean isShowing() {
        return mPopupWindow != null && mPopupWindow.isShowing();
    }

    public void showView(final View anchorView) {
        initPopupWindow(anchorView);

        int screenHeight = ScreenUtils.heightPixels(mContext);
        if (mContext instanceof Activity && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            int decorViewHeight = ((Activity) mContext).getWindow().getDecorView().getHeight();
            if (decorViewHeight > 0) {
                View navigationBar = null;
                try {
                    navigationBar = ((Activity) mContext).findViewById(android.R.id.navigationBarBackground);
                } catch (Exception e) {}
                if (navigationBar != null) {
                    screenHeight = decorViewHeight - navigationBar.getHeight();
                } else {
                    screenHeight = decorViewHeight;
                }
            }
        }
        int[] screenPos = new int[2];
        anchorView.getLocationOnScreen(screenPos);

        int popWinHeight = screenHeight - screenPos[1] - anchorView.getHeight();
        mPopupWindow.setHeight(popWinHeight);

        if(isFocusable) {
            mPopupWindow.setTouchInterceptor(null);
        } else {
            mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int[] loc = new int[2];
                    anchorView.getLocationOnScreen(loc);

                    if (event.getX() >= loc[0] && event.getX() <= loc[0] + anchorView.getWidth() &&
                            event.getY() <= 0 && event.getY() >= -anchorView.getHeight()) {
                        return true;
                    }
                    return false;
                }
            });
            interceptKeyEvent();
        }

        if (LifecycleUtils.isActivate(mContext)) {
            mPopupWindow.showAsDropDown(anchorView);
            onShow();
            mContentView.setOnAfterDrawListener(new ListenedDrawRelativeLayout.OnAfterDrawListener() {
                @Override
                public void onAfterDraw() {
                    mContentView.setOnAfterDrawListener(null);
                    //避免界面已切换，popupwindow弹出的问题(仍有可能出现)
                    boolean localVisibleRect = anchorView.getLocalVisibleRect(new Rect());
                    if(!localVisibleRect) {
                        mPopupWindow.dismiss();
                    }
                }
            });
        }
    }

    public void hideView() {
        if(mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public void toggle(View anchorView) {
        if(mPopupWindow == null || !mPopupWindow.isShowing()) {
            showView(anchorView);
        } else {
            mPopupWindow.dismiss();
        }
    }

    private void initPopupWindow(View anchorView) {
        if(mPopupWindow == null) {
            int[] screenPos = new int[2];
            anchorView.getLocationOnScreen(screenPos);

            mPopupWindow = new BasePopupWindow(mContext);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(android.R.color.transparent)));
            mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ScreenUtils.heightPixels(mContext) - screenPos[1] - anchorView.getHeight());

            mContentView = createContentView();
            mPopupWindow.setContentView(mContentView, getPopupWdName());
            mPopupWindow.setTouchable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(isFocusable);
            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    BasePickerHelper.this.onDismiss();
                    if(!isFocusable && !rawCallback.isFinishing()) {
                        ((Activity) mContext).getWindow().setCallback(rawCallback);
                    }
                }
            });
        }
    }

    protected abstract String getPopupWdName();

    protected abstract ListenedDrawRelativeLayout createContentView();
    protected abstract void onShow();
    protected abstract void onDismiss();

    private void interceptKeyEvent() {
        if(mContext instanceof Activity && !((Activity)mContext).isFinishing()) {
            ((Activity)mContext).getWindow().setCallback(new Window.Callback() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent event) {
                    if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                    Window win = ((Activity)mContext).getWindow();
                    if (win.superDispatchKeyEvent(event)) {
                        return true;
                    }
                    View decor = win.getDecorView();
                    return event.dispatch(((Activity)mContext), decor != null ? decor.getKeyDispatcherState() : null, this);
                }

                @Override
                public boolean dispatchKeyShortcutEvent(KeyEvent event) {
                    return false;
                }

                @Override
                public boolean dispatchTouchEvent(MotionEvent ev) {
                    if (((Activity)mContext).getWindow().superDispatchTouchEvent(ev)) {
                        return true;
                    }
                    return ((Activity)mContext).onTouchEvent(ev);
                }

                @Override
                public boolean dispatchTrackballEvent(MotionEvent event) {
                    return false;
                }

                @Override
                public boolean dispatchGenericMotionEvent(MotionEvent event) {
                    return false;
                }

                @Override
                public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
                    return false;
                }

                @Nullable
                @Override
                public View onCreatePanelView(int featureId) {
                    return null;
                }

                @Override
                public boolean onCreatePanelMenu(int featureId, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPreparePanel(int featureId, View view, Menu menu) {
                    return false;
                }

                @Override
                public boolean onMenuOpened(int featureId, Menu menu) {
                    return false;
                }

                @Override
                public boolean onMenuItemSelected(int featureId, MenuItem item) {
                    return false;
                }

                @Override
                public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
                }

                @Override
                public void onContentChanged() {
                }

                @Override
                public void onWindowFocusChanged(boolean hasFocus) {
                }

                @Override
                public void onAttachedToWindow() {
                }

                @Override
                public void onDetachedFromWindow() {
                }

                @Override
                public void onPanelClosed(int featureId, Menu menu) {
                }

                @Override
                public boolean onSearchRequested() {
                    return false;
                }

                @Override
                public boolean onSearchRequested(SearchEvent searchEvent) {
                    return false;
                }

                @Nullable
                @Override
                public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
                    return null;
                }

                @Nullable
                @Override
                public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
                    return null;
                }

                @Override
                public void onActionModeStarted(ActionMode mode) {
                }

                @Override
                public void onActionModeFinished(ActionMode mode) {
                }
            });
        }
    }
}
