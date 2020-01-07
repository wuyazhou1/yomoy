package com.nsc.Amoski.dto;

import lombok.Data;

@Data
public class RequestMsgDto {

	private String uuid;//唯一标识
	private String webType;//网站类型，根据域名区分，以此来区分序列号
	private int inOutType;//支付类型 1=出金，2=入金,3=验证签名,4=在线充值
	private String payType;//支付方式：0000=测试，前两位代表支付方式大类，后两位代表支付方式小类，例如0101=微信直连,0201=支付宝直连,,0301=支付宝直连
	
	private String orderNo;//订单号
	private String orderAmount;//订单金额，如是验证签名，此参数写入签名数据
	
	private String content;//类容，如是验证签名，此参数写入待签名串
	private String chartSet;//编码方式，如果为null或者传递， 默认用utf-8
	private String loginType;//登录类型，根据类型区分支付接口，1=pc，2=手机浏览,3=公众号，4=安卓，5=ios
	
}
