package com.lty.zgj.driver.WebSocket;

/**
 * 相应数据实体基类，不同的项目数据格式可能会有所不同，
 * 根据接口自己调整，大体上类似
 *
 */

public class CommonResponse<T> {
    /**
     * body : {"code":104,"message":"token 过期"}
     * headerPacket : {"msgId":260,"msgSn":83534313,"msgVn":1.01}
     */

    private BodyBean body;
    private HeaderPacketBean headerPacket;

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public HeaderPacketBean getHeaderPacket() {
        return headerPacket;
    }

    public void setHeaderPacket(HeaderPacketBean headerPacket) {
        this.headerPacket = headerPacket;
    }

    public static class BodyBean {
        /**
         * code : 104
         * message : token 过期
         */

        private int stateCode;
        private String message;
        private String data;     //请求返回的数据

        public int getStateCode() {
            return stateCode;
        }

        public void setStateCode(int stateCode) {
            this.stateCode = stateCode;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class HeaderPacketBean {
        /**
         * msgId : 260
         * msgSn : 83534313
         * msgVn : 1.01
         */

        private int msgId;
        private int msgSn;
        private double msgVn;

        public int getMsgId() {
            return msgId;
        }

        public void setMsgId(int msgId) {
            this.msgId = msgId;
        }

        public int getMsgSn() {
            return msgSn;
        }

        public void setMsgSn(int msgSn) {
            this.msgSn = msgSn;
        }

        public double getMsgVn() {
            return msgVn;
        }

        public void setMsgVn(double msgVn) {
            this.msgVn = msgVn;
        }
    }

}
