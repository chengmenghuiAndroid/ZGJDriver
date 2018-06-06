package com.lty.zgj.driver.ui.activity;

import android.os.Bundle;
import android.os.Handler;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;

/**
 * Created by Administrator on 2018/6/5.
 */

public class SplashActivity extends BaseXActivity{
    @Override
    public void initData(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launch(MainActivity.class);
                finish();
            }
        }, 1500);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_splash;
    }
}
