package express.tutu.com.lib_tools.tools.dialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class CardLayout extends FrameLayout {
    private float widthRatio = 0f;
    private float heightRatio = 0f;
    private float cornerRadius = 0f;

    private RectF viewRect = new RectF();
    private Path maskPath = new Path();

    private Paint clipPaint = new Paint();
    private Paint maskPaint = new Paint();
    private Paint layerPaint = new Paint();

    public CardLayout(Context context) {
        super(context);
    }

    public CardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardLayout);
        widthRatio = typedArray.getFloat(R.styleable.CardLayout_widthRatio, 0f);
        heightRatio = typedArray.getFloat(R.styleable.CardLayout_heightRatio, 0f);
        cornerRadius = typedArray.getDimension(R.styleable.CardLayout_cornerRadius, 0f);
        typedArray.recycle();

        maskPaint.setAntiAlias(true);
        maskPaint.setColor(0xFFFFFFFF);
        clipPaint.setAntiAlias(true);
        clipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        layerPaint.setAntiAlias(true);
//        layerPaint.setXfermode(getBackground() != null ? new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP) : null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(isHardwareAccelerated() ? LAYER_TYPE_HARDWARE : LAYER_TYPE_SOFTWARE, layerPaint);
        }
    }

    public void setAspectRatio(float widthRatio, float heightRatio) {
        this.widthRatio = widthRatio;
        this.heightRatio = heightRatio;
        requestLayout();
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
        invalidate();
    }

    public float getAspectRatio() {
        return widthRatio / heightRatio;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    @Override
    protected void onMeasure(int specW, int specH) {
        if (widthRatio <= 0) {
            super.onMeasure(specW, specH);
            return;
        }
        int modeW = MeasureSpec.getMode(specW);
        int sizeW = MeasureSpec.getSize(specW);
        int modeH = MeasureSpec.getMode(specH);
        int sizeH = MeasureSpec.getSize(specH);
        boolean wrapContent = getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT;
        if (modeW == MeasureSpec.EXACTLY && modeH == MeasureSpec.EXACTLY) {
            super.onMeasure(specW, specH);
        } else if (modeW == MeasureSpec.EXACTLY) {
            specH = MeasureSpec.makeMeasureSpec(calcH(sizeW), MeasureSpec.EXACTLY);
            super.onMeasure(specW, specH);
        } else if (modeH == MeasureSpec.EXACTLY) {
            specW = MeasureSpec.makeMeasureSpec(calcW(sizeH), MeasureSpec.EXACTLY);
            super.onMeasure(specW, specH);
        } else if (modeW == MeasureSpec.AT_MOST && modeH == MeasureSpec.AT_MOST) {
            specW = MeasureSpec.makeMeasureSpec(Math.min(sizeW, calcW(sizeH)), wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
            specH = MeasureSpec.makeMeasureSpec(Math.min(calcH(sizeW), sizeH), wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
            super.onMeasure(specW, specH);
            if (wrapContent) {
                int contentW = getMeasuredWidth();
                int contentH = getMeasuredHeight();
                setMeasuredDimension(Math.max(contentW, calcW(contentH)), Math.max(contentH, calcH(contentW)));
            }
        } else {
            if (modeW == MeasureSpec.AT_MOST) {
                specW = MeasureSpec.makeMeasureSpec(sizeW, wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
                specH = MeasureSpec.makeMeasureSpec(calcH(sizeW), wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
            }
            if (modeH == MeasureSpec.AT_MOST) {
                specW = MeasureSpec.makeMeasureSpec(calcW(sizeH), wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
                specH = MeasureSpec.makeMeasureSpec(sizeH, wrapContent ? MeasureSpec.AT_MOST : MeasureSpec.EXACTLY);
            }
            super.onMeasure(specW, specH);
            if (wrapContent) {
                int contentW = getMeasuredWidth();
                int contentH = getMeasuredHeight();
                setMeasuredDimension(Math.max(contentW, calcW(contentH)), Math.max(contentH, calcH(contentW)));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        viewRect.set(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        maskPath.reset();
        if (cornerRadius > 0) {
            maskPath.addRoundRect(viewRect, cornerRadius, cornerRadius, Path.Direction.CW);
        } else {
            maskPath.addRect(viewRect, Path.Direction.CW);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        @SuppressLint("WrongConstant") int saveCount = canvas.saveLayer(null, clipPaint, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        canvas.drawPath(maskPath, maskPaint);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG));
        super.dispatchDraw(canvas);
    }

    private int calcW(int height) {
        return Math.round(height * widthRatio / heightRatio);
    }

    private int calcH(int width) {
        return Math.round(width * heightRatio / widthRatio);
    }
}
