package com.lty.zgj.driver.bean;

/**
 * Created by Administrator on 2018/6/26.
 */

public class DriverNoticeInfoDetailModel {


    /**
     * id : 16
     * type : 1
     * title : 坐公交
     * content : 1255586
     * driverId : 0
     * createTime : 201807031124
     */

    private int id;
    private String type;
    private String title;
    private String content;
    private int driverId;
    private String createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
