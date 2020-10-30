package com.yeff.consumption.exception;

public enum ConsumptionErrorCode {

    UNKNOWN_ERROR(100001001L, "unknown error"),
    OKHTTP_ERROR(100001002L,"okhttp error,details --> {0}"),
    NAME_EMPTY(100001003L,"name is empty");

    long code;
    String message;

    ConsumptionErrorCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
