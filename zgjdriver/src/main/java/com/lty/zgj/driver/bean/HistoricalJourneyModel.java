package com.lty.zgj.driver.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/21.
 */

public class HistoricalJourneyModel {

    private int total; //总数据条数
    private int size;  //每页最大数据条数
    private int pages; //总页数
    private int current;  //当前页数
    private List<RecordsBean> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * date : 20180620
         * endName : 资本大厦
         * busNum : xxx
         * startTime : 0710
         * id : 1
         * endTime : 0910
         * tripNo : 5
         * routeName : 金融港-光谷
         * startName : 汇金中心
         * status : 1
         */

        private String date;        //时间
        private String endName;     //终点
        private String busNum;      //车牌号
        private String startTime;   //开始时间
        private String id;          //班次 id
        private String endTime;     //结束时间
        private int tripNo;         //行程编号
        private String routeName;   //线路名称
        private String startName;   //起点站名称
        private int status;         //行程状态 0待进行 1已完成
        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getEndName() {
            return endName;
        }

        public void setEndName(String endName) {
            this.endName = endName;
        }

        public String getBusNum() {
            return busNum;
        }

        public void setBusNum(String busNum) {
            this.busNum = busNum;
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

        public String getStartName() {
            return startName;
        }

        public void setStartName(String startName) {
            this.startName = startName;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
