package com.nsc.Amoski.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

//车辆品牌信息
@Data
public class VehicleBrandDto implements  Serializable{
    private static final long serialVersionUID= 97898309645312L;
    //主键(自动增长)
    private Integer id;
    //品牌名称
    private String brandName;
    //品牌英文名称
    public String brandEnglishName;
    //品牌图标
    private String brandIco;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;

    private List<VehicleBrandTypeDto> list;

}