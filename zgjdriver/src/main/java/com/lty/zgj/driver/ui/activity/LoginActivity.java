package com.lty.zgj.driver.ui.activity;


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
import com.lty.zgj.driver.bean.LoginWebWebSocketModel;
import com.lty.zgj.driver.bean.WebSocketManager;
import com.lty.zgj.driver.bean.WebSocketRequst;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.GsonUtils;
import com.lty.zgj.driver.core.tool.MD5Util;
import com.lty.zgj.driver.net.ObjectLoader;
import com.lty.zgj.driver.subscribers.ProgressSubscriber;
import com.lty.zgj.driver.subscribers.SubscriberOnNextListener;
import com.lty.zgj.driver.websocketdemo.WebSocketService;
import com.lty.zgj.driver.weight.ClearEditText;
import com.lty.zgj.driver.weight.CountDownTimerUtils;
import com.zhy.autolayout.AutoLinearLayout;

import org.java_websocket.client.WebSocketClient;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import cn.droidlover.xdroidbase.cache.SharedPref;

/**
 * Created by Administrator on 2018/6/5.
 */

public class LoginActivity extends AbsBaseWebSocketActivity {

    private static final String THIS_FILE = "LoginActivity";
    @BindView(R.id.et_pws)
    ClearEditText pws;
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

        etPhoneNumber.setText("1234567890");
        pws.setText("12345");
        initCountDownTimer();

    }

    @Override
    protected Class<? extends AbsBaseWebSocketService> getWebSocketClass() {
        return WebSocketService.class;
    }

    @Override
    protected void onCommonResponse(CommonResponse<String> response) {
        if (response != null ) {

            //我们需要通过 path 判断是不是登陆接口返回的数据，因为也有可能是其他接口返回的
            closeRoundProgressDialog();//关闭加载对话框

            CommonResponse.BodyBean body = response.getBody();
            int code = body.getCode();

            if(code == 104){
                ShowDialogRelative.toastDialog(context, body.getMessage());
                return;
            }
            ShowDialogRelative.toastDialog(context, body.getMessage());
            String data = body.getData();
            LoginWebWebSocketModel loginModel = GsonUtils.parserJsonToArrayBean(data, LoginWebWebSocketModel.class);
            String token = loginModel.getToken();
            SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, token);
            MainActivity.launch(context);

            Log.e(THIS_FILE, "token----"+token);
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
                param.put("account", "1234567890");
                param.put("pwd", MD5Util.getMD5("12345"));
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
                    //f3051c3ea00c43cf81dbf711e7351bac
                    String driver_custom_token = loginModel.getDRIVER_CUSTOM_TOKEN();
                    SharedPref.getInstance(context).putString(Constant.DRIVER_CUSTOM_TOKEN, driver_custom_token);

                    Map<String, Object> params = WebSocketRequst.getInstance(context).userLogin("1234567890", MD5Util.getMD5("12345"));
                    String webSocketJson = WebSocketManager.getInstance(context).sendWebSocketJson(context, 0x102, driver_custom_token,null);
                    sendText(webSocketJson);//登录鉴权
                    Log.e(THIS_FILE, "webSocketJson----"+webSocketJson);
                }
//
            }

            @Override
            public void onError(Throwable e) {
                Log.e("TAG","");
            }
        }, context), param);
    }

}
