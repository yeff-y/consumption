package com.yeff.consumption.bean;

import java.io.Serializable;

public class CResponse<T> implements Serializable {
    private static final long serialVersionUID = 5132407674044022634L;
    /**
     * 0表示成功返回，非零由各应用自行定义
     */
    private int code = 0;
    /**
     * 失败时，错误原因的简单英文描述
     */
    private String message = "success";
    /**
     * 执行成功后返回的结果
     */
    private T data;

    public CResponse() {
    }

    public CResponse(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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

    public static <T> CResponse<T> successT(T t){
        return new CResponse<T>(t);
    }
}
