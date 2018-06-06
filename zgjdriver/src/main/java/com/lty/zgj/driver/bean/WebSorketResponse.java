package com.lty.zgj.driver.bean;

/**
 * Created by cheng on 2018/6/7.
 */

public class WebSorketResponse {


    /**
     * header : {"msgSn":121,"msgId":257,"msgVn":1.01}
     * lappendBody : {"token":"asbxbasnxjaj","devSn":"adb-samk-sasa","devType":0}
     * body : {}
     */

    private HeaderBean header;
    private LappendBodyBean lappendBody;
    private BodyBean body;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public LappendBodyBean getLappendBody() {
        return lappendBody;
    }

    public void setLappendBody(LappendBodyBean lappendBody) {
        this.lappendBody = lappendBody;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeaderBean {
        /**
         * msgSn : 121
         * msgId : 257
         * msgVn : 1.01
         */

        private int msgSn;
        private int msgId;
        private double msgVn;

        public int getMsgSn() {
            return msgSn;
        }

        public void setMsgSn(int msgSn) {
            this.msgSn = msgSn;
        }

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public double getMsgVn() {
            return msgVn;
        }

        public void setMsgVn(double msgVn) {
            this.msgVn = msgVn;
        }
    }

    public static class LappendBodyBean {
        /**
         * token : asbxbasnxjaj
         * devSn : adb-samk-sasa
         * devType : 0
         */

        private String token;
        private String devSn;
        private int devType;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getDevSn() {
            return devSn;
        }

        public void setDevSn(String devSn) {
            this.devSn = devSn;
        }

        public int getDevType() {
            return devType;
        }

        public void setDevType(int devType) {
            this.devType = devType;
        }
    }

    public static class BodyBean {
    }
}
