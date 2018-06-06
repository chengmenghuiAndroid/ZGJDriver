package com.lty.zgj.driver.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.weight.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends BaseXActivity implements OnTabSelectListener {
    @BindView(R.id.viewpager)
    CustomViewPager vp;
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"发车", "待出行"};
    private MyPagerAdapter mAdapter;

    @Override
    public void initData(Bundle savedInstanceState) {
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mFragments.clear();
        mFragments.add(new DepartFragment());
        mFragments.add(new WaitGoingOutFragment());
        tabLayout.setOnTabSelectListener(this);
        vp.setAdapter(mAdapter);
        tabLayout.setViewPager(vp);
        vp.setPagingEnabled(false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
