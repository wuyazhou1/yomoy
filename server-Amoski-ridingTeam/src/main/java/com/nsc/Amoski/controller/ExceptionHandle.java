package com.nsc.Amoski.controller;

import com.nsc.Amoski.dto.ResultMsg;
import com.nsc.Amoski.entity.Result;
import com.nsc.Amoski.exception.SubException;
import com.nsc.Amoski.uti.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
@ResponseBody
public class ExceptionHandle {
    private Logger log = LoggerFactory.getLogger(ExceptionHandle.class);
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Result errorResult(RuntimeException e){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setStatus(200);
        log.error(">>>>>.catch!!! Exception:{}"+e.toString()+"=======;;;");
        e.printStackTrace();
        if(e instanceof SubException){
            SubException sub=(SubException)e;
            return ResultUtil.error(sub.getCode(),sub.getMsg());
        }
        return ResultUtil.error(ResultMsg.UNKNOWN_EXCEPTION.getCode(),ResultMsg.UNKNOWN_EXCEPTION.getMessage());
    }
}