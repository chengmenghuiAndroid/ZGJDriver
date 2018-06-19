package com.lty.zgj.driver.ui.activity;

import android.app.Activity;
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

public class PersonalHelpActivity extends AbsBaseWebSocketActivity{

    @BindView(R.id.tv_title)
    TextView title;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_help;
    }

    @Override
    protected void initView() {
        title.setText("使用帮助");
    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        closeRoundProgressDialog();//关闭加载对话框
    }

    @Override
    protected void onErrorResponse(WebSocketSendDataErrorEvent response) {
        closeRoundProgressDialog();//关闭加载对话框
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(PersonalHelpActivity.class)
                .launch();
    }
}
