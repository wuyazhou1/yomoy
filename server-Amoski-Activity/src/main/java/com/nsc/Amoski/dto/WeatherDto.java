package com.nsc.Amoski.dto;


import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

//天气地区编码表
@Data
public class WeatherDto {
    private Integer id;
    //地区码
    private String code;
    //省份
    private String province;
    //城市
    private String city;
    //地区
    private String area;
    //完整地址
    private String allAddr;
    //创建用户
    private String createUser;
    //创建时间
    private Timestamp createTime;

}
