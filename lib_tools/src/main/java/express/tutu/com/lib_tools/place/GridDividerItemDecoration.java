package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import express.tutu.com.lib_tools.R;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration{

    private Drawable divider;
    private int spanCount;
    private boolean includeEdge;
    private int colorRes;
    private int dividerWidth;

    public GridDividerItemDecoration(Context context, int spanCount, boolean includeEdge) {
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
        divider = new ColorDrawable(ContextCompat.getColor(context, R.color.gray_divider));
        dividerWidth = 1;
    }

    public GridDividerItemDecoration(Context context, int spanCount, int colorRes, boolean includeEdge) {
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
        this.colorRes = colorRes;
        divider = new ColorDrawable(ContextCompat.getColor(context, colorRes));
        dividerWidth = 1;
    }

    public GridDividerItemDecoration(Context context, int spanCount, int dividerWidth, int colorRes, boolean includeEdge) {
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
        this.dividerWidth = dividerWidth;
        this.colorRes = colorRes;
        divider = new ColorDrawable(ContextCompat.getColor(context, colorRes));
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {

        drawOverVertical(c, parent, state);
        drawOverHorizontal(c, parent, state);

    }

    private void drawOverVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();


        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - (includeEdge ? 0 : spanCount); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));
//            final int top = child.getTop() - params.bottomMargin;
            final int bottom = top + dividerWidth;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    private void drawOverHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();


        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (!includeEdge && (i + 1) % spanCount == 0) continue;

            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            final int left = child.getRight() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationX(child));
//            final int top = child.getTop() - params.bottomMargin;
            final int right = left + dividerWidth;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = dividerWidth - column * dividerWidth / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * dividerWidth / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = dividerWidth;
            }
            outRect.bottom = dividerWidth; // item bottom
        } else {
            outRect.left = column * dividerWidth / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = dividerWidth - (column + 1) * dividerWidth / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = dividerWidth; // item top
            }
        }
    }
}
