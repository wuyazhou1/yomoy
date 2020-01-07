package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_FORBID_DETAIL_INFO")
@Data
public class TbForbidDetailInfoEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_FORBID_DETAIL_INFO_SEQUENCE")
    private Integer id;
    //禁摩信息id
    @Column(name = "FORBID_ID")
    private Integer forbidId;
    //禁摩路线名称(多个用,隔开)
    @Column(name = "FORBID_ROAD_NAME")
    private String forbidRoadName;
    //禁摩路线经纬度(多个用,隔开)
    @Column(name = "FORBID_ROAD_POINT")
    private String forbidRoadPoint;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //城市代码
    @Column(name = "CITY_CODE")
    private String cityCode;
    //状态(0:消禁 1:禁止)
    @Column(name = "STATUS")
    private String status;
    //路线名称
    @Column(name = "FORBID_NAME")
    private String forbidName;
    //路线类型(1.路线 2.区域)
    @Column(name = "FORBID_TYPE")
    private String forbidType;

}