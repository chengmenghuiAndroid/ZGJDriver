package com.lty.zgj.driver;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.lty.zgj.driver.net.UrlKit;
import com.lty.zgj.driver.websocketdemo.WebSocketService;

import me.jessyan.retrofiturlmanager.RetrofitUrlManager;


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

        //将每个 BaseUrl 进行初始化,运行时可以随时改变 DOMAIN_NAME 对应的值,从而达到切换 BaseUrl 的效果
        RetrofitUrlManager.getInstance().putDomain(UrlKit.URL_YD_NAME, UrlKit.URL_YD);
        RetrofitUrlManager.getInstance().putDomain(UrlKit.URL__CQJ_NAME, UrlKit.URL__CQJ);
    }


    public static Context getContext() {
        return context;
    }

}
