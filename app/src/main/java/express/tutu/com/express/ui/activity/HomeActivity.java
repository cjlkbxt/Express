package express.tutu.com.express.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import express.tutu.com.express.R;
import express.tutu.com.express.ui.adapter.ViewPagerAdapter;
import express.tutu.com.express.ui.fragment.FindExpressFragment;
import express.tutu.com.express.ui.fragment.MineFragment;
import express.tutu.com.express.ui.fragment.MyCargoFragment;
import express.tutu.com.express.ui.fragment.OrderManageFragment;
import express.tutu.com.express.utils.BottomNavigationViewHelper;


/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class HomeActivity extends BaseActivity{

    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        initListener();
        setupViewPager(mViewPager);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
    }

    private void initData() {
    }

    private void initListener() {
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //BottomNaviationView和ViewPager联动,当BottomNaviationView的某个tab按钮被选中了,同时设置ViewPager对应的页面被选中
                switch (item.getItemId()) {
                    case R.id.tab_one:
                        mViewPager.setCurrentItem(0, false);
                        break;
                    case R.id.tab_two:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.tab_three:
                        mViewPager.setCurrentItem(2, false);
                        break;
                    case R.id.tab_four:
                        mViewPager.setCurrentItem(3, false);
                        break;
                }
                return false;
            }
        });

        //系统默认选中第一个,但是系统选中第一个不会执行onNavigationItemSelected(MenuItem)方法,如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mBottomNavigationView.setSelectedItemId(R.id.tab_one);//根据具体情况调用
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    mBottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = mBottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //为viewpager设置adapter
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new MyCargoFragment());
        adapter.addFragment(new FindExpressFragment());
        adapter.addFragment(new OrderManageFragment());
        adapter.addFragment(new MineFragment());
        viewPager.setAdapter(adapter);
    }


}
