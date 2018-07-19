package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class ListenedDrawRelativeLayout extends RelativeLayout {
    private OnAfterDrawListener mOnAfterDrawListener;

    public void setOnAfterDrawListener(OnAfterDrawListener onAfterDrawListener) {
        this.mOnAfterDrawListener = onAfterDrawListener;
    }

    public ListenedDrawRelativeLayout(Context context) {
        this(context, null);
    }

    public ListenedDrawRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListenedDrawRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mOnAfterDrawListener != null) {
            mOnAfterDrawListener.onAfterDraw();
        }
    }

    public interface OnAfterDrawListener {
        void onAfterDraw();
    }
}
