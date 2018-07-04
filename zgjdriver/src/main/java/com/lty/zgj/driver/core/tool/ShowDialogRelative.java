package com.lty.zgj.driver.core.tool;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.droidlover.xdroid.R;


/**
 * Created by asus-0000000 on 2016/8/23.
 */
public class ShowDialogRelative {
    private static Toast toast;

    public static void toastDialog(Context activity, String info) {
        //System.out.println("=======gold_num======"+info);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.toastdialog, null);

        TextView gaingold_num = (TextView) layout.findViewById(R.id.gaingoid_num);
        try {

            gaingold_num.setText(info);
            if (toast == null){
                toast = new Toast(activity.getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.setView(layout);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void toastLongDialog(Context activity, String info) {
        //System.out.println("=======gold_num======"+info);

        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.toastdialog, null);

        TextView gaingold_num = (TextView) layout.findViewById(R.id.gaingoid_num);
        try {

            gaingold_num.setText(info);
            if (toast == null){
                toast = new Toast(activity.getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 200);
                toast.setDuration(Toast.LENGTH_LONG);
            }
            toast.setView(layout);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}

