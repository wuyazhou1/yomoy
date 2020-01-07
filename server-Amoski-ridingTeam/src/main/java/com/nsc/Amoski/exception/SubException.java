package com.nsc.Amoski.exception;


import com.nsc.Amoski.dto.ResultMsg;

/**
 *@Author: yw
 *@Desciption: 自定义异常
 *@Date:14:01 2018/5/24
 *@param:No such property: code for class: Script1
 */
public class SubException extends  RuntimeException {

    public SubException(){};
    public SubException(String code , String msg){
        super(msg);
        this.code=code;
        this.msg=msg;
    };
    public SubException(ResultMsg msg){
        super(msg.getMessage());
        this.code=msg.getCode();
        this.msg=msg.getMessage();
    }
    //提示信息
    private String msg;
    //提示码
    private String code ;

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
}
