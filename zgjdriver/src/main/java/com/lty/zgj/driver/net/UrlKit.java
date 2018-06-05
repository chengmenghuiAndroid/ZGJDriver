package com.lty.zgj.driver.net;



/**
 * Created by wanglei on 2016/12/9.
 */

public class UrlKit {

    //        public static final String LAY_BASE_URL = "http://192.168.2.76:9999/api/";  //杨晓亮
//    public static final String LAY_BASE_URL = "http://192.168.2.78:10001/api/";  //杜鑫耀
//    public static final String LAY_BASE_URL = "http://192.168.2.233:9999/api/";  //成飞
//    public static final String LAY_BASE_URL = "http://59.173.242.138:13002/api/";  //Eline-后台:
//    public static final String LAY_BASE_URL = "http://10.1.254.118:9999/api/";  //测试
//    public static final String LAY_BASE_URL = "http://10.1.254.118:12003/eline/api/";
//    public static final String LAY_BASE_URL = "http://59.173.242.138:12003/eline/api/";//开发外网
//    public static final String LAY_BASE_URL = "http://59.173.242.138:22003/eline/api/";//少杰的外网
//    public static final String LAY_BASE_URL = "http://10.1.254.118:12003/eline/api/";
    public static final String LAY_BASE_URL = "http://10.1.254.169:10001/api/";

    public static String getUrl(String action) {
        return new StringBuilder(LAY_BASE_URL).append(action).toString();
    }
}