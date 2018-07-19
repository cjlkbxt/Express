package express.tutu.com.lib_tools.tools.popupwindow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class DrawableCenterTextView extends AppCompatTextView {
    public DrawableCenterTextView(Context context) {
        super(context);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            if (drawableLeft != null && drawableRight != null) {
                super.onDraw(canvas);
                return;
            }
            if (drawableLeft != null) {
                setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                float offset = getWidth() - getPaddingLeft() - getPaddingRight() - bodyWidth;
                offset = offset < 0 ? 0 : offset;
                canvas.translate(offset / 2, 0);
            } else if (drawableRight != null) {
                float textWidth = getPaint().measureText(getText().toString());
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = drawableRight.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                float offset = getWidth() - getPaddingLeft() - getPaddingRight() - bodyWidth;
                if (offset < 0) {
                    offset = 0;
                    setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                } else {
                    setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                }
                canvas.translate(-offset / 2, 0);
            }
        }
        super.onDraw(canvas);
    }
}
