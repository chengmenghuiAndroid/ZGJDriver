package com.lty.zgj.driver.net;


import com.alibaba.fastjson.JSONObject;
import com.lty.zgj.driver.bean.HttpResult;
import com.lty.zgj.driver.bean.LoginModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by cheng on 2017/12/5.
 */

public interface ZGJDService {

//    @Headers({"Content-type: application/json"})
    @POST("driver/login")
    Observable<HttpResult<LoginModel>> login(@Body JSONObject param);

}

