package com.nsc.Amoski.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 微信公众号分享使用的参数
 */
@Data
public class JsapiTicket implements Serializable{

    private String errcode;

    private String errmsg;

    private String ticket;

    private Long expires_in;
}
