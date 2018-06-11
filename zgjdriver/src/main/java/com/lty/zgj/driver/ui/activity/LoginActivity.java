package com.lty.zgj.driver.ui.activity;


import android.view.View;
import android.widget.TextView;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.ClearEditText;
import com.lty.zgj.driver.weight.CountDownTimerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.java_websocket.client.WebSocketClient;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/5.
 */

public class LoginActivity extends AbsBaseWebSocketActivity {
    @BindView(R.id.et_phone_number)
    ClearEditText etPhoneNumber;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ar_login_btn)
    AutoLinearLayout arLoginBtn;

    private WebSocketClient mSocketClient;
    private CountDownTimerUtils countDownTimerUtils;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_driver_login;
    }

    @Override
    protected void initView() {
        initCountDownTimer();
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


    @OnClick({
            R.id.ar_login_btn,
            R.id.tv_send_code
    })


    public void onLoginEventClick(View v){

        switch (v.getId()){
            case R.id.ar_login_btn:
                MainActivity.launch(context);
                break;
                case R.id.tv_send_code:
                    countDownTimerUtils.start();
                break;
        }
    }


    private void initCountDownTimer() {
        countDownTimerUtils = new CountDownTimerUtils(tvSendCode, 10 * 1000, 1000);
    }
}
