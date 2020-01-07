package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//活动简介表
@Entity
@Table(name = "TB_ACTIVITY_SYNOPSIS")
@Data
public class TbActivitySynopsisEntity  implements  Serializable{

    //活动须知
    @Column(name = "ACTIVITY_NOTICE")
    @ColumnName("活动须知")
    private String activityNotice;
    //活动简介id
    @Id
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动玩法类型
    @Column(name = "TYPE")
    private String type;
    //目的地（省市区）
    @Column(name = "DESTINATION")
    private String destination;
    //途径点
    @Column(name = "PATH_POINT")
    private String pathPoint;
    //活动强度（休闲，比赛）
    @Column(name = "ACTIVITY_INTENSITY")
    private String activityIntensity;
    //装备要求
    @Column(name = "EQUIPMENT_REQUIREMENTS")
    private String equipmentRequirements;
    //活动详情
    @Column(name = "DETAILS_ACTIVITIES")
    private String detailsActivities;
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

}