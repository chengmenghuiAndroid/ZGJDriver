package com.lty.zgj.driver.bean;

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
     * @return
     */
    public Map<String, Object> gpsUpload(int scheduleId, int routeId, double longitude, double latitude,
                                         String cityCode, int busId){
        Map<String, Object> params = new HashMap<>();
        params.put("scheduleId", scheduleId);
        params.put("routeId", routeId);
        params.put("longitude", longitude);
        params.put("latitude", latitude);
        params.put("cityCode", cityCode);
        params.put("busId", busId);
        return params;
    }



}
