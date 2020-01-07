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

    UPLOADFILE_EMPTY_ERROR("30000","上传文件为空"),
    UPLOADFILE_TYPE_ERROR("30001","上传文件类型错误"),
    GUIDE_NOTEXIST_ERROR("30002","路书不存在"),
    ROUTE_NOTEXIST_ERROR("30003","线路不存在"),
    FORBID_CITY_NOT_EXISTS("20002","禁摩城市信息不存在"),
    FORBID_CITY_EXISTS("20001","城市已存在"),
    RIDING_VECHICLE_NOTEXIST("40000","骑行车辆不存在"),
    GUIDE_DOWN_EXISTS("50000","已下载路书"),
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
