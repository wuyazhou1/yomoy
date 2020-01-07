package com.nsc.Amoski.dto;



/**
 *@Author: hf
 *@Desciption: 统一返回的状态码
 *@Date:14:00 2018/5/30
 *@param:No such property: code for class: Script1
 */
public enum ResultMsg {
    ///请问继续加状态码
    ///公用的状态码
    SUCCESS("0","成功"),
    IS_NULL("10001","参数校验失败"),
    LONGTIMEOUT("10009","登录超时"),
    UNKNOWN_EXCEPTION("10002","系统繁忙"),
    FAIL("10003","失败"),
    EXIST_RIDINGTEAM_ERROR("20000","已存在队伍中"),
    NOTEXIST_RIDINGTEAM_ERROR("20001","骑行队伍不存在或失效"),
    NOTEXIST_TEAMPERSON_ERROR("20002","不存在该队伍中"),
    RIDINGTEAM_FILL_ERROR("20003","骑行队伍已满"),
	LOG_DEFAULT_CLASSNAME("默认日志输出文件名", "CWA_Client");
	
    private String message;
    private String code ;

    ResultMsg(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
