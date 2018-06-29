package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.event.MessageEvent;
import com.lty.zgj.driver.event.ReceiveDateEvent;
import com.lty.zgj.driver.jupsh.MyDotReceiver;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.weight.CustomViewPager;
import com.lty.zgj.driver.weight.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

public class MainActivity extends BaseXActivity implements OnTabSelectListener , MyDotReceiver.BRInteraction{
    private static final String THIS_FILE = "MainActivity";
    @BindView(R.id.viewpager)
    CustomViewPager vp;
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout;
    @BindView(R.id.tv_msg_dot)
    ImageView msgDot;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"发车", "待出行"};
    private MyPagerAdapter mAdapter;

    /**
     * 最后按下的时间
     */
    private long lastTime;
    private boolean isLoad;
    private boolean isLoadData;
    private MyDotReceiver myDotReceiver;

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
        initBroadcastReciver();
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if(position == 0){
                    EventBus.getDefault().postSticky(new ReceiveDateEvent("", Constant.CLICK_DEPART_TAB));
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        useEventBus();

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

    private void initBroadcastReciver() {
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter .addAction("MyDotReceiver");
        myDotReceiver = new MyDotReceiver();

        // 注册此监听
        myDotReceiver.setBRInteractionListener(this);
        context.registerReceiver(myDotReceiver, mIntentFilter);
    }

    @Override
    public void app2IsClosedcallBack(String content) {
        if(content.equals(String.valueOf(Constant.SHOW_DOT))){
            getUiDelegate().visible(true, msgDot);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int dot = SharedPref.getInstance(context).getInt(Constant.DOT_KET, 0);

        if(dot == 0){
            getUiDelegate().gone(true, msgDot);
        }else {
            getUiDelegate().visible(true, msgDot);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myDotReceiver);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void mainThread(MessageEvent messageEvent) {
        tabLayout.setCurrentTab(0);
        EventBus.getDefault().postSticky(new ReceiveDateEvent(messageEvent.getItemId(), Constant.CLICK_ITEM));
    }

    @Override
    public boolean useEventBus() {
        boolean registered = EventBus.getDefault().isRegistered(this);

        return !registered;
    }
}
