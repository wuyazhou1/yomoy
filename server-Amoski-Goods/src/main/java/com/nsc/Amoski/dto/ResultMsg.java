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
    SUCCESS("10000","成功"),
    IS_NULL("10001","参数校验失败"),
    LONGTIMEOUT("10009","登录超时"),
    UNKNOWN_EXCEPTION("10002","系统繁忙"),
    FAIL("10003","失败"),
    NOT_LOGIN("10004","还没登录，请重新登录"),
    USER_NOTEXISTS("10005","用户不存在,或已被设置黑名单"),

    MOBILE_FORMAT_ERROR("20000","手机号格式错误"),
    MOBILECODE_NOTEXIST_ERROR("20001","验证码已失效或不存在"),
    MOBILECODE_ERROR_ERROR("20002","验证码错误"),
    REGISTER_PWDILLEGALITY_ERROR("20003","密码格式错误"),
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
