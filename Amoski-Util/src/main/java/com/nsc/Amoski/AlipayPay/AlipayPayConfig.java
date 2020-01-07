package com.nsc.Amoski.AlipayPay;

import java.util.UUID;

public class AlipayPayConfig {

    //密钥 YRV8GaEQgPs6DSOT559ZPA==

    /**测试环境配置**/
    // 商户appid
    public static String APPID = "2019073066097147";
    // 支付宝应用私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDEBbkTBUi4pFDjxfLXFvWUBi2gYyEZx9fuJaYSYdrJL2NOIkeDL5hrtVaIuR+rALAwXOnOTBZq77HzFqTCz5+RdAdiUawVxnwatdGHb/KBP0FlluyHHBo6iAr2iwLhqiGwosNo4a8ToFMR1AaDj93oADufoeeC70XB19AWcWRxzO7um1R+MxkMSJkgCypVoXD14S5QpqHo3IECFYYTpEHU7L27xOE17COOnfEWgk/zJAX2+vGSsVh4SmMb+dPM4mMiN2Txs9eY3HG5vLLWuXgbP4YlfuFdhDeNl3M1RRbFWiM7a4VKJdj2KnZUEEj5x9HgRHWu46uk3Y/Z0pWPDjjzAgMBAAECggEAUNOHR6eDH9dpHzJM+pGV+0483Ahj6qxzhdfyTVwaCaDHkr0hvZ8Pe0IPh1rGCw3CmH6HTbAkAj4JoxFwKH/R7Pjp3omMqoHoRF86IV1wogiCH7CGoQNecUoNMzGIo6IyOBI0/vcn6LDP7+b8NJ1Kw9Hsw9rPija9Dbu+KrLg1ucOYvN/SdvxvxD/frBcN1at6LdEAJL4tK1+DCij+aXDenLRu/cHoA/wACEACKDFUjkBiU0m5KsHxQXJr+Pl21viAx4Tki7iLQVREtrHjn4Sc6DaPLDzZ5kxRCNiUx0oSjGjw4zvvPEJrS5wz9e3shHBwlQDdJcdRLIeZCn6PmWOoQKBgQD925PsZ1/7BerhFc/XnKOE/SuOX7cvAyZKwVaizqefVcm6zUMeMXPwRDEK5xcdHgcd4PwlLPlWu9Uub8TAsdyhYNEksUS4DNlHsqMnoX4ITCVCSekLbiQWPZh4/vleREFVzcmft12BFVnW+1i0e54x/9xhj4Il6asI7iIhlYri4wKBgQDFrTNJAOanuvNf4iRBm2XfBIL99144ASKqiGo7arZjYfv4REMvLxqhgPa8WqquLSVvWCka1sGZZT/y8JJGxWd9k0gx3lxrWDdwBANJbnSBTylKinQGL/qAKqe2oi8UKmp0Ra0obar6tIG1gI6oxwSwdra/c/axKytfsEcrz+hesQKBgQCmGoDl1JZG6A5SDTWb/RA5unY/iH2SM1tg2rTXfqTLaxS+OATMwsr70YLNgXvsP3Okp0wU/yJ/EBRjeBqjgz28El+GxL8UNsQNV+PN0Ktovno6r6XfJshSDSexNBTzC3I8tJRS/2YdSl0of/oAzkoqqSIIbCBaslSJWVfX5cOkxQKBgQCLZwZYwFrpBE0nXConXjhsGxlpkm4OXracvpD0D+BJm8r+nN0oRDaqmHDoCaR+KnZQ3EI4nCRxUdbUM1VktUezZg8uo9pXFSIbS6kpusxMeI/s8lC6IFGu2uXlROCgfv0NvClN7nD6Vht27Q8t9RPYgg+EWnopHQF8fKrkOb1sMQKBgGYWNXODYObvl6cBjr1EKKDzEFBndzXDpWxwc/H8ukF2ZfBausTTqRv0V2DQEkeZnDddNDJ/NygTbKkByJl8ozvN7COtzu6tS/uNxcgpkE0oEY2Rzu4xrTbFEB4gxGY5cCQ+pgicBn8Fy5Wpt+6dK+jS5yP0IlKiinmYOfdttE4w";
    //public static String RSA_PRIVATE_KEY = "qTfa0v2oZkQiw5sP5eB5zplVMH+vVRrqiFuZZMOuJSJdzFhGAD8DivvEROQItNPxMp6S1fVpy/0/TKsiuYQUKA5W93JkKjjdVrQyD/UXZcBNH7AlvkqTO3t3kdBPBkQ0rFD5qJqsjqxsJ0I6XH3FywaaUmX32lexqLj0diGhUo8=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://yomoy.com.cn/AmoskiActivity/activityOrderManage/excludeRequest/aplipayCallBack";
    //支付宝同步回调地址
    public static String return_url = "http://yomoy.com.cn/AmoskiWebActivity/personalcenter/roadbookActivitype/order/payment.html";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    //	public static String return_url = "http://商户网关地址/alipay.trade.wap.pay-JAVA-UTF-8/return_url.jsp";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝应用公钥
    //public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    //支付宝应用公钥
    public static String ALIPAY_PUBLIC_KEY ="/opt/webUserHtml/AliPay/MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj.crt";
    //支付宝公钥证书文件路径
    public static String ALIPAY_CERT_PATH = "/opt/webUserHtml/AliPay/rALAwXOnOTBZq77HzFqTCz5.crt";
    //支付宝CA根证书文件路径
    public static String ALIPAY_ROOT_CERT_PATH = "/opt/webUserHtml/AliPay/RdAdiUawVxnwatdGHb.crt";


    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";

    //public static String seller_id = "2088622978973186";


    public static void main(String[] args){
        UUID uuid=UUID.randomUUID();
        System.out.println(uuid.toString().replaceAll("-", ""));
    }

}
