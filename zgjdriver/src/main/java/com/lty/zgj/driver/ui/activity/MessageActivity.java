package com.lty.zgj.driver.ui.activity;

import android.app.Activity;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.websocketdemo.WebSocketService;

import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/14.
 */

public class MessageActivity extends AbsBaseWebSocketActivity{

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mssage;
    }

    @Override
    protected void initView() {

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
                .to(MessageActivity.class)
                .launch();
    }
}
