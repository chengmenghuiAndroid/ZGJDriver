package com.lty.zgj.driver.bean;

import java.io.Serializable;

/**
 * Created by cheng on 2018/6/7.
 */

public class WebSocketResponse implements Serializable{

    /**
     *
     *  # {header : {"msgSn":121,"msgId":257,"msgVn":1.01}
     * lappendBody : {"token":"asbxbasnxjaj","devSn":"adb-samk-sasa","devType":0}
     * body : {}} 515645265415151
     *
     *检验码字符串长度是16位
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

    public static class HeaderBean implements Serializable{
        /**
         * msgSn : 121
         * msgId : 257
         * msgVn : 1.01
         */

        private int msgSn; //报文序列号 时间戳字符串 后面 8位
        private int msgId; //业务消息类型
        private String msgVn; //协议版本号 0x101
        private String appVn; //app版本号

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

        public String getMsgVn() {
            return msgVn;
        }

        public void setMsgVn(String msgVn) {
            this.msgVn = msgVn;
        }

        public String getAppVn() {
            return appVn;
        }

        public void setAppVn(String appVn) {
            this.appVn = appVn;
        }
    }

    public static class LappendBodyBean {
        /**
         * token : asbxbasnxjaj
         * devSn : adb-samk-sasa
         * devType : 0
         */

        private String token; //司机用户token
        private String devSn; //设备唯一 ID
        private int devType;  // 0 Android 1 ios

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
