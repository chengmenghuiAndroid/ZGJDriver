package com.lty.zgj.driver.net;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lty.zgj.driver.bean.HttpResult;
import com.lty.zgj.driver.bean.LoginModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cheng on 2017/12/6.
 */

public class ObjectLoader {


    private static final String THIS_FILE = "ObjectLoader";
    private Map<String, String> params = new HashMap();

    /**
     * @param observable
     * @param <T>
     * @return
     */

    protected <T> Observable<T> observe(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }


    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final ObjectLoader INSTANCE = new ObjectLoader();
    }

    //获取单例
    public static ObjectLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.e(THIS_FILE, "---------" + new Gson().toJson(httpResult.getData()));
            return httpResult.getData();
        }
    }


    public void getLoginData(Subscriber<LoginModel> logResult, JSONObject param) {

        Observable observable = Api.getGankService().login(param)
                .map(new HttpResultFunc<LoginModel>());
        toSubscribe(observable, logResult);
    }
}
