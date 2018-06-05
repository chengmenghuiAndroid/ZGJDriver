package com.lty.zgj.driver.net;

/**
 * Created by cheng on 2017/12/5.
 */

public class Api extends ObjectLoader{

    private static LanTaiYuanService lantaiyuanservice;

    public static LanTaiYuanService getGankService() {
        if (lantaiyuanservice == null) {
            synchronized (Api.class) {
                if (lantaiyuanservice == null) {
                    lantaiyuanservice = RetrofitServiceManager.getInstance().create(LanTaiYuanService.class);
                }
            }
        }
        return lantaiyuanservice;
    }
}
