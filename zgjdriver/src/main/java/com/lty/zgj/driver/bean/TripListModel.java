package com.lty.zgj.driver.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/23.
 */

public class TripListModel {

    private List<NoStartListBean> noStartList;
    private List<TodayListBean> todayList;

    public List<NoStartListBean> getNoStartList() {
        return noStartList;
    }

    public void setNoStartList(List<NoStartListBean> noStartList) {
        this.noStartList = noStartList;
    }

    public List<TodayListBean> getTodayList() {
        return todayList;
    }

    public void setTodayList(List<TodayListBean> todayList) {
        this.todayList = todayList;
    }

    public static class NoStartListBean {
        /**
         * id : 2018062732260700
         * busPlateNumber : 鄂A00165
         * startName : 光谷广场
         * endName : 光谷天地
         * startTime : 0700
         * endTime : 0745
         * list : null
         * date : 20180627
         */

        private String id;
        private String busPlateNumber;
        private String startName;
        private String endName;
        private String startTime;
        private String endTime;
        private Object list;
        private String date;

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

        public Object getList() {
            return list;
        }

        public void setList(Object list) {
            this.list = list;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

    public static class TodayListBean {
        /**
         * id : 2018062332250700
         * busPlateNumber : 鄂A00158
         * startName : 光谷广场
         * endName : 光谷天地
         * startTime : 0700
         * endTime : 0745
         * list : [{"stationId":51,"stationName":"光谷广场","stationNo":1,"planTime":"0700","lon":114.397118,"lat":30.505824,"peopleCount":0},{"stationId":50,"stationName":"杨家湾地铁站","stationNo":2,"planTime":"0715","lon":114.382699,"lat":30.505048,"peopleCount":0},{"stationId":52,"stationName":"中南民族大学","stationNo":3,"planTime":"0720","lon":114.392955,"lat":30.487113,"peopleCount":0},{"stationId":53,"stationName":"中南财经政法大学","stationNo":4,"planTime":"0732","lon":114.386904,"lat":30.473244,"peopleCount":0},{"stationId":54,"stationName":"光谷天地","stationNo":5,"planTime":"0745","lon":114.410079,"lat":30.476203,"peopleCount":0}]
         * date : 20180623
         */

        private String id;
        private String busPlateNumber;
        private String startName;
        private String endName;
        private String startTime;
        private String endTime;
        private String date;
        private List<ListBean> list;

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

        public static class ListBean {
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
    }
}
