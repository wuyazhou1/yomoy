package com.nsc.Amoski.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//车辆品牌下机车类型
@Data
public class VehicleBrandTypeDto  implements  Serializable{
    private static final long serialVersionUID= 978968329845312L;
    //主键(自动增长)
    private Integer id;
    //车辆类型名称
    private String name;
    //品牌id
    private Integer brandId;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;

}