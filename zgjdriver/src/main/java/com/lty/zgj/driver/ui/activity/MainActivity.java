package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.bean.WebSocketManager;
import com.lty.zgj.driver.bean.WebSocketRequst;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.CustomViewPager;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.router.Router;

public class MainActivity extends AbsBaseWebSocketActivity implements OnTabSelectListener {
    @BindView(R.id.viewpager)
    CustomViewPager vp;
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"发车", "待出行"};
    private MyPagerAdapter mAdapter;


    @Override
    protected void initView() {
        initData();
    }


    public void initData() {
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
    public void onTabSelect(int position) {
    }

    @Override
    public void onTabReselect(int position) {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {

    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {

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

                SetActivity.launch(context);

                break;
            case R.id.tv_msg:
                break;
        }
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(MainActivity.class)
                .launch();
    }
}
