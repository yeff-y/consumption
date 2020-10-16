package com.yeff.consumption.exception.handler;

import com.yeff.consumption.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) {
        // 获取code, message
        String code = ExceptionUtils.getCode(ex);
        String message = ExceptionUtils.getMessage(ex);
        logger.error(MessageFormat.format("Exception, code: {0}, message: {1}", code, message), ex);

        // 页面返回
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (ModelAndView.class.equals(handlerMethod.getMethod().getReturnType())) {
            ModelMap model = new ModelMap();
            model.addAttribute("code", code);
            model.addAttribute("message", message);
            return new ModelAndView("error_500", model);
        }

        // Json返回
        Map<String, Object> attributes = new LinkedHashMap<String, Object>();
        attributes.put("code", code);
        attributes.put("message", message);
        MappingJackson2JsonView view = new MappingJackson2JsonView() {

            protected Object filterModel(Map<String, Object> model) {
                return model;
            }
        };
        view.setAttributesMap(attributes);
        return new ModelAndView(view);
    }
}
