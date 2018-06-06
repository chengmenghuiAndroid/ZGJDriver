package com.lty.zgj.driver.ui.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Administrator on 2018/6/5.
 */

public class LoginActivity extends BaseXActivity {
    private WebSocketClient mSocketClient;

    @Override
    public void initData(Bundle savedInstanceState) {
        //ToDo
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_login;
    }


    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
