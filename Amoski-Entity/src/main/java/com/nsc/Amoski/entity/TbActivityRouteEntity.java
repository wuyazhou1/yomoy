package com.nsc.Amoski.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//活动线路安排表
@Entity
@Table(name = "TB_ACTIVITY_ROUTE")
@Data
public class TbActivityRouteEntity  implements  Serializable{

    //途径点地址详情
    @Column(name = "ADDRESS")
    private String address;
    //活动时辰安排id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_ROUTE_SEQUENCE")
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动日程安排id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId;
    //部门代码
    @Column(name = "ORG_CODE")
    private String orgCode;
    //线路排序编码
    @Column(name = "ORDER_ID")
    private String orderId;
    //途径点类型
    @Column(name = "PATH_POINT_TYPE")
    private String pathPointType;
    //途径点Y轴
    @Column(name = "Y_AXIS")
    private String yAxis;
    //途径点X轴
    @Column(name = "X_AXIS")
    private String xAxis;
    //途径点名称
    @Column(name = "PATH_POINT_NAME")
    private String pathPointName;
    //途径点描述
    @Column(name = "DESCRIBE")
    private String describe;
    //途径距离
    @Column(name = "DISTANCE")
    private String distance;
    //所需时间（分钟）
    @Column(name = "TIME_REQUIRED")
    private String timeRequired;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private Date createData;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private Date updateDate;
    //活动线路图片表
    @Transient
    private List<TbActivityRouteImageEntity> tbActivityRouteImageEntity;
}
