package com.lty.zgj.driver.net;



/**
 * Created by wanglei on 2016/12/9.
 */

public class UrlKit {

    public static final String LAY_BASE_URL = "http://116.205.13.132:18090/driver/"; //周永波  websorket Url


        public static final String URL_YD = "http://116.205.13.132:28090";//外网正式ip
//    public static final String URL_YD = "http://192.168.2.226:8090"; //饶达本地
//    public static final String URL_YD = "http://116.205.13.132:8092"; //饶达
//    public static final String URL_YD = "http://10.1.254.173:8090"; //测试
    public static final String URL_YD_NAME = "url_yd_name";

    public static final String URL__CQJ = "http://192.168.2.53:8090"; //昌奇晶
    public static final String URL__CQJ_NAME = "url__cqj_name";

    public static final String URL__CJW = "http://192.168.2.122:8090"; //陈家威
    public static final String URL__CJW_NAME = "url__cJw_name";

    public static String getUrl(String action) {
        return new StringBuilder(LAY_BASE_URL).append(action).toString();
    }
}