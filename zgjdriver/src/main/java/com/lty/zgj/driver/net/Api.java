package com.lty.zgj.driver.net;

/**
 * Created by cheng on 2017/12/5.
 */

public class Api extends ObjectLoader{

    private static ZGJDService lantaiyuanservice;

    public static ZGJDService getGankService() {
        if (lantaiyuanservice == null) {
            synchronized (Api.class) {
                if (lantaiyuanservice == null) {
                    lantaiyuanservice = RetrofitServiceManager.getInstance().create(ZGJDService.class);
                }
            }
        }
        return lantaiyuanservice;
    }
}
