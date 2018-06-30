package com.lty.zgj.driver.WebSocket.UI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lty.zgj.driver.core.config.Constant;

import butterknife.Unbinder;
import cn.droidlover.xdroid.kit.KnifeKit;


/**
 * AppCompatActivity 基类
 * Created by 张可 on 2017/5/22.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements IBaseActivity {

    protected final String TAG = this.getClass().getSimpleName();

    private RoundProgressDialog roundProgressDialog;
    private Unbinder unbinder;
    protected Activity context;

    private FinishActivityRecevier mRecevier;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        if (getLayoutResId() > 0) {
            setContentView(getLayoutResId());
            unbinder = KnifeKit.bind(this);
        }
        roundProgressDialog = new RoundProgressDialog(this);
        initView(savedInstanceState);
        initBind();
//        initView();

        mRecevier = new FinishActivityRecevier();
        registerFinishReciver();
    }

    protected abstract int getLayoutResId();

    /**
     * 绑定服务注册 ButterKnife 等等
     */
    protected void initBind() {

    }

    protected abstract void initView();

    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 全屏，隐藏状态栏
     */
    protected void fullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected void initToolbar(Toolbar toolbar, String title, boolean showBackBtn) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(showBackBtn);
        if (showBackBtn) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    @Override
    public void showToastMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseAppCompatActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示圆形加载对话框，默认消息（请稍等...）
     */
    @Override
    public void showRoundProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!roundProgressDialog.isShowing()) {
                    roundProgressDialog.showProgressDialog();
                }
            }
        });
    }

    /**
     * 显示圆形加载对话框
     *
     * @param msg 提示消息

    @Override*/
    public void showRoundProgressDialog(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!roundProgressDialog.isShowing()) {
                    roundProgressDialog.showProgressDialog(msg);
                }
            }
        });
    }

    /**
     * 关闭圆形加载对话框
     */
    @Override
    public void closeRoundProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (roundProgressDialog.isShowing()) {
                    roundProgressDialog.closeProgressDialog();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }




    private void registerFinishReciver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.RECEIVER_ACTION_FINISH_MAIN);
        intentFilter.addAction(Constant.RECEIVER_ACTION_FINISH_LOGIN);
        registerReceiver(mRecevier, intentFilter);
    }


    private class FinishActivityRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //根据需求添加自己需要关闭页面的action
            if (Constant.RECEIVER_ACTION_FINISH_MAIN.equals(intent.getAction())) {
                finish();
            }
        }
    }


}
