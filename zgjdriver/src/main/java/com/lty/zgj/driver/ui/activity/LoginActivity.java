package com.lty.zgj.driver.ui.activity;


import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketActivity;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.bean.LoginModel;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.MD5Util;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.ClearEditText;
import com.lty.zgj.driver.weight.CountDownTimerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.client.WebSocketClient;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.cache.SharedPref;
import cn.droidlover.xdroidbase.router.Router;

/**
 * Created by Administrator on 2018/6/5.
 */

public class LoginActivity extends AbsBaseWebSocketActivity {

    private static final String THIS_FILE = "LoginActivity";
    @BindView(R.id.et_pws)
    ClearEditText et_pws;
    @BindView(R.id.et_phone_number)
    ClearEditText etPhoneNumber;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ar_login_btn)
    AutoLinearLayout arLoginBtn;

    private WebSocketClient mSocketClient;
    private CountDownTimerUtils countDownTimerUtils;
    /**
     * 最后按下的时间
     */
    private long lastTime;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_driver_login;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        etPhoneNumber.setText("1234567890");
        et_pws.setText("12345");
        initCountDownTimer();

    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        if (response != null ) {

        }
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
                JSONObject param = new JSONObject();
                String phoneNumber = etPhoneNumber.getText().toString();
                String pws = et_pws.getText().toString();

                if(TextUtils.isEmpty(phoneNumber)){
                    ShowDialogRelative.toastDialog(context, "用户不能为空");
                    return;
                }
                if(TextUtils.isEmpty(pws)){
                    ShowDialogRelative.toastDialog(context, "用户密码不能为空");
                    return;
                }

                param.put("account", phoneNumber);
                param.put("pwd", MD5Util.getMD5(pws));
                fetchLoginData(param);
                Log.e(THIS_FILE, "param----"+param);
                break;

                case R.id.tv_send_code:
                    countDownTimerUtils.start();
                break;
        }
    }


    private void initCountDownTimer() {
        countDownTimerUtils = new CountDownTimerUtils(tvSendCode, 10 * 1000, 1000);
    }


    private void fetchLoginData(JSONObject param) {
        ObjectLoader.getInstance().getLoginData(new ProgressSubscriber<LoginModel>(new SubscriberOnNextListener<LoginModel>() {
            @Override
            public void onNext(LoginModel loginModel) {
                if(loginModel != null){
                    String driver_custom_token = loginModel.getDRIVER_CUSTOM_TOKEN();
                    SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, driver_custom_token);
                    Log.e(THIS_FILE, "driver_custom_token-----"+driver_custom_token);
                    SharedPref.getInstance(context).putBoolean(Constant.isLoginSuccess, true);
                    MainActivity.launch(context);
                    finish();
                }
//
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","");
            }
        }, context), param);
    }

    public static void launch(Activity activity) {
        Router.newIntent(activity)
                .to(LoginActivity.class)
                .launch();
    }

    /**
     * 按二次返回键退出应用
     */
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed();
        } else {
            ShowDialogRelative.toastDialog(context, "再按一次退出应用");
            lastTime = currentTime;
        }

    }

}
