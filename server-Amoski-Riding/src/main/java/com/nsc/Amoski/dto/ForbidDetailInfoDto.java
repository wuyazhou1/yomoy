package com.nsc.Amoski.dto;

import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Data
public class ForbidDetailInfoDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //禁摩信息id
    private Integer forbidId;
    //禁摩路线名称(多个用,隔开)
    private String forbidRoadName;
    //禁摩路线经纬度(多个用,隔开)
    private String forbidRoadPoint;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //城市代码
    private String cityCode;
    //状态(0:消禁 1:禁止)
    private String status;
    //路线类型(1.路线 2.区域)
    private String forbidName;
    //路线类型
    private String forbidType;

}