package com.allenliu.versionchecklib.v2.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allenliu.versionchecklib.R;
import com.allenliu.versionchecklib.core.http.AllenHttp;
import com.allenliu.versionchecklib.v2.eventbus.AllenEventType;
import com.allenliu.versionchecklib.v2.eventbus.CommonEvent;

public class DownloadingActivity extends AllenBaseActivity implements DialogInterface.OnCancelListener {
    public static final String PROGRESS = "progress";
    private Dialog downloadingDialog;
    private int currentProgress = 0;
    protected boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoadingDialog();
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {

        AllenHttp.getHttpClient().dispatcher().cancelAll();
        checkForceUpdate();
        finish();
    }

    @Override
    public void receiveEvent(CommonEvent commonEvent) {
        switch (commonEvent.getEventType()) {
            case AllenEventType.UPDATE_DOWNLOADING_PROGRESS:
                int progress = (int) commonEvent.getData();
                currentProgress = progress;
                updateProgress();
                break;
            case AllenEventType.DOWNLOAD_COMPLETE:
            case AllenEventType.CLOSE_DOWNLOADING_ACTIVITY:
                destroy();
                break;
        }
    }

    @Override
    public void showDefaultDialog() {
        View loadingView = LayoutInflater.from(this).inflate(R.layout.downloading_layout, null);
        downloadingDialog = new AlertDialog.Builder(this).setTitle("").setView(loadingView).create();
        if (getVersionBuilder().getForceUpdateListener() != null)
            downloadingDialog.setCancelable(false);
        else
            downloadingDialog.setCancelable(true);

        downloadingDialog.setCanceledOnTouchOutside(false);
        ProgressBar pb = loadingView.findViewById(R.id.pb);
        TextView tvProgress = loadingView.findViewById(R.id.tv_progress);
        tvProgress.setText(String.format(getString(R.string.versionchecklib_progress), currentProgress));
        pb.setProgress(currentProgress);
        downloadingDialog.show();
    }

    @Override
    public void showCustomDialog() {
        downloadingDialog = getVersionBuilder().getCustomDownloadingDialogListener().getCustomDownloadingDialog(this, currentProgress, getVersionBuilder().getVersionBundle());
        View cancelView = downloadingDialog.findViewById(R.id.versionchecklib_loading_dialog_cancel);
        if (cancelView != null) {
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCancel(downloadingDialog);
                }
            });
        }
        downloadingDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        destroy();
        isDestroy = true;
    }

    private void destroy() {
        if (downloadingDialog != null) {
            downloadingDialog.dismiss();
        }
        finish();
    }

    private void updateProgress() {
        if (!isDestroy) {
            if (getVersionBuilder().getCustomDownloadingDialogListener() != null) {
                getVersionBuilder().getCustomDownloadingDialogListener().updateUI(downloadingDialog, currentProgress, getVersionBuilder().getVersionBundle());
            } else {
                ProgressBar pb = downloadingDialog.findViewById(R.id.pb);
                pb.setProgress(currentProgress);
                TextView tvProgress = downloadingDialog.findViewById(R.id.tv_progress);
                tvProgress.setText(String.format(getString(R.string.versionchecklib_progress), currentProgress));
                if (!downloadingDialog.isShowing())
                    downloadingDialog.show();
            }
        }
    }

    private void showLoadingDialog() {
        if (!isDestroy) {
            if (getVersionBuilder().getCustomDownloadingDialogListener() != null) {
                showCustomDialog();
            } else {
                showDefaultDialog();
            }
            downloadingDialog.setOnCancelListener(this);
        }
    }


}
