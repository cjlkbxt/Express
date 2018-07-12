package express.tutu.com.lib_tools.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.tools.ToastCompat;


/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ToastUtil {
    public static ToastCompat showToast(Context ctx, int msgRes) {
        return showToast(ctx, msgRes, Toast.LENGTH_SHORT);
    }

    public static ToastCompat showToast(Context ctx, CharSequence msg) {
        return showToast(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static ToastCompat showToast(Context ctx, int msgRes, @ToastCompat.Duration int duration) {
        return showToast(ctx, ctx.getText(msgRes), duration);
    }

    public static ToastCompat showToast(Context ctx, CharSequence msg, @ToastCompat.Duration int duration) {
        ToastCompat toast = newToast(ctx, msg, duration);
        if (toast == null) return null;
        if (TextUtils.isEmpty(msg)) {
            return toast;
        }
        toast.show();
        return toast;
    }

    public static ToastCompat newToast(Context ctx) {
        return newToast(ctx, "", Toast.LENGTH_SHORT);
    }

    public static ToastCompat newToast(Context ctx, CharSequence msg) {
        return newToast(ctx, msg, Toast.LENGTH_SHORT);
    }

    public static ToastCompat newToast(Context ctx, int msgRes) {
        return newToast(ctx, ctx.getText(msgRes));
    }

    public static ToastCompat newToast(Context ctx, CharSequence msg, @ToastCompat.Duration int duration) {
        if (ctx == null) {
            ctx = ContextUtil.get();
        }
        ToastCompat toast = ToastCompat.make(ctx, msg, duration);
        toast.setGravity(Gravity.CENTER);
        toast.setBackground(ContextCompat.getDrawable(ctx, R.drawable.base_toast_common_bg));
        toast.setTextColor(Color.WHITE);
        toast.getView().setMinimumWidth(ctx.getResources().getDisplayMetrics().widthPixels / 3);
        return toast;
    }
}
