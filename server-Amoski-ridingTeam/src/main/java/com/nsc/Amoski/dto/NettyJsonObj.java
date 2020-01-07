package com.nsc.Amoski.dto;

import lombok.Data;


@Data
public class NettyJsonObj {

    private String type;//请求类型
    private NettyJsonBodyObj body;//数据体
    private String code;//请求返回码
    private Integer userId;//用户id
    private String teamCode;//队伍口令
    private String teamId;//队伍id
    private String msg;//请求返回码描述
    private Long sendTime;//发送时间
}
