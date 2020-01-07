package com.nsc.Amoski.entity;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//骑行路书信息表
@Entity
@Table(name = "TB_RIDING_GUIDE_INFO")
@Data
public class TbRidingGuideInfoEntity implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_RIDING_GUIDE_INFO_SEQUENCE")
    public Integer id;
    //路书下载数量
    public String userGuideCount;
    @Column(name = "EXPONENT")
    public String exponent;
    //路书标题
    @Column(name = "title")
    private String title          ;
    //路书海报url
    @Column(name = "bill")
    private String bill           ;
    //特色
    @Column(name = "feature")
    private String feature        ;
    //出发地
    @Column(name = "startaddr")
    private String startaddr      ;
    //途径点
    @Column(name = "routepoint")
    private String routepoint     ;
    //强度等级
    @Column(name = "strengthgrade")
    private String strengthgrade  ;
    //风景等级
    @Column(name = "scenerygrad")
    private String scenerygrad    ;
    //经验要求
    @Column(name = "experience")
    private String experience     ;
    //适宜季节
    @Column(name = "season")
    private String season         ;
    //状态(0.删除 1.草稿 2.已发布)
    @Column(name = "status")
    private Integer status         ;
    //浏览量
    @Column(name = "lookcount")
    private Integer lookcount      ;
    //骑行时间
    @Column(name = "ridingtime")
    private String ridingtime     ;
    //车型建议
    @Column(name = "vehicletype")
    private String vehicletype    ;
    //途径点数量
    @Column(name = "routepointcount")
    private Integer routepointcount;
    //预计里程
    @Column(name = "aboutdis")
    private String aboutdis       ;
    //简介
    @Column(name = "introduction")
    private String introduction   ;
    //创建时间
    @Column(name = "create_time")
    private Timestamp createTime    ;
    //创建人
    @Column(name = "create_user")
    private String createUser    ;
    //目的地
    @Column(name = "endaddr")
    private String endaddr        ;
    //关键字
    @Column(name = "keys")
    private String keys           ;
    //路书类型(1.官方路书 2.自定义路书)
    @Column(name = "GUIDE_TYPE")
    private String guideType           ;
    //海报图片baseUrl
    @Column(name = "base_url")
    private String baseUrl        ;
    //结束地点经度
    @Column(name = "lng")
    private String lng    ;
    //结束地点纬度
    @Column(name = "lat")
    private String lat       ;

    //是否推荐(0.不推荐 1.推荐)
    @Column(name = "RECOMMEND")
    private String recommend        ;
    //推荐等级
    @Column(name = "RECOMMENDLEVEL")
    private String recommendLevel    ;
    //简介缩减文本
    @Column(name = "SIMP_INTRODUCTION")
    private String simpIntroduction       ;
    //宽
    @Column(name = "WIDTH")
    private String width;
    //高
    @Column(name = "HEIGHT")
    private String height;


}
