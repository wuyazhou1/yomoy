package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//途径点信息
@Entity
@Table(name = "TB_ROUTE_POINT_INFO")
@Data
public class TbRoutePointInfoEntity implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ROUTE_POINT_INFO_SEQUENCE")
    public Integer id;
    //途径点名称
    @Column(name = "route_name")
    private String routeName     ;
    //途径点详细地址
    @Column(name = "address")
    private String address     ;
    //预计骑行时间
    @Column(name = "ridingtime")
    private String ridingtime     ;
    //预计里程
    @Column(name = "aboutdis")
    private String aboutdis       ;
    //路线小图标
    @Column(name = "route_icon")
    private String routeIcon     ;
    //途径点备注
    @Column(name = "remake")
    private String remake         ;
    //路线id
    @Column(name = "route_id")
    private Integer routeId       ;
    //点类型
    @Column(name = "type")
    private Integer type       ;
    //图片路径
    @Column(name = "img_url")
    private String imgUrl       ;
    //图片基础路径
    @Column(name = "base_url")
    private String baseUrl       ;


    //结束地点经度
    @Column(name = "lng")
    private String lng    ;
    //结束地点纬度
    @Column(name = "lat")
    private String lat       ;
    //当前点到下一个点的线路描述
    @Column(name = "LINEDESC")
    private String linedesc       ;
    //简介缩减文本
    @Column(name = "SIMP_INTRODUCTION")
    private String simpIntroduction       ;
    //简介缩减文本
    @Column(name = "LINE_INTRODUCTION")
    private String lineIntroduction       ;
    //图片路径1
    @Column(name = "img_url1")
    private String imgUrl1       ;
    //图片路径2
    @Column(name = "img_url2")
    private String imgUrl2      ;
    //图片路径3
    @Column(name = "img_url3")
    private String imgUrl3       ;


}
