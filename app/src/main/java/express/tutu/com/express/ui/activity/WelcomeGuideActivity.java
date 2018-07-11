package express.tutu.com.express.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.express.R;
import express.tutu.com.express.constants.Constant;
import express.tutu.com.express.ui.adapter.GuideViewPagerAdapter;
import express.tutu.com.express.utils.SpUtil;

/**
 * Created by cjlkbxt on 2018/7/11/011.
 * 引导页面
 */

public class WelcomeGuideActivity extends BaseActivity implements View.OnClickListener{

    private ViewPager mViewPager;
    private GuideViewPagerAdapter mAdapter;
    private List<View> mViews;
    private Button mBtnStart;

    // 引导页图片资源
    private static final int[] mPics = { R.layout.guid_view1,
            R.layout.guid_view2, R.layout.guid_view3, R.layout.guid_view4 };

    // 底部小点图片
    private ImageView[] mIvDots;

    // 记录当前选中位置
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mViews = new ArrayList<View>();

        // 初始化引导页视图列表
        for (int i = 0; i < mPics.length; i++) {
            View view = LayoutInflater.from(this).inflate(mPics[i], null);

            if (i == mPics.length - 1) {
                mBtnStart = (Button) view.findViewById(R.id.btn_login);
                mBtnStart.setTag("enter");
                mBtnStart.setOnClickListener(this);
            }

            mViews.add(view);

        }

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        // 初始化adapter
        mAdapter = new GuideViewPagerAdapter(mViews);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new PageChangeListener());

        initDots();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 如果切换到后台，就设置下次不进入功能引导页
        SpUtil.putBoolean(WelcomeGuideActivity.this, Constant.FIRST_OPEN, true);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        mIvDots = new ImageView[mPics.length];

        // 循环取得小点图片
        for (int i = 0; i < mPics.length; i++) {
            // 得到一个LinearLayout下面的每一个子元素
            mIvDots[i] = (ImageView) ll.getChildAt(i);
            mIvDots[i].setEnabled(false);// 都设为灰色
            mIvDots[i].setOnClickListener(this);
            mIvDots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        mCurrentIndex = 0;
        mIvDots[mCurrentIndex].setEnabled(true); // 设置为白色，即选中状态
    }

    /**
     * 设置当前view
     *
     * @param position
     */
    private void setCurView(int position) {
        if (position < 0 || position >= mPics.length) {
            return;
        }
        mViewPager.setCurrentItem(position);
    }

    /**
     * 设置当前指示点
     *
     * @param position
     */
    private void setCurDot(int position) {
        if (position < 0 || position > mPics.length || mCurrentIndex == position) {
            return;
        }
        mIvDots[position].setEnabled(true);
        mIvDots[mCurrentIndex].setEnabled(false);
        mCurrentIndex = position;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("enter")) {
            enterHomeActivity();
        }

        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }


    private void enterHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        SpUtil.putBoolean(this, Constant.FIRST_OPEN, false);
        finish();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        // 当滑动状态改变时调用
        @Override
        public void onPageScrollStateChanged(int position) {
            // arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
        }

        // 当前页面被滑动时调用
        @Override
        public void onPageScrolled(int position, float arg1, int arg2) {
            // arg0 :当前页面，及你点击滑动的页面
            // arg1:当前页面偏移的百分比
            // arg2:当前页面偏移的像素位置
        }

        // 当新的页面被选中时调用
        @Override
        public void onPageSelected(int position) {
            // 设置底部小点选中状态
            setCurDot(position);
        }

    }
}
