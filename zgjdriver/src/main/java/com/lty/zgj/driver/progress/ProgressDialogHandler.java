package com.lty.zgj.driver.progress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;

import com.lty.zgj.driver.progress.kprogresshud.KProgressHUD;

/**
 * Created by liukun on 16/3/10.
 */
public class ProgressDialogHandler extends Handler {

    public static final int SHOW_PROGRESS_DIALOG = 1;
    public static final int DISMISS_PROGRESS_DIALOG = 2;

    //    private ProgressDialog pd;
    private KProgressHUD pd;

    private Context context;
    private boolean cancelable;
    private ProgressCancelListener mProgressCancelListener;

    public ProgressDialogHandler(Context context, ProgressCancelListener mProgressCancelListener,
                                 boolean cancelable) {
        super();
        this.context = context;
        this.mProgressCancelListener = mProgressCancelListener;
        this.cancelable = cancelable;
    }

    private void initProgressDialog(){
        if (context==null){
            return;
        }
        if (pd == null) {
//            pd = new ProgressDialog(context);

            pd = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setDimAmount(0.5f)
                    .setCancellable(cancelable)
                    .setSize(50, 50)
                    .setCancellable(new DialogInterface.OnCancelListener() {
                        @Override public void onCancel(DialogInterface dialogInterface) {
                            mProgressCancelListener.onCancelProgress();
                        }
                    });

//            pd.setCancelable(cancelable);

//            if (cancelable) {
//                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//                        mProgressCancelListener.onCancelProgress();
//                    }
//                });

//            }

            if (!pd.isShowing()) {
                pd.show(context);
            }
        }
    }

    private void dismissProgressDialog(){
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_PROGRESS_DIALOG:
                initProgressDialog();
                break;
            case DISMISS_PROGRESS_DIALOG:
                dismissProgressDialog();
                break;
        }
    }

}
