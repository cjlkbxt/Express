package express.tutu.com.lib_tools.tools;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class Toasty {
    private static final String TAG = Toasty.class.getSimpleName();
    private static final boolean localLOGV = false;

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private static final long LONG_DURATION = 3500;
    private static final long SHORT_DURATION = 2000;

    //    private Handler mHandler = new Handler(); // see https://bugly.qq.com/v2/crash-reporting/crashes/33840e9461/21338?pid=1
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;

    private static Toasty mToasty;

    private Context mContext;
    private Config mConfig;
    private View mCustomView;
    private View mView;
    private TextView mTv;

    private Toasty(Context context) {
        this.mContext = context.getApplicationContext();
        initWindow(mContext);
    }

    private void initWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowLayoutParams.windowAnimations = getSystemToastAnim();
        mWindowLayoutParams.setTitle("Toast");
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowLayoutParams.x = 0;
        mWindowLayoutParams.y = 0;
    }

    public static Toasty make(Context context, CharSequence text, int duration) {
        Config config = getDefaultConfig(context);
        config.setText(text);
        config.setDuration(duration);
        return make(context, config);
    }

    public static Toasty make(Context context, @StringRes int strRes, int duration) {
        return make(context, context.getResources().getText(strRes), duration);
    }

    public static Toasty make(Context context) {
        Config config = getDefaultConfig(context);
        return make(context, config);
    }

    public static Toasty make(Context context, Config config) {
        if(mToasty == null) {
            mToasty = new Toasty(context);
            View sysToastLayout = null;
            try {
                sysToastLayout = LayoutInflater.from(context).inflate(mToasty.getSystemToastLayout(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            TextView sysToastTv = null;
            if(sysToastLayout != null) {
                sysToastTv = (TextView) sysToastLayout.findViewById(mToasty.getSystemToastId());
            }
            if(sysToastLayout != null && sysToastTv != null) {
                mToasty.mView = sysToastLayout;
                mToasty.mTv = sysToastTv;
            } else {
                mToasty.mView = LayoutInflater.from(context).inflate(R.layout.base_toast_layout, null);
                mToasty.mTv = (TextView) mToasty.mView.findViewById(R.id.toast_text);
            }
        }
        if (config != null) {
            mToasty.mConfig = config;
        } else {
            mToasty.mConfig = getDefaultConfig(context);
        }
        return mToasty;
    }

    @NonNull
    private static Config getDefaultConfig(Context context) {
        Config config = new Config();
        config.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        config.setY(200);
        config.setDuration(Toasty.LENGTH_SHORT);
        Drawable background = ContextCompat.getDrawable(context, android.R.drawable.toast_frame);
        config.setBackground(background);
        return config;
    }

    public void setConfig(Config mConfig) {
        this.mConfig = mConfig;
    }

    public void setView(@NonNull View view) {
        mCustomView = view;
    }

    public View getView() {
        return mView;
    }

    public Toasty setDuration(int duration) {
        mConfig.mDuration = duration;
        return this;
    }

    public int getDuration() {
        return mConfig.mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        mConfig.horizontalMargin = horizontalMargin;
        mConfig.verticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return mConfig.horizontalMargin;
    }

    public float getVerticalMargin() {
        return mConfig.verticalMargin;
    }

    public Toasty setTextColor(@ColorInt int textColor) {
        mConfig.textColor = textColor;
        if(mTv != null) {
            mTv.setTextColor(textColor);
        }
        return this;
    }

    public Toasty setBackground(Drawable background) {
        mConfig.background = background;
        if(mView != null) {
            ViewCompat.setBackground(mView, null);
            if(mTv != null) {
                ViewCompat.setBackground(mTv, background);
            }
        }
        return this;
    }

    public Toasty setGravity(int gravity, int xOffset, int yOffset) {
        mConfig.gravity = gravity;
        mConfig.x = xOffset;
        mConfig.y = yOffset;
        return this;
    }

    public Toasty setText(@StringRes int resId) {
        return setText(mContext.getText(resId));
    }

    public Toasty setText(CharSequence s) {
        mConfig.mText = s;
        if (mTv != null) {
            mTv.setText(s);
        }
        return this;
    }

    public void show() {
        if(mCustomView == null) {
            ViewCompat.setBackground(mView, null);
            if (mTv != null) {
                ViewCompat.setBackground(mTv, mConfig.background);
                mTv.setGravity(Gravity.CENTER);
                if(mConfig.textColor != 0) {
                    mTv.setTextColor(mConfig.textColor);
                }
                if(mConfig.textSize != 0) {
                    mTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, mConfig.textSize);
                }
                mTv.setText(mConfig.mText);
            }
        } else {
            if (mView != null && mView.getParent() != null) {
                mWindowManager.removeViewImmediate(mView);
            }
            mView = mCustomView;
        }

        mWindowLayoutParams.gravity = mConfig.gravity;
        mWindowLayoutParams.x = mConfig.x;
        mWindowLayoutParams.y = mConfig.y;
        mWindowLayoutParams.verticalMargin = mConfig.verticalMargin;
        mWindowLayoutParams.horizontalMargin = mConfig.horizontalMargin;

        try {
            if (mView.getParent() == null) {
                if (localLOGV) Log.v(TAG, "ADD VIEW: " + mView);
                mWindowManager.addView(mView, mWindowLayoutParams);
            } else {
                if (localLOGV) Log.v(TAG, "UPDATE VIEW: " + mView);
                mWindowManager.updateViewLayout(mView, mWindowLayoutParams);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        delayHide();
    }

    public void cancel() {
        if (localLOGV) Log.v(TAG, "CANCEL");
        mHandler.removeCallbacksAndMessages(null);
        handleHide();
    }

    private void handleHide() {
        if (localLOGV) Log.v(TAG, "HANDLE_HIDE");
        if (mView != null && mView.getParent() != null) {
            mWindowManager.removeViewImmediate(mView);
        }
        mToasty = null;
    }

    private void delayHide() {
        mHandler.removeCallbacksAndMessages(null);
        long delayMillis;
        switch (mConfig.mDuration) {
            case LENGTH_SHORT:
                delayMillis = SHORT_DURATION;
                break;
            case LENGTH_LONG:
                delayMillis = LONG_DURATION;
                break;
            default:
                delayMillis = mConfig.mDuration;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleHide();
            }
        }, delayMillis);
    }

    private int getSystemToastLayout() {
        return mContext.getResources().getIdentifier("transient_notification", "layout", "android");
    }

    private int getSystemToastId() {
        return mContext.getResources().getIdentifier("message", "id", "android");
    }

    private int getSystemToastAnim() {
        return android.R.style.Animation_Toast;
    }

    public static class Config {
        @ColorInt
        int textColor;
        int textSize;
        Drawable background;
        int gravity;
        float horizontalMargin;
        float verticalMargin;
        int x;
        int y;
        int mDuration;
        CharSequence mText;

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public Drawable getBackground() {
            return background;
        }

        public void setBackground(Drawable background) {
            this.background = background;
        }

        public int getGravity() {
            return gravity;
        }

        public void setGravity(int gravity) {
            this.gravity = gravity;
        }

        public float getHorizontalMargin() {
            return horizontalMargin;
        }

        public void setHorizontalMargin(float horizontalMargin) {
            this.horizontalMargin = horizontalMargin;
        }

        public float getVerticalMargin() {
            return verticalMargin;
        }

        public void setVerticalMargin(float verticalMargin) {
            this.verticalMargin = verticalMargin;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getDuration() {
            return mDuration;
        }

        public void setDuration(int mDuration) {
            this.mDuration = mDuration;
        }

        public CharSequence getText() {
            return mText;
        }

        public void setText(CharSequence mText) {
            this.mText = mText;
        }
    }
}
