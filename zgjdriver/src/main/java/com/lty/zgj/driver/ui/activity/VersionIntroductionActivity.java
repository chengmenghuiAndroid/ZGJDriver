package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.websocketdemo.WebSocketService;

import butterknife.BindView;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/11.
 */

public class VersionIntroductionActivity extends AbsBaseWebSocketActivity {
    @BindView(R.id.nav_button)
    ImageView navButton;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_version_introduction;
    }

    @Override
    protected void initView() {
        tvTitle.setText("软件版本介绍");
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
                .to(VersionIntroductionActivity.class)
                .launch();
    }

}