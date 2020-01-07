package com.nsc.Amoski.WXPay;

public class WXPayConfig {
    //服务器地址
    private static String url = "http://yomoy.com.cn";
    //加密方式
    public static String signType = "HMAC-SHA256";

    /************************************公众号*******************************************/
    //公众账号ID
    public static String appid = "wxb32615d0ad90ffa8";
    //公众号密钥
    public static String gKey = "bdcd3b303db0ebaa870fca3fdd00ce40";
    //商户号
    public static String mch_id = "1542700051";
    //商户密钥
    public static String Key = "31f8b1e0bde84e18af466f082a2006d3";
    //服务器异步通知页面路径
    public static String notify_url = "http://yomoy.com.cn/AmoskiActivity/activityOrderManage/excludeRequest/wechatCallBack";

/*    *//************************************开发平台*******************************************//*
    //开发平台appid
    public static String app_appid = "wx7673bae8f2a12763";
    //商户号
    public static String app_mch_id = "1497921122";
    //商户密钥
    public static String app_Key = "9Hc7s12WpiC03sUv6Cm5j3bQ14GvI0K6";
    //服务器异步通知页面路径
    public static String app_notify_url = url+"/pay/getWxpayAppTradePayNotify";*/
}
