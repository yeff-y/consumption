package com.yeff.consumption.exception;

public class ConsumptionException extends RuntimeException{
    private String message;
    private ConsumptionErrorCode code;

    public ConsumptionException(ConsumptionErrorCode code) {
        super();
        this.code = code;
    }

    public ConsumptionException(ConsumptionErrorCode code, Throwable t) {
        super(t);
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ConsumptionErrorCode getCode() {
        return code;
    }
}
