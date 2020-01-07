package com.nsc.Amoski.exception;


import com.nsc.Amoski.dto.ResultMsg;

/**
 *@Author: yw
 *@Desciption:校验异常类
 *@Date:13:59 2018/5/24
 *@param:No such property: code for class: Script1
 */
public class VerifyException extends  SubException  {
    public VerifyException(){};
    public VerifyException(String code , String msg){
        super(code,msg);
    };
    public VerifyException(ResultMsg msg){
        super(msg);
    }
}
