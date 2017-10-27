package com.huyingbao.rxflux2.model;


import java.io.IOException;

/**
 * 自定义HttpException
 * Created by liujunfeng on 2016/11/26.
 */
public class RxHttpException extends IOException {
    private final int mCode;
    private final String mMessage;


    public RxHttpException(int code, String message) {
        super("HTTP " + code + " " + message);
        this.mCode = code;
        this.mMessage = message;
    }

    /**
     * HTTP status mCode.
     */
    public int code() {
        return mCode;
    }

    /**
     * HTTP status mMessage.
     */
    public String message() {
        return mMessage;
    }
}
