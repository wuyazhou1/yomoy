package com.nsc.Amoski.controller;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.entity.jg.ReturnCode;
import com.nsc.Amoski.exception.JGException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.Map;

/**
 * @author 李阳
 * @date 2019/12/12 17:07
 */
@RestControllerAdvice
public class HandlerController {

    @ExceptionHandler(value = {JGException.class, APIRequestException.class, APIConnectionException.class})
    public Result jg(JGException e) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(e.getMessage(), Map.class);
            objectMapper = null;
            return new Result(ReturnCode.NO, map);
        } catch (IOException e1) {
            return new Result(ReturnCode.NO, e1);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public Result all(Exception e) {
        return new Result(ReturnCode.NO, e.getMessage());
    }

}
