package com.lty.zgj.driver.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/6/26.
 */

public class DriverNoticeInfoModel {


    /**
     * total : 2
     * size : 20
     * pages : 1
     * current : 1
     * records : [{"id":1,"type":"1","title":"坐公交官方","content":"喜喜喜喜喜喜喜喜喜喜喜喜喜喜","driverId":0,"createTime":"201806251515"},{"id":2,"type":"2","title":"邯郸公交","content":"我是大英雄","driverId":0,"createTime":"201806251515"}]
     */

    private int total;
    private int size;
    private int pages;
    private int current;
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
         * id : 1
         * type : 1
         * title : 坐公交官方
         * content : 喜喜喜喜喜喜喜喜喜喜喜喜喜喜
         * driverId : 0
         * createTime : 201806251515
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
}
