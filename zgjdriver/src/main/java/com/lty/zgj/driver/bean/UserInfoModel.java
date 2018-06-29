package com.lty.zgj.driver.bean;

/**
 * Created by Administrator on 2018/6/29.
 */

public class UserInfoModel {


    /**
     * account :
     * driverId : 115
     * name :
     * token : 18d71216b5574311a98cf485ab2ce638
     */

    private String account;
    private int driverId;
    private String name;
    private String token;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
