package com.lty.zgj.driver.core.tool;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.CustomDownloadingDialogListener;
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.weight.BaseDialogV;

import static com.allenliu.versionchecklib.v2.ui.VersionService.builder;
import static com.lty.zgj.driver.core.tool.StringUtil.getString;


/**
 * Created by chengmenghui on 2018/1/22.
 * App更新
 */

public class AllenVersionUtils {


    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData 内部是一个Bundle
     * @param versionurl 更新url
     * @param contentdesc 更新描述
     */
    public UIData crateUIData(String versionurl, String contentdesc) {
        UIData uiData = UIData.create();
//        uiData.setTitle("e路线"+versionid+"版震撼来袭");
        String string = getString(R.string.update_title);
        uiData.setTitle(string);
        uiData.setDownloadUrl(versionurl);
        uiData.setContent(contentdesc);
        return uiData;
    }

//    private NotificationBuilder createCustomNotification() {
//        return NotificationBuilder.create()
//                .setRingtone(true)
//                .setIcon(R.mipmap.dialog4)
//                .setTicker("custom_ticker")
//                .setContentTitle("custom title")
//                .setContentText(getString(R.string.custom_content_text));
//    }



    public CustomVersionDialogListener createCustomDialogTwo() {
        return (context, versionBundle) -> {
            BaseDialogV baseDialog = new BaseDialogV(context, R.style.BaseDialog, R.layout.custom_dialog_two_layout);
            TextView textView = baseDialog.findViewById(R.id.tv_msg);
            textView.setText(versionBundle.getContent());
            baseDialog.setCanceledOnTouchOutside(false);
            return baseDialog;
        };
    }


    /**
     * 自定义下载中对话框，下载中会连续回调此方法 updateUI
     * 务必用库传回来的context 实例化你的dialog
     *
     * @return
     */
    public CustomDownloadingDialogListener createCustomDownloadingDialog(Context context) {
        return new CustomDownloadingDialogListener() {
            @Override
            public Dialog getCustomDownloadingDialog(Context context, int progress, UIData versionBundle) {
                BaseDialogV baseDialog = new BaseDialogV(context, R.style.BaseDialog, R.layout.custom_download_layout);
                return baseDialog;
            }

            @Override
            public void updateUI(Dialog dialog, int progress, UIData versionBundle) {
                TextView tvProgress = dialog.findViewById(R.id.tv_progress);
                ProgressBar progressBar = dialog.findViewById(R.id.pb);
                progressBar.setProgress(progress);
                tvProgress.setText( context.getString(R.string.versionchecklib_progress, progress));
            }
        };
    }

    public void  checkLastVersion(int versionResult, Context context, String versionurl, String contentdesc) {
        if(versionResult == -1){//版本号 versionName < versionid
            builder = AllenVersionChecker
                    .getInstance()
                    .downloadOnly(crateUIData(versionurl, contentdesc))//下载apk
//                    .setForceRedownload(true) // 是否强制下载
//                .setNotificationBuilder(createCustomNotification());
                    .setCustomVersionDialogListener(createCustomDialogTwo());
            builder.excuteMission(context);
        }
    }

    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     *
     */
    private static class SingletonHolder {
        private static AllenVersionUtils instance = new AllenVersionUtils();
    }

    public static AllenVersionUtils getInstance() {
        return SingletonHolder.instance;
    }
}
