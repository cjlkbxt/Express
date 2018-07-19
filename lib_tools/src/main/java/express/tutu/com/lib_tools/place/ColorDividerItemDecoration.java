package express.tutu.com.lib_tools.place;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class ColorDividerItemDecoration extends RecyclerView.ItemDecoration {

    private float mDividerHeight;

    private Paint mPaint;
    private int color;

    public ColorDividerItemDecoration(@ColorInt int color) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        this.color = color;
        mPaint.setColor(color);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

//        //第一个ItemView不需要在上面绘制分割线
//        if (parent.getChildAdapterPosition(view) != 0){
//            //这里直接硬编码为1px
//            outRect.bottom = 1;
//            mDividerHeight = 1;
//        }
        //这里直接硬编码为1px
        outRect.bottom = 1;
        mDividerHeight = 1;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        for ( int i = 0; i < childCount; i++ ) {
            View view = parent.getChildAt(i);

            int index = parent.getChildAdapterPosition(view);
            //最后一个ItemView不需要绘制
            if ( index == childCount - 1 ) {
                continue;
            }

            float dividerTop = view.getBottom() ;
            float dividerLeft = parent.getPaddingLeft();
            float dividerBottom = view.getBottom() + mDividerHeight;
            float dividerRight = parent.getWidth() - parent.getPaddingRight();

            c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,mPaint);
        }
    }
}
