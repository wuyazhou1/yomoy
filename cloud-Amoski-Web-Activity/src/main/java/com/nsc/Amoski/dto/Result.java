package com.nsc.Amoski.dto;


/**
 *@Author: fh
 *@Desciption:统一返回json的格式
 *@Date:14:00 2018/5/30
 *@param:No such property: code for class: Script1
 */
public class Result {
    //提示信息
    private String msg;
    //提示码
    private String code ;
    ///内容
    private Object data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
