package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//车辆信息管理
@Data
public class VehicleInfoDto  implements  Serializable{
    private static final long serialVersionUID= 9324789645312L;
    //主键(自动增长)
    private Integer id;
    //用户id
    private Integer memberId;
    //车辆名称
    private String carName;
    //车辆图片
    private String carImg;
    //图片基础路径
    private String baseUrl;
    //车辆品牌
    private Integer carBrandId;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //状态
    private String status;
    //是否默认
    private String isDefault;
    //车辆品牌名称
    private String brandName;
    //车辆品牌下型号名称
    private String brandTypeName;

}