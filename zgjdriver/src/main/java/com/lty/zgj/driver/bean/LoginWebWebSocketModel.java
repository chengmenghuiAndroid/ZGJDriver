package com.lty.zgj.driver.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/6/13.
 */

public class LoginWebWebSocketModel implements Serializable{

    /**
     * driverId : 9
     * name : cmh
     * token : d6d0b25189f94e16be584ba7782d9df7
     */

    private int driverId;
    private String name;
    private String token;

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
