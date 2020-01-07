package com.nsc.Amoski.dto;

import lombok.Data;

@Data
public final class AliPayDirDto {

	 
	private String subject;//订单标题
	private String body;//订单描述
	
	private String goods_detail;//订单包含的商品列表信息，Json格式，详见商品明细说明
	private String passback_params;//公用回传参数，如果请求时传递了该参数，则返回给商户时会回传该参数。支付宝只会在异步通知时将该参数原样返回。本参数必须进行UrlEncode之后才可以发送给支付宝
	private String timeout_express;//该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。该参数在请求到支付宝时开始计时。
	
	private String qr_pay_mode;//跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。2：订单码-跳转模式
	 
	private String timestamp;//发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
	private String return_url;//同步返回地址，HTTP/HTTPS开头字符串
	private String notify_url;//支付宝服务器主动通知商户服务器里指定的页面http/https路径。
}
