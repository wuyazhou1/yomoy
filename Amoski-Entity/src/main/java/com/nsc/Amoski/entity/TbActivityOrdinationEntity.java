package com.nsc.Amoski.entity;


import javax.persistence.Column;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//活动报名规则表
@Entity
@Table(name = "TB_ACTIVITY_ORDINATION")
@Data
public class TbActivityOrdinationEntity  implements  Serializable{

    //活动报名规则id
    @Id
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //null
    @Column(name = "ORG_CODE")
    private String orgCode;
    //活动报名必填字段
    @Column(name = "MANDATORY_FIELD")
    private String mandatoryField;
    //报名范围（团队，个人）
    @Column(name = "SCOPE_REGISTRATION")
    private String scopeRegistration;
    //集合地
    @Column(name = "COLLECTION_PLACE")
    private String collectionPlace;
    //集合时间
    @Column(name = "COLLECTION_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date collectionTime;
    //活动开始时间
    @Column(name = "ACTIVITY_START")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityStart;
    //活动结束时间
    @Column(name = "ACTIVITY_STOP")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityStop;
    //活动截止时间
    @Column(name = "ACTIVITY_END")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date activityEnd;
    //活动报名人数限制
    @Column(name = "NUMBER_LIMITATION")
    private String numberLimitation;
    //人数显示
    @Column(name = "SHOW_NUMBER")
    private String showNumber;
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