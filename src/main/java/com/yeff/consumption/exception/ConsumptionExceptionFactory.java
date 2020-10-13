package com.yeff.consumption.exception;

import java.text.MessageFormat;

public class ConsumptionExceptionFactory {
    public static ConsumptionException create(ConsumptionErrorCode code, Object... args) {
        return create(code, null, args);
    }

    public static ConsumptionException create(ConsumptionErrorCode code, Throwable t, Object... args) {
        ConsumptionException exception = new ConsumptionException(code, t);
        String message = code.message;
        if (args != null && args.length > 0) {
            message = MessageFormat.format(message, args);
        }
        exception.setMessage(message);
        return exception;
    }
}
