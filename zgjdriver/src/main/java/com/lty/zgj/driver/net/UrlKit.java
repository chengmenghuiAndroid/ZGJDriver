package com.lty.zgj.driver.net;



/**
 * Created by wanglei on 2016/12/9.
 */

public class UrlKit {

//    public static final String LAY_BASE_URL = "http://10.1.254.172:8089/driver/"; //周永波
    public static final String LAY_BASE_URL = "http://192.168.2.31:8090/driver/";

    public static String getUrl(String action) {
        return new StringBuilder(LAY_BASE_URL).append(action).toString();
    }
}