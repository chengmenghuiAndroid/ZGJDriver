package com.lty.zgj.driver.core.tool;

import android.content.Context;
import android.content.Intent;

import com.lty.zgj.driver.core.config.Constant;

/**
 * Created by Administrator on 2018/6/14.
 */

public class Utils {

    /**
     * 发送广播 关闭Activity
     * @param context
     */
    public static void sendFinishActivityBroadcast(Context context) {
        Intent intent = new Intent(Constant.RECEIVER_ACTION_FINISH_MAIN);
        context.sendBroadcast(intent);
    }
}
