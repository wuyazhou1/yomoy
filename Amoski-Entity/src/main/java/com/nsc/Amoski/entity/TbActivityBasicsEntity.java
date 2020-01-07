package com.nsc.Amoski.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


//活动基础表
@Entity
@Table(name = "TB_ACTIVITY_BASICS")
@Data
public class TbActivityBasicsEntity  implements  Serializable{
    //活动基础id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_BASICS_SEQUENCE")
    private Integer id;
    //活动代码
    @Column(name = "CODE")
    private String code;
    //null
    @Column(name = "ORG_CODE")
    private String orgCode;
    //当前访问量
    @Column(name = "CURRENT_TRAFFIC")
    private Integer currentTraffic;
    //当天访问量
    @Column(name = "DAILY_VISITS")
    private Integer dailyVisits;
    //总访问量
    @Column(name = "TOTAL_VISITS")
    private Integer totalVisits;
    //活动类型
    @Column(name = "TYPE")
    private String type;
    //活动标题
    @Column(name = "TITLE")
    private String title;
    //活动性质
    @Column(name = "NATURE")
    private String nature;
    //活动状态1(保存，草稿)，2发布，3已失效
    @Column(name = "STATE")
    private String state;
    //活动描述
    @Column(name = "DESCRIBE")
    private String describe;
    //活动须知
    @Column(name = "NOTICE")
    private String notice;
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
    //活动海报图片表
    @Transient
    private List<TbActivityBillImageEntity> tbActivityBillImageEntity;
    //活动票种表
    @Transient
    private List<TbCtivityInvoiceEntity> tbCtivityInvoiceEntity;
    //活动退款设置表
    @Transient
    private List<TbActivityRefundSettingsEntity> tbActivityRefundSettings;

}