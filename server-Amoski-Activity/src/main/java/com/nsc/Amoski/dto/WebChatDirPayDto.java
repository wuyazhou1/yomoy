package com.nsc.Amoski.dto;

import lombok.Data;

@Data
public class WebChatDirPayDto {

	private String nonce_str;//随机字符串
	private String body;//商品简单描述
	private String attach;//附加数据,如果是支付，取资金账号，如果是付款，取用户的真实姓名
	private String spbill_create_ip;//提交用户端ip
	private String time_start;//订单生成时间 格式为yyyyMMddHHmmss
	private String time_expire;//订单失效时间yyyyMMddHHmmss
	private String timestamp;//第三方浏览器支付时候使用
	private String notify_url;//异步通知地址
	private String openid;//trade_type =JSAPI，此参数必传，用户在商户appid下的唯一标识。
}
