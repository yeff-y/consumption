package com.yeff.consumption.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public final class RequestUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletRequest getRequest() {
        return getServletRequestAttributes().getRequest();
    }

    public static String getParameter(String key) {
        return RequestUtil.getRequest().getParameter(key);
    }

    public static <T> T getAttribute(String key, Class<T> c) {
        Object object = getServletRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_REQUEST);
        if (object != null && c.isAssignableFrom(object.getClass())) {
            return (T)object;
        }
        return null;
    }

    public static String getAttribute(String key) {
        Object object = getServletRequestAttributes().getAttribute(key, RequestAttributes.SCOPE_REQUEST);
        if (object != null) {
            return String.valueOf(object);
        }
        return null;
    }

    public static void setAttribute(String key, Object value) {
        getServletRequestAttributes().setAttribute(key, value, RequestAttributes.SCOPE_REQUEST);
    }

    public static void removeAttribute(String key) {
        getServletRequestAttributes().removeAttribute(key, RequestAttributes.SCOPE_REQUEST);
    }

}
