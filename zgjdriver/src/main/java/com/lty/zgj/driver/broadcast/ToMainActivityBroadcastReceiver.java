package com.lty.zgj.driver.broadcast;

/**
 * Created by Administrator on 2018/6/15.
 */

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lty.zgj.driver.ui.activity.MessageActivity;

/**
 * 跳转到消息页面的广播接收者
 * Created by Think on 2017/10/19.
 */

public class ToMainActivityBroadcastReceiver extends BroadcastReceiver {
    public static final String IS_TO_FIRST_FRAGMENT = "isToFirstFragment";

    @Override
    public void onReceive(Context context, Intent intent) {
        //用这个方法实现点击notification后的事件  不知为何不能自动清掉已点击的notification  故自己手动清就ok了
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(intent.getIntExtra("notificationId", -1));
        Intent toMainActivityIntent = new Intent(context, MessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toMainActivityIntent.putExtra(IS_TO_FIRST_FRAGMENT, true);
        context.startActivity(toMainActivityIntent);
    }
}
