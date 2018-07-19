package express.tutu.com.lib_tools.tools.popupwindow;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;

import express.tutu.com.lib_tools.tools.dialog.TouchHack;
import express.tutu.com.lib_tools.tools.dialog.ViewTouchSource;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class BasePopupWindow extends PopupWindow {
    public static final String TAG = "BasePopupWindow";

    private TouchHack touchHack;

    public BasePopupWindow(Context context) {
        super(context);
    }

    public BasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public BasePopupWindow() {
    }

    public BasePopupWindow(View contentView) {
        super(contentView);
    }

    public BasePopupWindow(int width, int height) {
        super(width, height);
    }

    public BasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public BasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    private void createTouchHack(View contentView, String name) {
        if (contentView == null) return;
        touchHack = new TouchHack(contentView.getContext());
        touchHack.setWindowName(name);
        touchHack.removeAllViews();
        touchHack.addView(contentView);
        touchHack.setOnHackTouchL(ViewTouchSource.getInstance().hackTouchL);
    }


    public void setContentView(View contentView, String name) {
        createTouchHack(contentView, name);
        super.setContentView(touchHack);
    }

    @Override
    public void setContentView(View contentView) {
        setContentView(contentView, null);
    }

    public void setPopupWindowName(String popupWindowName) {
        if (touchHack != null && !TextUtils.isEmpty(popupWindowName)) {
            touchHack.setWindowName(popupWindowName);
            Log.i(TAG, popupWindowName);
        }
    }
}
