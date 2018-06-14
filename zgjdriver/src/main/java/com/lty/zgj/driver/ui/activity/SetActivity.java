package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.bean.WebSocketManager;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.Utils;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.zhy.autolayout.AutoRelativeLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class SetActivity extends AbsBaseWebSocketActivity {

    @BindView(R.id.tv_title)
    TextView title;
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ar_travel_history)
    AutoRelativeLayout arTravelHistory;
    @BindView(R.id.personal_information)
    AutoRelativeLayout personalInformation;
    @BindView(R.id.ar_personal_help)
    AutoRelativeLayout arPersonalHelp;
    @BindView(R.id.personal_about_zgj)
    AutoRelativeLayout personalAboutZgj;
    @BindView(R.id.ar_login_btn)
    AutoRelativeLayout arLoginBtn;

    private String webSocketJson;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        title.setText("设置");
        String token = SharedPref.getInstance(context).getString(Constant.DRIVER_CUSTOM_TOKEN, null);
        webSocketConnectLogin(token);
    }

    private void webSocketConnectLogin(String token) {
        webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x103, token,null);
    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        CommonResponse.BodyBean body = response.getBody();

    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {

    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .requestCode(Constant.MAIN_REQUEST_CODE)
                .to(SetActivity.class)
                .launch();
    }


    @OnClick({
            R.id.ar_travel_history,
            R.id.personal_information,
            R.id.ar_personal_help,
            R.id.personal_about_zgj,
            R.id.ar_login_btn

    })

    public void onClickEventSet(View v) {
        switch (v.getId()) {
            case R.id.ar_travel_history:
                break;
            case R.id.personal_information:
                PersonalInformationActivity.launch(context);
                break;
            case R.id.ar_personal_help:
                PersonalHelpActivity.launch(context);
                break;
            case R.id.personal_about_zgj:
                AboutActivity.launch(context);
                break;
            case R.id.ar_login_btn:
                logOut();
                break;

        }
    }

    /**
     *退出登录
     */
    private void logOut() {
        SharedPref.getInstance(context).putBoolean(Constant.isLoginSuccess, false);
        SharedPref.getInstance(context).remove(Constant.DRIVER_CUSTOM_TOKEN);
        sendText(webSocketJson);//登录鉴权
        LoginActivity.launch(context);
        Utils.sendFinishActivityBroadcast(context);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.SET_RESULT_CODE);
        finish();

        super.onBackPressed();
    }



}
