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
    NOT_LOGIN("10004","还没登录，请重新登录"),
    USER_NOTEXISTS("10005","用户不存在"),
    USER_BLACKBILL_NOTEXISTS("10006","用户已被设置黑名单"),
    USER_ACCOUNTORPWD_ERROR("10007","账号或密码错误"),


    MOBILE_FORMAT_ERROR("20000","手机号格式错误"),
    MOBILECODE_NOTEXIST_ERROR("20001","验证码已失效或不存在"),
    MOBILECODE_ERROR_ERROR("20002","验证码错误"),
    REGISTER_PWDILLEGALITY_ERROR("20003","密码格式错误"),
    REGISTER_MOBILEEXIST_ERROR("20004","手机号已被注册"),
    TWO_EFFECT_ERROR("20005","验证码2分钟有效"),

    UPDPWD_TWODISFERENCE_ERROR("20006","两次密码不一致"),
    FORGET_OLDPWD_ERROR("20007","旧密码错误"),
    IDENTITY_FORMAT_ERROR("20008","身份证号码格式错误"),
    USER_VERIFIED_ERROR("20009","用户已认证"),

    ACTIVITY_ORDEREXIST_ERROR("60000","当前活动已报名"),


    ORDER_NOTEXIST_ERROR("70000","订单不存在"),
    ACTIVITY_NOTEXIST_ERROR("70001","活动不存在"),
    ACTIVITY_FAILURE_ERROR("70002","报名已截止"),
    ORDER_RETUNRN_ERROR("70003","订单已退款"),


    UPLOADFILE_EMPTY_ERROR("30000","上传文件为空"),
    UPLOADFILE_TYPE_ERROR("30001","上传文件类型错误"),
    MOBILE_APPLY_ERROR("30002","当前手机号已报名"),
	LOG_DEFAULT_CLASSNAME("默认日志输出文件名", "CWA_Client"),

    NUMBER_OF_POINTS("30003","该用户已经取消点赞"),
    COMMENT_QUANTITY("30004","该用户已经评论"),
    HAVE_BEEN_COLLECTED("30005","该用户已经取消收藏"),
    ALREADY_CONCERNED("30006","该用户已经取消关注"),


    ;
	
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
