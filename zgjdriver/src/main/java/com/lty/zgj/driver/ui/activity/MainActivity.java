package com.lty.zgj.driver.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.bean.WebSocketManager;
import com.lty.zgj.driver.bean.WebSocketRequst;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.weight.CustomViewPager;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

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


    @OnClick({
            R.id.tv_set,
            R.id.tv_msg
    })

    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_set:
                Map<String,Object> params = WebSocketRequst.getInstance(context).gpsUpload(111, 222,1.22222, 1.3333,"11",11);
//                String webSorkcetRequestJson = WebSocketManager.getInstance(context).webSocketRequest(context,0x101, "102", params);

                String webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x101, "102", params);
//                Log.e("MainActivity", "webSorkcetRequestJson---"+webSorkcetRequestJson);
//                String md5 = MD5Util.getMD5(webSorkcetRequestJson);

                Log.e("MainActivity", "webSorkcetRequestJson---"+webSocketJson);
                break;
            case R.id.tv_msg:
                break;
        }
    }
}
