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
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.zhy.autolayout.AutoRelativeLayout;

import butterknife.BindView;
import butterknife.OnClick;
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    protected void initView() {
        title.setText("设置");
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

    public static void launch(Activity activity) {
        Router.newIntent(activity)
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
                break;

        }
    }

}
