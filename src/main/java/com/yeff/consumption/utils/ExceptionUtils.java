package com.yeff.consumption.utils;

import com.yeff.consumption.exception.handler.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtils.class);

    public ExceptionUtils() {
    }

    public static String getCode(Exception exception) {
        if (exception instanceof BizException) {
            BizException e = (BizException)exception;
            return e.getErrorCode();
        } else {
            return "system.error";
        }
    }

    public static String getMessage(Exception exception) {
        return exception.getMessage() == null ? exception.getClass().getName() : exception.getMessage();
    }
}
