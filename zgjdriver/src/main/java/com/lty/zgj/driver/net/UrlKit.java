package com.lty.zgj.driver.net;



/**
 * Created by wanglei on 2016/12/9.
 */

public class UrlKit {

    public static final String LAY_BASE_URL = "http://10.1.254.172:8089/";

    public static String getUrl(String action) {
        return new StringBuilder(LAY_BASE_URL).append(action).toString();
    }
}