package com.lty.zgj.driver.WebSocket.UI;

import android.content.Context;

import com.lty.zgj.driver.progress.kprogresshud.KProgressHUD;

/**
 * Created by 张可 on 2017/5/22.
 */

public class RoundProgressDialog {
    private KProgressHUD progressDialog;
    private Context context;

    private RoundProgressDialog(){
    }

    public RoundProgressDialog(Context context){
        this.context=context;
    }

    /**
     * 显示进度条
     */
    public  void showProgressDialog(){
        if(progressDialog==null){
//            progressDialog=new ProgressDialog(context);
            progressDialog = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setDimAmount(0.5f)
                    .setSize(50, 50);
//            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show(context);
    }

    public boolean isShowing(){
        if(null == progressDialog) return false;
        return progressDialog.isShowing();
    }

    /**
     * 显示进度条
     */
    public  void showProgressDialog(String msg){
        if(progressDialog==null){
//            progressDialog=new ProgressDialog(context);
            progressDialog = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setDimAmount(0.5f)
                    .setSize(50, 50);
//            progressDialog.setMessage(msg);
//            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show(context);
    }

    /**
     * 关闭进度条
     */
    public  void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }
}
