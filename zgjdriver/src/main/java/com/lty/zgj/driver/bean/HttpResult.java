package com.lty.zgj.driver.bean;


import com.lty.zgj.driver.core.config.Constant;

/**
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {

    private int code;//	101

    // private String resultMsg;//	API服务正常

    private String message;//	用户名或者密码错误
    //用来模仿Data
    private T data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int resultCode) {
        this.code = resultCode;
    }


    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
        return code != Constant.CODE_SUCCESS_IN_SERVICE;
    }


}
