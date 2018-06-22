package com.lty.zgj.driver.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/21.
 *
 */

public class HistoricalJourneyDetailModel {


    /**
     * date : 20180620
     * relStations : [{"stationTime":"20180614081920","stationName":"光谷汇金中心"},{"stationTime":"20180614091920","stationName":"现代森林小镇"}]
     * relAssits : [{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666},{"lon":30.412423,"lat":114.368666}]
     * busNum : xxx
     * stations : [{"stationNo":1,"planTime":"0700","stationName":"现代森林小镇2122","lon":114.37721,"id":4,"lat":30.641578,"stationId":11},{"stationNo":2,"planTime":"0730","stationName":"青山区红卫路街道东兴天地D区","lon":114.370264,"id":5,"lat":30.630165,"stationId":11},{"stationNo":3,"planTime":"0810","stationName":"青山区红卫路街道二环线奥山世纪城·V公馆","lon":114.358591,"id":6,"lat":30.617757,"stationId":27},{"stationNo":4,"planTime":"0815","stationName":"武昌区杨园街街道武汉理工大学余家头校区武汉理工大学(余家头校区)","lon":114.353784,"id":7,"lat":30.606529,"stationId":28},{"stationNo":5,"planTime":"0850","stationName":"武昌区杨园街街道武昌区杨园街柴东社区","lon":114.319452,"id":8,"lat":30.569289,"stationId":29}]
     * tripNo : 5
     * routeName : 金融港-光谷
     * endName : 资本大厦
     * assits : [{"lon":114.2227,"lat":30.659112},{"lon":96.4578,"lat":31.457812}]
     * startTime : 0710
     * id : 1
     * endTime : 0910
     * nums : 15
     * startName : 汇金中心
     */

    private String date;
    private String busNum;
    private int tripNo;
    private String routeName;
    private String endName;
    private String startTime;
    private String id;
    private String endTime;
    private int nums;
    private String startName;
    private List<RelStationsBean> relStations;
    private List<RelAssitsBean> relAssits;
    private List<StationsBean> stations;
    private List<AssitsBean> assits;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBusNum() {
        return busNum;
    }

    public void setBusNum(String busNum) {
        this.busNum = busNum;
    }

    public int getTripNo() {
        return tripNo;
    }

    public void setTripNo(int tripNo) {
        this.tripNo = tripNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getStartName() {
        return startName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public List<RelStationsBean> getRelStations() {
        return relStations;
    }

    public void setRelStations(List<RelStationsBean> relStations) {
        this.relStations = relStations;
    }

    public List<RelAssitsBean> getRelAssits() {
        return relAssits;
    }

    public void setRelAssits(List<RelAssitsBean> relAssits) {
        this.relAssits = relAssits;
    }

    public List<StationsBean> getStations() {
        return stations;
    }

    public void setStations(List<StationsBean> stations) {
        this.stations = stations;
    }

    public List<AssitsBean> getAssits() {
        return assits;
    }

    public void setAssits(List<AssitsBean> assits) {
        this.assits = assits;
    }

    public static class RelStationsBean {
        /**
         * stationTime : 20180614081920
         * stationName : 光谷汇金中心
         */

        private String stationTime;
        private String stationName;

        public String getStationTime() {
            return stationTime;
        }

        public void setStationTime(String stationTime) {
            this.stationTime = stationTime;
        }

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }
    }

    public static class RelAssitsBean {
        /**
         * lon : 30.412423
         * lat : 114.368666
         */

        private double lon;
        private double lat;

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
    }

    public static class StationsBean {
        /**
         * stationNo : 1
         * planTime : 0700
         * stationName : 现代森林小镇2122
         * lon : 114.37721
         * id : 4
         * lat : 30.641578
         * stationId : 11
         */

        private int stationNo;
        private String planTime;
        private String stationName;
        private double lon;
        private int id;
        private double lat;
        private int stationId;

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

        public String getStationName() {
            return stationName;
        }

        public void setStationName(String stationName) {
            this.stationName = stationName;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public int getStationId() {
            return stationId;
        }

        public void setStationId(int stationId) {
            this.stationId = stationId;
        }
    }

    public static class AssitsBean {
        /**
         * lon : 114.2227
         * lat : 30.659112
         */

        private double lon;
        private double lat;

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
    }
}
