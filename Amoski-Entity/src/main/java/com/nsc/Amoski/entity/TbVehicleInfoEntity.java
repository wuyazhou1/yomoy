package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//车辆信息管理
@Entity
@Table(name = "TB_VEHICLE_INFO")
@Data
public class TbVehicleInfoEntity  implements  Serializable{
    //主键(自动增长)
    @Id
    public Integer id;
    //用户id
    @Column(name = "MEMBER_ID")
    public Integer memberId;
    //车辆名称
    @Column(name = "CAR_NAME")
    public String carName;
    //车辆图片
    @Column(name = "CAR_IMG")
    public String carImg;
    //图片基础路径
    @Column(name = "BASE_URL")
    public String baseUrl;
    //车辆型号
    @Column(name = "CAR_BRAND_ID")
    public Integer carBrandId;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //状态
    @Column(name = "STATUS")
    private String status;
    //是否默认
    @Column(name = "IS_DEFAULT")
    private String isDefault;
    //车辆品牌名称
    @Column(name = "brand_name")
    public String brandName;
    //车辆品牌下型号名称
    @Column(name = "brand_type_name")
    public String brandTypeName;


}