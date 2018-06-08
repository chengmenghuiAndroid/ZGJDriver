package com.lty.zgj.driver.core.tool;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chen on 2016/8/4.
 */

public class HttpUtils {

    public static void get(String url, StringCallback callback) {
        OkHttpUtils
                .get()
                .url(url)
                .addHeader("Accept-Encoding", "")
                .build()
                .execute(callback);
    }

    public static void post(String url, String tag, HashMap<String, String> map, StringCallback callback) {
        PostFormBuilder post = OkHttpUtils.post();
        post.url(url);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            post.addParams(entry.getKey(),entry.getValue());
        }
        post.tag(tag);
        RequestCall build = post.build();
        build.execute(callback);
    }
    public static void cancelTag(String tag){
        OkHttpUtils.getInstance().cancelTag(tag);
    }

}
