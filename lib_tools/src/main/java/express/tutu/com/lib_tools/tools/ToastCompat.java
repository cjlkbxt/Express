package express.tutu.com.lib_tools.tools;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import express.tutu.com.lib_tools.utils.ContextUtil;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class ToastCompat {
    private Toast mToast;
    private Toasty mToasty;

    /**
     *
     * @param context
     * @param text
     * @param duration
     * @return
     */
    public static ToastCompat make(Context context, CharSequence text, int duration) {
        if(context == null) {
            context = ContextUtil.get();
        }
        ToastCompat toastCompat = new ToastCompat();
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        boolean notificationsEnabled = manager.areNotificationsEnabled();
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && notificationsEnabled) {
            toastCompat.mToast = Toast.makeText(context, text, duration);
        } else {
            toastCompat.mToasty = Toasty.make(context, text, duration);
        }
        return toastCompat;
    }

    public static ToastCompat make(Context context, int duration) {
        return make(context, "", duration);
    }

    public ToastCompat setText(CharSequence text) {
        if(mToast != null) {
            mToast.setText(text);
        } else if (mToasty != null) {
            mToasty.setText(text);
        }
        return this;
    }

    public ToastCompat setView(View view) {
        if(mToast != null) {
            mToast.setView(view);
        } else if (mToasty != null) {
            mToasty.setView(view);
        }
        return this;
    }

    public View getView() {
        if(mToast != null) {
            return mToast.getView();
        } else if (mToasty != null) {
            return mToasty.getView();
        }
        return null;
    }

    public ToastCompat setGravity(int gravity) {
        return setGravity(gravity, 0, 0);
    }

    public ToastCompat setGravity(int gravity, int xOffset, int yOffset) {
        if(mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        } else if (mToasty != null) {
            mToasty.setGravity(gravity, xOffset, yOffset);
        }
        return this;
    }

    public ToastCompat setDuration(int duration) {
        if(mToast != null) {
            mToast.setDuration(duration);
        } else if (mToasty != null) {
            mToasty.setDuration(duration);
        }
        return this;
    }

    public ToastCompat setBackground(Drawable background) {
        if(mToast != null) {
            //容错，background和textColor一般一起修改，避免出现背景色和字体色接近看不清楚的情况，如果获取系统ToastTv失败则不修改背景
            TextView systemToastTv = getSystemToastTv();
            if(systemToastTv != null) {
                if (systemToastTv.getBackground() != null) {
                    ViewCompat.setBackground(systemToastTv, background);
                    ViewCompat.setBackground(mToast.getView(), null);
                } else {
                    ViewCompat.setBackground(mToast.getView(), background);
                }
            }
        } else if (mToasty != null) {
            mToasty.setBackground(background);
        }
        return this;
    }

    public ToastCompat setTextColor(int textColor) {
        if(mToast != null) {
            TextView toastTv = getSystemToastTv();
            if(toastTv != null) {
                toastTv.setTextColor(textColor);
            }
        } else if (mToasty != null) {
            mToasty.setTextColor(textColor);
        }
        return this;
    }

    public void show() {
        if(mToast != null) {
            mToast.show();
        } else if (mToasty != null) {
            mToasty.show();
        }
    }

    public void cancel() {
        if(mToast != null) {
            mToast.cancel();
        }
        if (mToasty != null) {
            mToasty.cancel();
        }
    }

    private TextView getSystemToastTv() {
        if(mToast != null) {
            View toastView = mToast.getView();
            if(toastView != null) {
                View messageTv = toastView.findViewById(android.R.id.message);
                if(messageTv != null && messageTv instanceof TextView) {
                    return (TextView) messageTv;
                }
            }
        }
        return null;
    }

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}
}
