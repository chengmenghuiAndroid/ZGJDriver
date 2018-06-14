package com.lty.zgj.driver.websocketdemo;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zk721 on 2018/1/28.
 */

public class WebSocketService extends AbsBaseWebSocketService {

    @Override
    protected String getConnectUrl() {
        return "ws://10.1.254.172:18089/diverSocket";
    }

    @Override
    protected void dispatchResponse(String textResponse) {
        //处理数据
        try {

            Log.e("WebSocketService", "textResponse-----"+textResponse);
            String substring = textResponse.substring(1, textResponse.length() - 16);
            Log.e("WebSocketService", "substring-----"+substring);
            CommonResponse<String> response = JSON.parseObject(substring, new TypeReference<CommonResponse<String>>() {});

            if (response == null) {
                EventBus.getDefault().post(new WebSocketSendDataErrorEvent("", textResponse, "响应数据为空"));
                return;
            }
            //此处可根据服务器接口文档进行调整，判断 code 值是否合法，如下：
//            if (response.getCode() >= 1000 && response.getCode() < 2000) {
//                EventBus.getDefault().post(response);
//            }else{
//                EventBus.getDefault().post(new WebSocketSendDataErrorEvent(response.getCommand().getPath(), textResponse, response.getMsg()));
//            }
            EventBus.getDefault().post(response);
        }catch(Exception e){
            //一般由于 JSON 解析时出现异常
            EventBus.getDefault().post(new WebSocketSendDataErrorEvent("", textResponse, "数据异常:" + e.getMessage()));
        }
    }
}
