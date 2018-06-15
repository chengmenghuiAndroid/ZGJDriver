package com.lty.zgj.driver.core.tool;

import android.content.Context;
import android.content.Intent;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/6/14.
 */

public class Utils {

    /**
     * 发送广播 关闭Activity
     * @param context
     */
    public static void sendFinishActivityBroadcast(Context context, String receiver_action_finish) {
        Intent intent = new Intent(receiver_action_finish);
        context.sendBroadcast(intent);
    }

    /**
     * double转String,保留小数点后两位
     * @param num
     * @return
     */
    public static double doubleToString(double num){
        //使用0.00不足位补0，#.##仅保留有效位
        String format = new DecimalFormat("0.0000000").format(num);
        double aDouble = Double.parseDouble(format);
        return aDouble;
    }
}
