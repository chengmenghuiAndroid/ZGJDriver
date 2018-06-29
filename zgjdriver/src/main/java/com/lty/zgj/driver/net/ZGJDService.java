package com.lty.zgj.driver.net;


import com.alibaba.fastjson.JSONObject;
import com.lty.zgj.driver.bean.DepartModel;
import com.lty.zgj.driver.bean.DriverNoticeInfoDetailModel;
import com.lty.zgj.driver.bean.DriverNoticeInfoModel;
import com.lty.zgj.driver.bean.EndBusModel;
import com.lty.zgj.driver.bean.HistoricalJourneyDetailModel;
import com.lty.zgj.driver.bean.HistoricalJourneyModel;
import com.lty.zgj.driver.bean.HttpResult;
import com.lty.zgj.driver.bean.LoginModel;
import com.lty.zgj.driver.bean.StartBustModel;
import com.lty.zgj.driver.bean.TripListModel;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

/**
 * Created by cheng on 2017/12/5.
 */

public interface ZGJDService {

//    @Headers({"Content-type: application/json"})
    @POST("login")
    Observable<HttpResult<LoginModel>> login(@Body JSONObject param);

    /**
     * 历史行程
     * @param driverId 司机Id
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @GET("/trip/{driverId}/{page}")
    Observable<HttpResult<HistoricalJourneyModel>> getTrip(@Path("driverId") int driverId, @Path("page") int page);


    //192.168.2.31:8090/driver/trip/info/1/5/11   5行程编号 11线路id
    /**
     * @param scheduleId
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @GET("/trip/info/{scheduleId}")
    Observable<HttpResult<HistoricalJourneyDetailModel>> getTripDetail(@Path("scheduleId") String scheduleId);


    /**
     * 获取发车数据
     * @param driverId
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @POST("/trip/today/{driverId}")
    Observable<HttpResult<DepartModel>> getDepartModel(@Path("driverId") int driverId);


    /**
     * 待发车详情 接口
     * @param itemId
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @POST("/trip/detail/{itemId}")
    Observable<HttpResult<DepartModel>> getDepartDetailModel(@Path("itemId") String itemId);


    /**
     * 待发车
     * @param driverId
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @POST("/trip/getTripList/{driverId}")
    Observable<HttpResult<TripListModel>> getTripListModel(@Path("driverId") int driverId);

    /**
     *发车
     * @param scheduleId 班次id
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @POST("/trip/startBus/{scheduleId}")
    Observable<HttpResult<StartBustModel>> startBus(@Path("scheduleId") String scheduleId);

    /**
     *发车完成
     * @param scheduleId 班次id
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL_YD_NAME})
    @POST("/trip/endBus/{scheduleId}")
    Observable<HttpResult<EndBusModel>> endBus(@Path("scheduleId") String scheduleId);

    /**
     * 消息通知
     * @param driverId 司机id
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL__CQJ_NAME})
    @POST("/driverNoticeInfo/search")
    Observable<HttpResult<DriverNoticeInfoModel>> driverNoticeInfo(@Query("driverId") int driverId,
                                                                   @Query("page") int page);

    /**
     * 消息详情
     * @param noticeId 条目id
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + UrlKit.URL__CQJ_NAME})
    @GET("/driverNoticeInfo/getNotice/{noticeId}")
    Observable<HttpResult<DriverNoticeInfoDetailModel>> driverNoticeInfoDetail(@Path("noticeId") int noticeId);



}

