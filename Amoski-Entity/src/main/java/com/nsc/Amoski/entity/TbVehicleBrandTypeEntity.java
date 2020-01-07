package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//车辆品牌下机车类型
@Entity
@Table(name = "TB_VEHICLE_BRAND_TYPE")
@Data
public class TbVehicleBrandTypeEntity  implements  Serializable{
    //主键(自动增长)
    @Id
    public Integer id;
    //车辆类型名称
    @Column(name = "NAME")
    public String name;
    //品牌id
    @Column(name = "BRAND_ID")
    public Integer brandId;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;

}