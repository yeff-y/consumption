package com.yeff.consumption.exception;

public enum ConsumptionErrorCode {
    UNKNOWN_ERROR(111001001L, "unknown error"),
    NO_SUCH_NAME(111001002L,"no such records about name : {0}"),
    ADD_RECORD_ERROR(111001003L,"add consumption record error,message --> {0}"),
    NO_RECORD_BEFORE_THIS_DATE(111001004L,"no records before this date: {0}"),
    STRING_DATE_TYPE_PARSE_ERROR(111001005L,"string date type parse error,date: {}"),
    NO_RECORDS_BETWEEN_PERIOD(111001006L,"no records between this period,date: {0}---{1}");

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
