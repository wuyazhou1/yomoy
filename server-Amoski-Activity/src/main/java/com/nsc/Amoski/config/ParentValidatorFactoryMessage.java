package com.nsc.Amoski.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@ResponseBody
public class ParentValidatorFactoryMessage {
    private static final Logger log = LoggerFactory.getLogger(ParentValidatorFactoryMessage.class);

    public ParentValidatorFactoryMessage() {
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public Object handleBindException(BindException e) {
        //log.error("参数绑定校验异常", e);
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("code","10001");
        resultMap.put("msg",e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return resultMap;
    }

    @ExceptionHandler({ArithmeticException.class})
    public Map<String,String> BindExceptionaaa(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        log.warn("参数验证失败：" + message);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("code","10001");
        resultMap.put("msg",code);
        resultMap.put("data",field);
        return resultMap;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Map<String,String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        log.warn("参数验证失败：" + message);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("code","10001");
        resultMap.put("msg",code);
        resultMap.put("data",field);
        return resultMap;
    }
}
