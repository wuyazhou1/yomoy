package com.nsc.Amoski.dto;


import lombok.Data;


//微信接口参数dto
@Data
public class WechatDto{
    private String access_token;

    private String expires_in;

    private String refresh_token;

    private String openid;

    private String scope;
}
