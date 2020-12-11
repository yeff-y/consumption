package com.yeff.consumption.exception.handler;

import com.yeff.consumption.exception.ConsumptionException;
import com.yeff.consumption.utils.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.*;

@Component
public class ExceptionHandler implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    private List<ExceptionHandlerProcessor> exceptionHandlerProcessors;

    @PostConstruct
    void _init(){
        _initExceptionHandlerProcessors();
    }

    /**
     * 初始化 exceptionHandlerProcessors ，processor的添加顺序就是processor的添加顺序，
     * 注意若需要添加其他的processor,需要将processor添加到LastExceptionHandlerProcessor之前的任意顺序
     */
    private void _initExceptionHandlerProcessors() {
        exceptionHandlerProcessors = new ArrayList<>();
        exceptionHandlerProcessors.add(new ConsumptionExceptionHandlerProcessor());
        exceptionHandlerProcessors.add(new IllegalArgumentExceptionHandlerProcessor());
        exceptionHandlerProcessors.add(new ControllerExceptionHandlerProcessor());
        exceptionHandlerProcessors.add(new DataAccessExceptionHandlerProcessor());
        exceptionHandlerProcessors.add(new LastExceptionHandlerProcessor());
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception ex) {
        // 获取code, message
        String code = ExceptionUtils.getCode(ex);
        String message = ExceptionUtils.getMessage(ex);
        logger.error(MessageFormat.format("Exception, code: {0}, message: {1}", code, message), ex);

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


    /**
     * 定一个ExceptionHandlerProcessorChain
     */
    public class ExceptionHandlerProcessorChain{
        private List<ExceptionHandlerProcessor> exceptionHandlerProcessors = new ArrayList<>();

        private int index = 0;//index的值表示processor的执行顺序

        public ExceptionHandlerProcessorChain(List<ExceptionHandlerProcessor> processors) {
            this.exceptionHandlerProcessors = processors;
        }

        public Map<String, Object> process(Exception ex,ExceptionHandlerProcessorChain exceptionHandlerProcessorChain) {

            if (index >= exceptionHandlerProcessors.size()) {
                logger.warn("exception handler processor not found {},so return null",ex);
                return null;
            }
            //根据index的值，取到对应的processor
            ExceptionHandlerProcessor exceptionHandlerProcessor = exceptionHandlerProcessors.get(index++);
            //执行processor处理exception的方法
            return exceptionHandlerProcessor.handleException(ex, exceptionHandlerProcessorChain);
        }
    }

    /**
     * 定义一个 ExceptionHandlerProcessor 接口
     */
    public  interface ExceptionHandlerProcessor{
        Map<String, Object> handleException(Exception ex, ExceptionHandlerProcessorChain chain);
    }

    /**
     * 定义一个ExceptionHandlerProcessor抽象类
     */
    abstract class AbstractExceptionHandlerProcessor implements ExceptionHandlerProcessor {

        @Override
        public Map<String, Object> handleException(Exception ex, ExceptionHandlerProcessorChain chain) {
            if (belongCurrentProcessor(ex)) {
                return handleCurrentException(ex);
            }
            return chain.process(ex, chain);
        }

        abstract boolean belongCurrentProcessor(Exception ex);

        abstract Map<String, Object> handleCurrentException(Exception ex);

    }

    /**
     * 定义一个处理 ConsumptionException的processor;
     */
    class ConsumptionExceptionHandlerProcessor extends   AbstractExceptionHandlerProcessor{

        @Override
        boolean belongCurrentProcessor(Exception ex) {
            return ex instanceof ConsumptionException;
        }

        @Override
        Map<String, Object> handleCurrentException(Exception ex) {
            Map<String, Object> attributes = new HashMap<>();
            ConsumptionException exception = (ConsumptionException)ex;
            attributes.put("code", exception.getCode().getCode());
            attributes.put("message", exception.getMessage());
            return attributes;
        }
    }

    /**
     * 定义一个IllegalArgumentExceptionHandlerProcessor 处理IllegalArgumentException
     */
    class IllegalArgumentExceptionHandlerProcessor extends  AbstractExceptionHandlerProcessor{

        @Override
        boolean belongCurrentProcessor(Exception ex) {
            return ex instanceof  IllegalArgumentException;
        }

        @Override
        Map<String, Object> handleCurrentException(Exception ex) {
            Map<String, Object> attributes = new HashMap<>();
            IllegalArgumentException exception = (IllegalArgumentException)ex;
            attributes.put("code", "500");
            attributes.put("message", exception.getMessage());
            return attributes;
        }
    }

    /**
     * 定义一个处理ControllerExceptionProcessor 用于处理来之ControllerExceptionHandler的异常
     */
    class ControllerExceptionHandlerProcessor extends  AbstractExceptionHandlerProcessor{

        @Override
        boolean belongCurrentProcessor(Exception ex) {
            return _isFromControllerException(ex);
        }


        @Override
        Map<String, Object> handleCurrentException(Exception ex) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("code", "500");
            attributes.put("message", ex.getMessage());
            return attributes;
        }
    }

    /**
     * 判断异常是否来之ControllerExceptionHandler
     * @param ex
     * @return
     */
    private boolean _isFromControllerException(Exception ex) {

        return ex instanceof HttpRequestMethodNotSupportedException || ex instanceof HttpMediaTypeNotSupportedException ||
                ex instanceof HttpMediaTypeNotAcceptableException || ex instanceof MissingPathVariableException ||
                ex instanceof MissingServletRequestParameterException || ex instanceof ServletRequestBindingException ||
                ex instanceof ConversionNotSupportedException || ex instanceof TypeMismatchException ||
                ex instanceof HttpMessageNotReadableException || ex instanceof HttpMessageNotWritableException ||
                ex instanceof MethodArgumentNotValidException || ex instanceof MissingServletRequestPartException ||
                ex instanceof BindException || ex instanceof NoHandlerFoundException ||
                ex instanceof AsyncRequestTimeoutException;
    }

    /**
     * 定义一个SQLExceptionHandlerProcessor 处理IllegalArgumentException
     */
    class DataAccessExceptionHandlerProcessor extends  AbstractExceptionHandlerProcessor{

        @Override
        boolean belongCurrentProcessor(Exception ex) {
            return ex instanceof DataAccessException;
        }

        @Override
        Map<String, Object> handleCurrentException(Exception ex) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("code", "500");
            attributes.put("message", ex.getMessage());
            return attributes;
        }
    }

    /**
     * 定义一个最后会执行的ExceptionHandlerProcessor
     */
    class LastExceptionHandlerProcessor extends   AbstractExceptionHandlerProcessor{

        @Override
        boolean belongCurrentProcessor(Exception ex) {
            return true;
        }
        @Override
        Map<String, Object> handleCurrentException(Exception ex) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("code", "500");
            attributes.put("message", "unknown error");

            return attributes;
        }
    }


}
