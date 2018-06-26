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
import com.lty.zgj.driver.core.config.Constant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import cn.droidlover.xdroidbase.cache.SharedPref;

/**
 * Created by zk721 on 2018/1/28.
 */

public class WebSocketService extends AbsBaseWebSocketService {

    @Override
    protected String getConnectUrl() {
        return "ws://116.205.13.132:18089/diverSocket";
    }

    @Override
    protected void dispatchResponse(String textResponse) {
        //处理数据
        try {

            Log.e("WebSocketService", "textResponse-----"+textResponse);
            String substring = textResponse.substring(1, textResponse.length() - 16);
            CommonResponse<String> response = JSON.parseObject(substring, new TypeReference<CommonResponse<String>>() {});



            Log.e("WebSocketService", "substring-----"+substring);

            JSONObject mJsonObject = new JSONObject(substring);
            JSONObject headerPacket = mJsonObject.getJSONObject("headerPacket");
            int msgId = headerPacket.getInt("msgId");

            if(msgId == 0x301){ //
                JSONObject body = mJsonObject.getJSONObject("body");
                JSONObject data = body.getJSONObject("data");
                String bodyDate = data.getString("bodyDate");
                onReceiveMessageData(BaseApplication.getContext(), bodyDate);
            }else {
                //鉴权业务类型
                EventBus.getDefault().post(response);
            }

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
        builder.setContentText(gtTransmitMessage);
        builder.setContentTitle("普通通知");
        mNotificationManager.notify(1, builder.build());

        SharedPref.getInstance(context).putInt(Constant.DOT_KET, -1);
        //消息小红点通知
        sendBroadcastReciver(context, String.valueOf(Constant.SHOW_DOT));

    }

    // 发送广播
    private void sendBroadcastReciver(Context context, String value) {
        Intent intent = new Intent("MyDotReceiver");
        intent.putExtra("key", value);
        context.sendBroadcast(intent);
    }
}
