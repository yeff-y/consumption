package com.yeff.consumption.exception;

public enum ConsumptionErrorCode {

<<<<<<< HEAD
    UNKNOWN_ERROR(100001001L, "unknown error"),
    OKHTTP_ERROR(100001002L,"okhttp error,details --> {0}"),
    NAME_EMPTY(100001003L,"name is empty");
=======
    UNKNOWN_ERROR(111001001L, "unknown error"),
    NO_SUCH_NAME(111001002L,"no such records about name : {0}"),
    ADD_RECORD_ERROR(111001003L,"add consumption record error,message --> {0}"),
    NO_RECORD_BEFORE_THIS_DATE(111001004L,"no records before this date: {0}"),
    STRING_DATE_TYPE_PARSE_ERROR(111001005L,"string date type parse error,date: {}");
>>>>>>> 00a0e33eac8cd7debc50cf94339858521bd4c6c0

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
