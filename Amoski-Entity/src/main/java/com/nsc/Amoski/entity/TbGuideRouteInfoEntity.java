package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//路书线路信息
@Entity
@Table(name = "TB_GUIDE_ROUTE_INFO")
@Data
public class TbGuideRouteInfoEntity implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_GUIDE_ROUTE_INFO_SEQUENCE")
    public Integer id;
    //当天路线开始地点
    @Column(name = "startaddr")
    private String startaddr      ;
    //当天路线结束地点
    @Column(name = "endaddr")
    private String endaddr        ;
    //第几天
    @Column(name = "day")
    private Integer day            ;
    //简介
    @Column(name = "introduction")
    private String introduction   ;
    //所有途径点
    @Column(name = "all_routepoint")
    private String allRoutepoint ;
    //预计里程
    @Column(name = "aboutdis")
    private String aboutdis       ;
    //预计骑行时间
    @Column(name = "ridingtime")
    private String ridingtime     ;
    //创建时间
    @Column(name = "create_time")
    private Timestamp createTime    ;
    //创建人
    @Column(name = "create_user")
    private String createUser    ;
    //路书id
    @Column(name = "guide_id")
    private Integer guideId       ;

    //开始地点经度
    @Column(name = "STARTLNG")
    private String startlng    ;
    //开始地点纬度
    @Column(name = "STARTLAT")
    private String startlat       ;
    //结束地点经度
    @Column(name = "ENDLNG")
    private String endlng    ;
    //结束地点纬度
    @Column(name = "ENDLAT")
    private String endlat       ;

    //简介缩减文本
    @Column(name = "SIMP_INTRODUCTION")
    private String simpIntroduction       ;

}
