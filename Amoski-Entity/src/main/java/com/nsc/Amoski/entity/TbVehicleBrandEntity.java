package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//车辆品牌信息
@Entity
@Table(name = "TB_VEHICLE_BRAND")
@Data
public class TbVehicleBrandEntity  implements  Serializable{
    //主键(自动增长)
    @Id
    public Integer id;
    //品牌名称
    @Column(name = "BRAND_NAME")
    public String brandName;
    //品牌英文名称
    @Column(name = "BRAND_ENGLISH_NAME")
    public String brandEnglishName;
    //品牌图标
    @Column(name = "BRAND_ICO")
    public String brandIco;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;

}