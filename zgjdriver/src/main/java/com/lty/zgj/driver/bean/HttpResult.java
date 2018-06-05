package com.lty.zgj.driver.bean;


/**
 * Created by liukun on 16/3/5.
 */
public class HttpResult<T> {

    private int resultCode;//	1001
    private String resultMsg;//	API服务正常
    private String errMsg;//	用户名或者密码错误
    //用来模仿Data
    private T model;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {return resultMsg;}

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
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
