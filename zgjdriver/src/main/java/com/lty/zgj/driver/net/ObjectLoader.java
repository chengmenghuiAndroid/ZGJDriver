package com.lty.zgj.driver.net;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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


    /**
     * 用户登录
     * @param logResult
     * @param param
     */
    public void getLoginData(Subscriber<LoginModel> logResult, JSONObject param) {

        Observable observable = Api.getGankService().login(param)
                .map(new HttpResultFunc<LoginModel>());
        toSubscribe(observable, logResult);
    }


    /**
     * 历史行程
     * @param historicalJourneyModel
     * @param Id
     */
    public void getTripDate(Subscriber<HistoricalJourneyModel> historicalJourneyModel , int Id, int page){
        Observable<HistoricalJourneyModel> journeyModelObservable = Api.getGankService().getTrip(Id, page)
                .map(new HttpResultFunc<HistoricalJourneyModel>());

        toSubscribe(journeyModelObservable, historicalJourneyModel);
    }

    public void getTripDetailData(Subscriber<HistoricalJourneyDetailModel> historicalJourneyDetailModel , String Id){
        Observable<HistoricalJourneyDetailModel> journeyModelObservable = Api.getGankService().getTripDetail(Id)
                .map(new HttpResultFunc<HistoricalJourneyDetailModel>());

        toSubscribe(journeyModelObservable, historicalJourneyDetailModel);
    }

    /**
     * 发车
     * @param departModelSubscriber
     * @param Id
     */
    public void getDepartModelData(Subscriber<DepartModel> departModelSubscriber, int Id){
        Observable<DepartModel> modelObservable = Api.getGankService().getDepartModel(Id).map(new HttpResultFunc<DepartModel>());
        toSubscribe(modelObservable, departModelSubscriber);
    }

    /**
     * 待出行
     * @param tripListModelSubscriber
     * @param Id
     */
    public void getTripListModelData(Subscriber<TripListModel> tripListModelSubscriber, int Id){
        Observable<TripListModel> modelObservable = Api.getGankService().getTripListModel(Id).map(new HttpResultFunc<TripListModel>());
        toSubscribe(modelObservable, tripListModelSubscriber);
    }

    /**
     * 发车
     * @param startBustModelSubscriber
     * @param scheduleId
     */
    public void getStartBuslData(Subscriber<StartBustModel> startBustModelSubscriber, String scheduleId){
        Observable<StartBustModel> modelObservable = Api.getGankService().startBus(scheduleId).map(new HttpResultFunc<StartBustModel>());
        toSubscribe(modelObservable, startBustModelSubscriber);
    }



    /**
     * 发车完成
     * @param endBusModelSubscriber
     * @param scheduleId
     */
    public void getEndBusModelData(Subscriber<EndBusModel> endBusModelSubscriber, String scheduleId){
        Observable<EndBusModel> modelObservable = Api.getGankService().endBus(scheduleId).map(new HttpResultFunc<EndBusModel>());
        toSubscribe(modelObservable, endBusModelSubscriber);
    }


    public void getDriverNoticeInfoData(Subscriber<DriverNoticeInfoModel> driverNoticeInfoModel, int driverId, int page){
        Observable<DriverNoticeInfoModel> modelObservable = Api.getGankService().driverNoticeInfo(driverId, page).map(new
                HttpResultFunc<DriverNoticeInfoModel>());
        toSubscribe(modelObservable, driverNoticeInfoModel);
    }

    public void getDriverNoticeInfoDetailData(Subscriber<DriverNoticeInfoDetailModel> driverNoticeInfoModel, int noticeId){
        Observable<DriverNoticeInfoDetailModel> modelObservable = Api.getGankService().driverNoticeInfoDetail(noticeId).map(new HttpResultFunc<DriverNoticeInfoDetailModel>());
        toSubscribe(modelObservable, driverNoticeInfoModel);
    }



}
