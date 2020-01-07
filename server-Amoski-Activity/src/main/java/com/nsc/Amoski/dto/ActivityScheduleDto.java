package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.*;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//活动日程安排表
@Data
public class ActivityScheduleDto implements  Serializable{
    //日程id
    private Integer id;
    //活动基础id
    private String basicsId;
    //部门代码
    private String orgCode;
    //出发地
    private String placeDeparture;
    //目的地
    private String destination;
    //介绍
    private String introduce;
    //创建人
    private String createName;
    //创建时间
    private Date createData;
    //修改人
    private String updateName;
    //修改时间
    private Date updateDate;



    //活动时辰安排表
    private List<TbActivityTimeHistoryEntity> tbActivityTimeHistoryEntity=new ArrayList<>();
    //活动线路安排表
    private List<TbActivityRouteEntity> tbActivityRouteEntity=new ArrayList<>();
    //活动酒店表
    private TbActivityHotelEntity tbActivityHotelEntity;

    //吃的建议
    private  String eatAdvice;
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

    //途经点字段

    private Integer rid;
    //活动日程安排id
    private String scheduleId;
    //线路排序编码
    private String orderId;
    //途径点类型
    private String pathPointType;
    //途径点Y轴
    private String yAxis;
    //途径点X轴
    private String xAxis;
    //途径点名称
    private String pathPointName;
    //途径点描述
    private String describe;
    //途径距离
    private String distance;
    //所需时间（分钟）
    private String timeRequired;
}
