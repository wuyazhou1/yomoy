package com.nsc.Amoski.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice
//@ResponseBody
public class ParentValidatorFactoryMessage {
    private static final Logger log = LoggerFactory.getLogger(ParentValidatorFactoryMessage.class);

    public ParentValidatorFactoryMessage() {
    }


//    @ExceptionHandler({MethodArgumentNotValidException.class})
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String field = error.getField();
        String code = error.getDefaultMessage();
        String message = String.format("%s:%s", field, code);
        log.warn("参数验证失败：" + message);
        return "参数验证失败：" + message;
    }
}
