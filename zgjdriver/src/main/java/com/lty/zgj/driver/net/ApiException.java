package com.lty.zgj.driver.net;


/**
 * Created by liukun on 16/3/10.
 */
public class ApiException extends RuntimeException {

    public ApiException(int resultCode, String errorMsg) {
        this(getApiExceptionMessage(resultCode, errorMsg));
    }

    public ApiException(String errorMessage) {
        super(errorMessage);
    }


    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code, String errorMsg) {
        String message = "";
        switch (code) {
            default:
                message = errorMsg;
        }
        return message;
    }


}

