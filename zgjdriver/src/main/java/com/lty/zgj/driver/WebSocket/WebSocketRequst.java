package com.lty.zgj.driver.WebSocket;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/8.
 */

public class WebSocketRequst {

    /**
     * 获取全局的WebSocketManager
     */
    public WebSocketRequst() {
    }

    public static WebSocketRequst getInstance(Context context){
        return WebSocketManagerInstance.instance;
    }
    private static  class WebSocketManagerInstance{
        private static WebSocketRequst instance=new WebSocketRequst();
    }




    /**
     * 司机登录
     * @param account 账户
     * @param pwd 密码
     * @return
     */
    public Map<String, Object> userLogin(String account, String pwd){
        Map<String, Object> params = new HashMap<>();
        params.put("account", account);
        params.put("pwd", pwd);
        return params;
    }

    /**
     * gps轨迹信息上传
     * @param scheduleId 班次id
     * @param routeId 线路id
     * @param longitude 经度（精确到小数点后6位）
     * @param latitude 纬度（精确到小数点后6位）
     * @param cityCode 城市id
     * @param busId 车辆id
     * @param driverId 司机id
     * @param gpsTime gps时间(以秒为单位的时间戳)
     * @return
     */
    public Map<String, Object> gpsUpload(int scheduleId, int routeId, double longitude, double latitude,
                                         String cityCode, int busId, int driverId, long gpsTime){
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleId", scheduleId);
        params.put("routeId", routeId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("cityCode", cityCode);
        params.put("busId", busId);
        params.put("driverId", driverId);
        params.put("gpsTime", gpsTime);
        return params;
    }


    /**
     *
     * @param scheduleId 班次id
     * @param routeId 线路id
     * @param stationId 站点id
     * @param stationName 站点名称
     * @param stationTime 站点时间
     * @param scheduleDate 班次时间
     * @param busId 车辆id
     * @param scheduleTripId 行程id
     * @return
     */
    public Map<String, Object> travelPathUpload(int scheduleTripId, int routeId, int stationId, String stationName,
                                                long stationTime, long scheduleDate, int busId){
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleTripId", scheduleTripId);
        params.put("routeId", routeId);
        params.put("stationId", stationId);
        params.put("stationName", stationName);
        params.put("scheduleDate", scheduleDate);
        params.put("busId", busId);
        return params;
    }


}
