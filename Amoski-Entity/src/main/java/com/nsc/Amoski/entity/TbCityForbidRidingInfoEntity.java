package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_CITY_FORBID_RIDING_INFO")
@Data
public class TbCityForbidRidingInfoEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_CITY_FORBID_RIDING_INFO_SEQUENCE")
    private Integer id;
    //城市代码
    @Column(name = "CITY_CODE")
    private String cityCode;
    //城市名称
    @Column(name = "CITY_NAME")
    private String cityName;
    //禁止区域个数
    @Column(name = "FORBID_AREA_COUNT")
    private Integer forbidAreaCount;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //政策描述
    @Column(name = "POLICY_DESC")
    private String policyDesc;
    //修改时间
    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;
    //修改人
    @Column(name = "UPDATE_USER")
    private String updateUser;
    //状态(0:消禁 1:禁止)
    @Column(name = "STATUS")
    private String status;
}