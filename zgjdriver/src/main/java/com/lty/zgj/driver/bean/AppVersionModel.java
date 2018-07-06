package com.lty.zgj.driver.bean;

/**
 * Created by Administrator on 2018/7/6.
 */

public class AppVersionModel {


    /**
     * id : 3
     * versionId : V1.0.1
     * type : 0
     * versionUrl :
     * cityCode : 420100
     * createTime : 20180705113000
     * forceToUpdate : false
     * contentDesc : <p>
     1.水电费水电费是的防辐射
     </p>
     <p>
     2.收费的斯蒂芬斯蒂芬s
     </p>
     <p>
     3.水电费水电费是的hfghfgh
     </p>
     * softType : 0
     */

    private int id;
    private String versionId;
    private int type;
    private String versionUrl;
    private String cityCode;
    private String createTime;
    private boolean forceToUpdate;
    private String contentDesc;
    private int softType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean isForceToUpdate() {
        return forceToUpdate;
    }

    public void setForceToUpdate(boolean forceToUpdate) {
        this.forceToUpdate = forceToUpdate;
    }

    public String getContentDesc() {
        return contentDesc;
    }

    public void setContentDesc(String contentDesc) {
        this.contentDesc = contentDesc;
    }

    public int getSoftType() {
        return softType;
    }

    public void setSoftType(int softType) {
        this.softType = softType;
    }
}
