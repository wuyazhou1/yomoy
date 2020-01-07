package com.nsc.Amoski.exception;


import com.nsc.Amoski.dto.ResultMsg;

/**
 *@Author: yw
 *@Desciption: 不合法异常
 *@Date:10:04 2018/5/25
 *@param:No such property: code for class: Script1
 */
public class PuppetException extends SubException {
    public PuppetException(){};
    public PuppetException(String code , String msg){
        super(code,msg);
    };
    public PuppetException(ResultMsg msg){
        super(msg);
    }
}
