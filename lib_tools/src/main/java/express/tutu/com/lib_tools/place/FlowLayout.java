package express.tutu.com.lib_tools.place;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/18/018.
 */

public class FlowLayout extends ViewGroup{

    private int mGravity = (isIcs() ? Gravity.START : Gravity.LEFT) | Gravity.TOP;

    private final List<List<View>> mLines = new ArrayList<List<View>>();
    private final List<Integer> mLineHeights = new ArrayList<Integer>();
    private final List<Integer> mLineMargins = new ArrayList<Integer>();

    private int fixLines = -1; // -1表示行数没有限制

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.FlowLayout, defStyle, 0);

        try {
            int index = a.getInt(R.styleable.FlowLayout_android_gravity, -1);
            if (index > 0) {
                setGravity(index);
            }
            fixLines = a.getInt(R.styleable.FlowLayout_fixFlowLines, -1);
            if (fixLines < -1) {
                fixLines = -1;
            }
        } finally {
            a.recycle();
        }

    }


    public void setFixFlowLines(int lines) {
        if (lines < -1) {
            lines = -1;
        }
        this.fixLines = lines;
        requestLayout();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (fixLines == 0) {
            return;
        }

        int sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec);

        int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);

        int width = 0;
        int height = getPaddingTop() + getPaddingBottom();

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        int lineCount = 1;
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            boolean lastChild = i == childCount - 1;

            if (child.getVisibility() == View.GONE) {

                if (lastChild) {
                    width = Math.max(width, lineWidth);
                    height += lineHeight;
                }

                continue;
            }

