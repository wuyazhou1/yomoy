package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.TbRoutePointInfoEntity;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

//路书线路信息
@Data
public class TbGuideRouteInfoDto implements Serializable {

    //主键(自动增长)
    public Integer id;
    //当天路线开始地点
    private String startaddr      ;
    //当天路线结束地点
    private String endaddr        ;
    //第几天
    private Integer day            ;
    //简介
    private String introduction   ;
    //所有途径点
    private String allRoutepoint ;
    //预计里程
    private String aboutdis       ;
    //预计骑行时间
    private String ridingtime     ;
    //创建时间
    private Timestamp createTime    ;
    //创建人
    private String createUser    ;
    //路书id
    private Integer guideId       ;
    //开始地点经度
    private String startlng    ;
    //开始地点纬度
    private String startlat       ;
    //结束地点经度
    private String endlng    ;
    //结束地点纬度
    private String endlat       ;
    //简介缩减文本
    private String simpIntroduction       ;


    //线路所有途经点数据
    private List<TbRoutePointInfoEntity> pointList=new ArrayList<TbRoutePointInfoEntity>();


    //途经点信息
    //途经点id
    public Integer pId;
    //途径点名称
    private String routeName     ;
    //途径点详细地址
    private String address     ;
    //预计骑行时间
    private String pRidingtime     ;
    //预计里程
    private String pAboutdis       ;
    //路线小图标
    private String routeIcon     ;
    //途径点备注
    private String remake         ;
    //路线id
    private Integer routeId       ;
    //途径点经度
    private String lng    ;
    //途径点纬度
    private String lat       ;
    //点类型(1.起点 2.终点 3.途经点)
    private Integer type       ;
    //图片路径
    private String imgUrl       ;
    //图片基础路径
    private String baseUrl       ;
    //简介缩减文本
    private String lineIntroduction       ;
    //图片路径1
    private String imgUrl1       ;
    //图片路径2
    private String imgUrl2      ;
    //图片路径3
    private String imgUrl3       ;

}
