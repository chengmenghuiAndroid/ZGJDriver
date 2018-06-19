package com.lty.zgj.driver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.lty.zgj.driver.websocketdemo.WebSocketService;


/**
 * Created by chen on 2016/8/3.
 */

public class BaseApplication extends Application {

    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static BaseApplication instance;
    private static Context context;

    //实例化一次
    public synchronized static BaseApplication getInstance() {
        if (null == instance) {
            instance = new BaseApplication();
        }
        return instance;
    }

    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Intent intent = new Intent(this, WebSocketService.class);
        startService(intent);
    }


    public static Context getContext() {
        return context;
    }

}
