package com.lty.zgj.driver.ui.activity;

import android.os.Bundle;
import android.os.Handler;

import com.lty.zgj.driver.R;
import com.lty.zgj.driver.base.BaseXActivity;
import com.lty.zgj.driver.core.config.Constant;

import cn.droidlover.xdroidbase.cache.SharedPref;

/**
 * Created by Administrator on 2018/6/5.
 */

public class SplashActivity extends BaseXActivity {

    private void initJump() {
        boolean isLoginSuccess = SharedPref.getInstance(context).getBoolean(Constant.isLoginSuccess, false);
        if(isLoginSuccess){
            //用户已登录
            MainActivity.launch(context);
        }else {
            //用户未登录
            LoginActivity.launch(context);

        }
        finish();

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initJump();
            }
        }, 2500);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_splash;
    }
}
