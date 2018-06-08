package com.lty.zgj.driver.WebSocket;

/**
 * 相应数据实体基类，不同的项目数据格式可能会有所不同，
 * 根据接口自己调整，大体上类似
 *
 */

public class CommonResponse<T> {

    private String msg; //提示信息 成功有成功的提示 失败有失败的提示
    private T data;     //请求返回的数据
    private String code; //101 成功  102失败

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
