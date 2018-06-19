package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.lty.zgj.driver.WebSocket.WebSocketManager;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.bean.LoginWebWebSocketModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.GsonUtils;
import com.lty.zgj.driver.ui.fragment.DepartFragment;
import com.lty.zgj.driver.ui.fragment.WaitGoingOutFragment;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.CustomViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

public class MainActivity extends AbsBaseWebSocketActivity implements OnTabSelectListener {
    private static final String THIS_FILE = "MainActivity";
    @BindView(R.id.viewpager)
    CustomViewPager vp;
    @BindView(R.id.tl_1)
    SlidingTabLayout tabLayout;


    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {"发车", "待出行"};
    private MyPagerAdapter mAdapter;
    private String webSocketJson;
    /**
     * 最后按下的时间
     */
    private long lastTime;
    private boolean isLoad;
    private boolean isLoadData;


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        initData();
//        webSocketConnectLogin();
    }


    private void webSocketConnectLogin() {
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x102, token, null);
        sendText(webSocketJson);//登录鉴权
        Log.e(THIS_FILE, "token---SharedPref----" + token+"-----"+"main_webSock");
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
        closeRoundProgressDialog();//关闭加载对话框
        CommonResponse.BodyBean body = response.getBody();
        int code = body.getCode();

        //token过期 去登录界面
        if (code == 104) {
            LoginActivity.launch(context);
            finish();
        } else {
            String data = body.getData();
            SharedPref.getInstance(context).putString(Constant.USER_INFO, data);
            LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
            String token = loginModel.getToken();//更新token
            SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, token);
            SharedPref.getInstance(context).putInt(Constant.WEBSOCKT_CONT, 1);
            Log.e("token", "token-----" + token+"====");
        }
    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {
        closeRoundProgressDialog();//关闭加载对话框
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
