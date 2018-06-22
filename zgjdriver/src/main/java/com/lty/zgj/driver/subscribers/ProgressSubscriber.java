package com.lty.zgj.driver.subscribers;

import android.content.Context;

import com.lty.zgj.driver.BaseApplication;
import com.lty.zgj.driver.R;
import com.lty.zgj.driver.net.ApiException;
import com.lty.zgj.driver.progress.ProgressCancelListener;
import com.lty.zgj.driver.progress.ProgressDialogHandler;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import cn.droidlover.xdroid.dialog.ShowDialogRelative;
import rx.Subscriber;

/**
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理
 * Created by liukun on 16/3/10.
 */
public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private boolean isNoToast = false;//当为true的时候，该类不弹土司
    private SubscriberOnNextListener mSubscriberOnNextListener;
    private ProgressDialogHandler mProgressDialogHandler;

    private Context context;

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
    }

    /**
     * @param mSubscriberOnNextListener
     * @param context
     * @param isShowDialog              是否显示加载框
     */
    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context, boolean isShowDialog) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        if (isShowDialog) {
            mProgressDialogHandler = new ProgressDialogHandler(context, this, true);
        }
    }

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context, boolean isNoToast, boolean cancelable) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
        this.isNoToast = isNoToast;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, cancelable);
    }

    public ProgressSubscriber(SubscriberOnNextListener mSubscriberOnNextListener, Context context, String str) {
        this.mSubscriberOnNextListener = mSubscriberOnNextListener;
        this.context = context;
    }

    private void showProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {
        showProgressDialog();
    }

    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {

        if (e instanceof SocketTimeoutException) {
            ShowDialogRelative.toastDialog(BaseApplication.getContext(), BaseApplication.getContext().getResources().getString(R.string.tv_sorket_timeout));
        }else if (e instanceof ApiException) {
            ShowDialogRelative.toastDialog(BaseApplication.getContext(),e.getMessage());
        }else if (e instanceof ConnectException){
            ShowDialogRelative.toastDialog(BaseApplication.getContext(), BaseApplication.getContext().getResources().getString(R.string.connection_fail));
        }else if(e instanceof SocketException){
            ShowDialogRelative.toastDialog(BaseApplication.getContext(), "服务器连接异常");
        }
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onError(e);
        }
        dismissProgressDialog();
    }

    /**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     */
    @Override
    public void onNext(T t) {
        if (mSubscriberOnNextListener != null) {
            mSubscriberOnNextListener.onNext(t);
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}