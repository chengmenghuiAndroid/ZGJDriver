package com.lty.zgj.driver.net;


import com.alibaba.fastjson.JSONObject;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.bean.HistoricalJourneyModel;
import com.lty.zgj.driver.bean.HttpResult;
import com.lty.zgj.driver.bean.LoginModel;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cheng on 2017/12/5.
 */

public interface ZGJDService {

//    @Headers({"Content-type: application/json"})
    @POST("login")
    Observable<HttpResult<LoginModel>> login(@Body JSONObject param);

    /**
     * 历史行程
     * @param Id 司机Id
     * @return
     */
    @GET("trip/{Id}/{page}")
    Observable<HttpResult<HistoricalJourneyModel>> getTrip(@Path("Id") int Id, @Path("page") int page);





    //192.168.2.31:8090/driver/trip/info/1/5/11   5行程编号 11线路id
    /**
     * @param Id
     * @param tripNo 行程编号
     * @param routeId 线路id
     * @return
     */
    @GET("trip/info/{Id}/{tripNo}/{routeId}")
    Observable<HttpResult<HistoricalJourneyDetailModel>> getTripDetail(@Path("Id") int Id,
                                                                       @Path("tripNo") int tripNo,
                                                                       @Path("routeId") int routeId);

}

