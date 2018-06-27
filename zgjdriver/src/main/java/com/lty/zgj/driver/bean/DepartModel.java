package com.lty.zgj.driver.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/6/23.
 */

public class DepartModel implements Serializable{

    /**
     * id : 2018062332250700
     * busPlateNumber : 鄂A00158
     * startName : 光谷广场
     * endName : 光谷天地
     * startTime : 0700
     * endTime : 0745
     * list : [{"stationId":51,"stationName":"光谷广场","stationNo":1,"planTime":"0700","lon":114.397118,"lat":30.505824,"peopleCount":0},{"stationId":50,"stationName":"杨家湾地铁站","stationNo":2,"planTime":"0715","lon":114.382699,"lat":30.505048,"peopleCount":0},{"stationId":52,"stationName":"中南民族大学","stationNo":3,"planTime":"0720","lon":114.392955,"lat":30.487113,"peopleCount":0},{"stationId":53,"stationName":"中南财经政法大学","stationNo":4,"planTime":"0732","lon":114.386904,"lat":30.473244,"peopleCount":0},{"stationId":54,"stationName":"光谷天地","stationNo":5,"planTime":"0745","lon":114.410079,"lat":30.476203,"peopleCount":0}]
     * pointList : [{"id":489,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.379952,"latitude":30.511185,"upStation":null,"downStation":null,"pointOrder":1},{"id":490,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.40038,"latitude":30.504974,"upStation":null,"downStation":null,"pointOrder":2},{"id":491,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.398148,"latitude":30.490626,"upStation":null,"downStation":null,"pointOrder":3},{"id":492,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.393513,"latitude":30.485893,"upStation":null,"downStation":null,"pointOrder":4},{"id":493,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.407933,"latitude":30.47746,"upStation":null,"downStation":null,"pointOrder":5},{"id":494,"routeId":32,"cityCode":"420100","creatTime":"20180623155720","longitude":114.388878,"latitude":30.472874,"upStation":null,"downStation":null,"pointOrder":6}]
     * date : 20180623
     */

    private String id;
    private String busPlateNumber;
    private String startName;
    private String endName;
    private String startTime;
    private String endTime;
    private String date;
    private int busId;
    private int routeId;
    private int status;// 状态  0 未开始  1:进行中  2 已结束
    private List<ListBean> list;
    private List<PointListBean> pointList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusPlateNumber() {
        return busPlateNumber;
    }

    public void setBusPlateNumber(String busPlateNumber) {
        this.busPlateNumber = busPlateNumber;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getEndName() {
        return endName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<PointListBean> getPointList() {
        return pointList;
    }

    public void setPointList(List<PointListBean> pointList) {
        this.pointList = pointList;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public static class ListBean implements Serializable{
        /**
         * stationId : 51
         * stationName : 光谷广场
         * stationNo : 1
         * planTime : 0700
         * lon : 114.397118
         * lat : 30.505824
         * peopleCount : 0
         */

        private int stationId;
        private String stationName;
        private int stationNo;
        private String planTime;
        private double lon;
        private double lat;
        private int peopleCount;
        private boolean uploadPoint;
        private int tripNo;


        public int getTripNo() {
            return tripNo;
        }

        public void setTripNo(int tripNo) {
            this.tripNo = tripNo;
        }


        public boolean isUploadPoint() {
            return uploadPoint;
        }

        public void setUploadPoint(boolean uploadPoint) {
            this.uploadPoint = uploadPoint;
        }



        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public int getStationNo() {
            return stationNo;
        }

        public void setStationNo(int stationNo) {
            this.stationNo = stationNo;
        }

        public String getPlanTime() {
            return planTime;
        }

        public void setPlanTime(String planTime) {
            this.planTime = planTime;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public int getPeopleCount() {
            return peopleCount;
        }

        public void setPeopleCount(int peopleCount) {
            this.peopleCount = peopleCount;
        }
    }

    public static class PointListBean implements Serializable{
        /**
         * id : 489
         * routeId : 32
         * cityCode : 420100
         * creatTime : 20180623155720
         * longitude : 114.379952
         * latitude : 30.511185
         * upStation : null
         * downStation : null
         * pointOrder : 1
         */

        private int id;
        private int routeId;
        private String cityCode;
        private String creatTime;
        private double longitude;
        private double latitude;
        private Object upStation;
        private Object downStation;
        private int pointOrder;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRouteId() {
            return routeId;
        }

        public void setRouteId(int routeId) {
            this.routeId = routeId;
        }

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public Object getUpStation() {
            return upStation;
        }

        public void setUpStation(Object upStation) {
            this.upStation = upStation;
        }

        public Object getDownStation() {
            return downStation;
        }

        public void setDownStation(Object downStation) {
            this.downStation = downStation;
        }

        public int getPointOrder() {
            return pointOrder;
        }

        public void setPointOrder(int pointOrder) {
            this.pointOrder = pointOrder;
        }
    }
}