//            measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height);

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidthMode = View.MeasureSpec.AT_MOST;
            int childWidthSize = sizeWidth;

            int childHeightMode = View.MeasureSpec.AT_MOST;
            int childHeightSize = sizeHeight;

            if (lp.width == LayoutParams.MATCH_PARENT) {
                childWidthMode = View.MeasureSpec.EXACTLY;
                childWidthSize -= lp.leftMargin + lp.rightMargin;
            } else if (lp.width >= 0) {
                childWidthMode = View.MeasureSpec.EXACTLY;
                childWidthSize = lp.width;
            }

            if (lp.height >= 0) {
                childHeightMode = View.MeasureSpec.EXACTLY;
                childHeightSize = lp.height;
            } else if (modeHeight == View.MeasureSpec.UNSPECIFIED) {
                childHeightMode = View.MeasureSpec.UNSPECIFIED;
                childHeightSize = 0;
            }

            child.measure(
                    View.MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                    View.MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode)
            );

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;

                if (fixLines > 0 && lineCount++ >= fixLines) {
                    if (mOverListener != null) {
                        mOverListener.callBack(fixLines);
                    }
                    break;
                }
                lineHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }

            if (lastChild) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }

        width += getPaddingLeft() + getPaddingRight();

        setMeasuredDimension(
                (modeWidth == View.MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == View.MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    OverFixLineCallBack mOverListener;

    public void setOverFixLinesListener(OverFixLineCallBack linesListener) {
        mOverListener = linesListener;
    }

    public void showAllLines() {
        fixLines = Integer.MAX_VALUE;
        requestLayout();
    }

    public void justsetShowAllLines() {
        fixLines = Integer.MAX_VALUE;
    }

    public interface OverFixLineCallBack {
        void callBack(int lines);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (fixLines == 0) {
            return;
        }
        mLines.clear();
        mLineHeights.clear();
        mLineMargins.clear();

        int width = getWidth();
        int height = getHeight();

        int linesSum = getPaddingTop();

        int lineWidth = 0;
        int lineHeight = 0;
        @SuppressLint("DrawAllocation") List<View> lineViews = new ArrayList<View>();

        float horizontalGravityFactor;
        switch ((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK)) {
            case Gravity.LEFT:
            default:
                horizontalGravityFactor = 0;
                break;
            case Gravity.CENTER_HORIZONTAL:
                horizontalGravityFactor = .5f;
                break;
            case Gravity.RIGHT:
                horizontalGravityFactor = 1;
                break;
        }

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            if (child.getVisibility() == View.GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            if (lineWidth + childWidth > width) {
                mLineHeights.add(lineHeight);
                mLines.add(lineViews);
                mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

                linesSum += lineHeight;

                if (fixLines != -1 && mLines.size() >= fixLines) {
                    break;
                }

                lineHeight = 0;
                lineWidth = 0;
                lineViews = new ArrayList<View>();
            }

            lineWidth += childWidth;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);

            if (i == getChildCount() - 1) {
                mLineHeights.add(lineHeight);
                mLines.add(lineViews);
                mLineMargins.add((int) ((width - lineWidth) * horizontalGravityFactor) + getPaddingLeft());

                linesSum += lineHeight;
            }
        }

        int verticalGravityMargin = 0;
        switch ((mGravity & Gravity.VERTICAL_GRAVITY_MASK)) {
            case Gravity.TOP:
            default:
                break;
            case Gravity.CENTER_VERTICAL:
                verticalGravityMargin = (height - linesSum) / 2;
                break;
            case Gravity.BOTTOM:
                verticalGravityMargin = height - linesSum;
                break;
        }

        int numLines = mLines.size();

        int left;
        int top = getPaddingTop();

        for (int i = 0; i < numLines; i++) {

            lineHeight = mLineHeights.get(i);
            lineViews = mLines.get(i);
            left = mLineMargins.get(i);

            int children = lineViews.size();

            for (int j = 0; j < children; j++) {

                View child = lineViews.get(j);

                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                // if height is match_parent we need to remeasure child to line height
                if (lp.height == LayoutParams.MATCH_PARENT) {
                    int childWidthMode = View.MeasureSpec.AT_MOST;
                    int childWidthSize = lineWidth;

                    if (lp.width == LayoutParams.MATCH_PARENT) {
                        childWidthMode = View.MeasureSpec.EXACTLY;
                    } else if (lp.width >= 0) {
                        childWidthMode = View.MeasureSpec.EXACTLY;
                        childWidthSize = lp.width;
                    }

                    child.measure(
                            View.MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
                            View.MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin, View.MeasureSpec.EXACTLY)
                    );
                }

                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();

                int gravityMargin = 0;

                if (Gravity.isVertical(lp.gravity)) {
                    switch (lp.gravity) {
                        case Gravity.TOP:
                        default:
                            break;
                        case Gravity.CENTER_VERTICAL:
                        case Gravity.CENTER:
                            gravityMargin = (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2;
                            break;
                        case Gravity.BOTTOM:
                            gravityMargin = lineHeight - childHeight - lp.topMargin - lp.bottomMargin;
                            break;
                    }
                }

                child.layout(left + lp.leftMargin,
                        top + lp.topMargin + gravityMargin + verticalGravityMargin,
                        left + childWidth + lp.leftMargin,
                        top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin);

                left += childWidth + lp.leftMargin + lp.rightMargin;

            }

            top += lineHeight;
            if (monAfterMeasuredListener != null) {
                monAfterMeasuredListener.afterMeasured(this, mLines.size());
            }
        }

    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && p instanceof LayoutParams;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= isIcs() ? Gravity.START : Gravity.LEFT;
            }

            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }

            mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return mGravity;
    }

    /**
     * @return <code>true</code> if device is running ICS or grater version of Android.
     */
    private static boolean isIcs() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FlowLayout_Layout);

            try {
                gravity = a.getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1);
            } finally {
                a.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }

    public int getDataIndexByLine(int line) {
        if (mLines.size() == 0) {
            return -1;
        }
        int result = 0;
        for (int i = 0; i < line; i++) {
            result += mLines.get(i).size();
        }
        return result;
    }

    public void setOnAfterMeasuredListener(onAfterMeasuredListener l) {
        monAfterMeasuredListener = l;
    }

    private onAfterMeasuredListener monAfterMeasuredListener;

    public interface onAfterMeasuredListener {
        void afterMeasured(FlowLayout v, int lineNum);
    }

    public int getFixLines() {
        return fixLines;
    }
}
