package com.lty.zgj.driver.websocketdemo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lty.zgj.driver.BaseApplication;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.WebSocket.AbsBaseWebSocketService;
import com.lty.zgj.driver.WebSocket.CommonResponse;
import com.lty.zgj.driver.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lty.zgj.driver.broadcast.ToMainActivityBroadcastReceiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Created by zk721 on 2018/1/28.
 */

public class WebSocketService extends AbsBaseWebSocketService {

    @Override
    protected String getConnectUrl() {
        return "ws://10.1.254.172:18089/diverSocket";
//        return "ws://192.168.2.131:18089/diverSocket";
    }

    @Override
    protected void dispatchResponse(String textResponse) {
        //处理数据
        try {

            Log.e("WebSocketService", "textResponse-----"+textResponse);
            String substring = textResponse.substring(1, textResponse.length() - 16);
            Log.e("WebSocketService", "substring-----"+substring);

            JSONObject mJsonObject = new JSONObject(substring);
            JSONObject headerPacket = mJsonObject.getJSONObject("headerPacket");
            String body = mJsonObject.getString("body");

            int msgId = headerPacket.getInt("msgId");
            if(msgId == 0x301){
                onReceiveMessageData(BaseApplication.getContext(), body);
            }

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


    public void onReceiveMessageData(Context context, String gtTransmitMessage) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

//        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jianshu.com/p/82e249713f1b"));
//        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        int id = (int) (System.currentTimeMillis() / 1000);
        Intent intent = new Intent(context, ToMainActivityBroadcastReceiver.class);
        intent.putExtra("notificationId",id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setAutoCancel(true);
        builder.setContentTitle("普通通知");
        mNotificationManager.notify(1, builder.build());

    }
}
