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

    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_login;
    }

    private void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mSocketClient = new WebSocketClient(new URI("ws://10.27.0.197:2017/"), new Draft_10()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Log.d("picher_log", "打开通道" + handshakedata.getHttpStatus());
                            handler.obtainMessage(0, handshakedata).sendToTarget();
                        }

                        @Override
                        public void onMessage(String message) {
                            Log.d("picher_log", "接收消息" + message);
                            handler.obtainMessage(0, message).sendToTarget();
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d("picher_log", "通道关闭");
                            handler.obtainMessage(0, reason).sendToTarget();
                        }

                        @Override
                        public void onError(Exception ex) {
                            Log.d("picher_log", "链接错误");
                        }
                    };
                    mSocketClient.connect();

                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    @SuppressLint("HandlerLeak") Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };
}
