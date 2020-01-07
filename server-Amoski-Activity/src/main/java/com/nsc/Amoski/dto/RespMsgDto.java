package com.nsc.Amoski.dto;

import lombok.Data;

@Data
public class  RespMsgDto{

	private String uuid;//跟request的uuid一致
	private String respCode;//10000=响应成功,其他代表失败
	private String message;//错误描述，如果为10000，返回success
	private String orderNo;//订单号
	private String bankflow;//银行流水
	private String responseTime;//响应时间 yyyy-MM-dd HH:mm:ss
	private String noticeTime;//第三方响应时间 yyyy-MM-dd HH:mm:ss
	private String respContent;//订单提交完成，有的接口需要返回二维码url，有的接口返回html类容
	private String remark;//备注，可能为空
}
