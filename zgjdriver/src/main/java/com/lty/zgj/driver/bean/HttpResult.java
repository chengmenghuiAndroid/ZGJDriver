package com.lty.zgj.driver.bean;


/**
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {

    private int returntCode;//	1001

    // private String resultMsg;//	API服务正常

    private String msg;//	用户名或者密码错误
    //用来模仿Data
    private T data;
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


    public int getResultCode() {
        return returntCode;
    }

    public void setResultCode(int resultCode) {
        this.returntCode = resultCode;
    }


    /**
     * API是否请求失败
     *
     * @return 失败返回true, 成功返回false
     */
    public boolean isCodeInvalid() {
//        return resultCode != Constants.CODE_SUCCESS_IN_SERVICE;
        return Boolean.parseBoolean(null);
    }


}
