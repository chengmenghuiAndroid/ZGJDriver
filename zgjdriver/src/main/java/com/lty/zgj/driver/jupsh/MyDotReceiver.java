package com.lty.zgj.driver.jupsh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2018/5/28.
 */

public class MyDotReceiver extends BroadcastReceiver {

    private BRInteraction brInteraction;
    @Override
    public void onReceive(Context context, Intent intent) {
        // 广告发送消息后,这里接受广播(MyDotReceiver)
        if (intent.getAction().equals("MyDotReceiver")) {
            String name = intent.getExtras().getString("key");
            brInteraction.app2IsClosedcallBack(name);
        }
    }

    // 自定义接口实现对广告发送消息的处理
    public interface BRInteraction {
        public void app2IsClosedcallBack(String content);
    }

    public void setBRInteractionListener(BRInteraction brInteraction) {
        this.brInteraction = brInteraction;
    }
}
