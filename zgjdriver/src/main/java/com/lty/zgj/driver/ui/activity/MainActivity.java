package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.weight.CustomViewPager;
import com.lty.zgj.driver.weight.StatusBarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.router.Router;

public class MainActivity extends BaseXActivity implements OnTabSelectListener {
    private static final String THIS_FILE = "MainActivity";
    @BindView(R.id.viewpager)
    CustomViewPager vp;
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"发车", "待出行"};
    private MyPagerAdapter mAdapter;

    /**
     * 最后按下的时间
     */
    private long lastTime;
    private boolean isLoad;
    private boolean isLoadData;

    @Override
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtils.with(this)
                .setDrawable(getResources().getDrawable(R.mipmap.bg_status_bar))
                .init();
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
                SetActivity.launch(context);
                break;
            case R.id.tv_msg:
                MessageActivity.launch(context);
                break;
        }
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(MainActivity.class)
                .launch();
    }


    /**
     * 按二次返回键退出应用
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime < 2 * 1000) {
            finish();
            System.exit(0);
        } else {
            ShowDialogRelative.toastDialog(context, "再按一次退出应用");
            lastTime = currentTime;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constant.MAIN_REQUEST_CODE){
            if(resultCode == Constant.SET_RESULT_CODE){

            }
        }
    }
}
