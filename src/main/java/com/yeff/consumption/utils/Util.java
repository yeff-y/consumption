package com.yeff.consumption.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.UUID;

public class Util {
    private static final Logger logger = LoggerFactory.getLogger(Util.class);

    public static String getUUIDStr() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
