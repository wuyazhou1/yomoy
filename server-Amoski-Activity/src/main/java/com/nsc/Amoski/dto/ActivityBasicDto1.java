package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.*;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//活动日程安排表
@Data
public class ActivityBasicDto1 extends PageDto implements Serializable {
    private Integer id;
    //活动代码
    private String code;
    //null
    private String orgCode;
    //当前访问量
    private Integer currentTraffic;
    //当天访问量
    private Integer dailyVisits;
    //总访问量
    private Integer totalVisits;
    //活动类型
    private String type;
    //活动标题
    private String title;
    //活动性质
    private String nature;
    //活动状态1(保存，草稿)，2发布，3已失效
    private String state;
    //活动描述
    private String describe;
    //活动须知
    private String notice;
    //创建人
    private String createName;
    //创建时间
    private String createData;
    //修改人
    private String updateName;
    //修改时间
    private String updateDate;
    //活动海报
    private String fileNameUrl;

    //活动报名字段

    //活动报名必填字段
    private String mandatoryField;
    //报名范围（团队，个人）
    private String scopeRegistration;
    //集合地
    private String collectionPlace;
    //集合时间
    private Date collectionTime;
    //活动开始时间
    private String activityStart;
    //活动结束时间
    private String activityStop;
    //活动报名截止时间
    private Date activityEnd;
    //活动报名人数限制
    private String numberLimitation;
    //人数显示
    private String showNumber;

    //活动简介
    //活动玩法类型
    private String playType;
    //目的地（省市区）
    private String destination;
    //途径点
    private String pathPoint;
    //活动强度（休闲，比赛）
    private String activityIntensity;
    //装备要求
    private String equipmentRequirements;
    //活动详情
    private String detailsActivities;


    //活动海报图片表
    private List<TbActivityBillImageEntity> tbActivityBillImageEntity;
    //活动票种表
    private List<TbCtivityInvoiceEntity> tbCtivityInvoiceEntity;

    //活动时辰安排表
    private List<TbActivityTimeHistoryEntity> tbActivityTimeHistoryEntity = new ArrayList<>();
    //活动线路安排表
    private List<TbActivityRouteEntity> tbActivityRouteEntity = new ArrayList<>();
    //活动酒店表
    private TbActivityHotelEntity tbActivityHotelEntity;

    //吃的建议
    private String eatAdvice;
    //所有途径点
    private String roleName;
    //总距离
    private String allDistance;

    //酒店信息

    //酒店名称
    private String hotelName;
    //酒店政策
    private String hotelPolicy;
    //联系人
    private String contacts;
    //联系电话
    private String contactsTel;
    //报名人数
    private String signCount;
    //距离
    private Integer sqrtValue;
    //经度
    private String x_axis;
    //纬度
    private String y_axis;
    //集合地
    private String path_point_name;
}
