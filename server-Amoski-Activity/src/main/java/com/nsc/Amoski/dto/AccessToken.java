package com.nsc.Amoski.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取微信token的类
 */
@Data
public class AccessToken implements Serializable{

    private String access_token;

    private Long expires_in;

    private String refresh_token;//用户刷新access_token  网页授权access_token
    private String openid;//用户唯一标识
    private String scope;
}