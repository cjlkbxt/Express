package express.tutu.com.express.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import express.tutu.com.express.ui.fragment.FindExpressFragment;
import express.tutu.com.express.ui.fragment.MineFragment;
import express.tutu.com.express.ui.fragment.MyCargoFragment;
import express.tutu.com.express.ui.fragment.OrderManageFragment;

/**
 * Created by cjlkbxt on 2018/7/7/007.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
