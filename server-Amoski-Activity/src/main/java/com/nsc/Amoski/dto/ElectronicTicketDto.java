package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.TbActivityBillImageEntity;
import com.nsc.Amoski.util.RegUtil;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//活动电子票
@Data
public class ElectronicTicketDto implements  Serializable{

    private String signUpTel;
    //订单详情id
    @Id
    private String id;
    //订单代码
    private String code;
    //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
    private String state;
    //订单金额
    private BigDecimal primitiveMoney;
    //创建人
    private String createName;
    //创建时间
    private Timestamp createData;
    //修改人
    private String updateName;
    //修改时间
    private Timestamp updateDate;

    //活动时辰安排id
    private Integer scheduleTimeId;
    //验票数字码
    private String validCode;
    //验票状态(1.成功 2.已使用 3.已失效 4.无效)
    private String vastate;
    //创建时间
    private Timestamp vacreateTime;
    //创建人
    private String createUser;
    //会员id
    private String memberId;


    //报名人员信息

    //活动基础id
    private String basicsId;
    //头像路径
    private String headPortraitUrl;
    //姓名
    private String name;
    //性别
    private String sex;
    //票种id
    private String invoiceId;
    //票种名称
    private String invoiceName;
    //订单id
    private String orderId;
    //备注
    private String remake;
    //验票数字码
    private String verificationCode;
    //验票时间
    private String verificationDate;

    //订单所有验票信息
    private List<Map<String,Object>> list=new ArrayList<>();

    //活动标题
    private String title;
    //集合地
    private String collectionPlace;
    //集合时间
    private Date collectionTime;
    //活动开始时间
    private Date activityStart;
    //活动结束时间
    private Date activityStop;
    //活动报名截止时间
    private Date activityEnd;

    //开始时间
    private String startTime;
    //结束时间
    private String stopTime;
    //描述类型
    private String introduceType;
    //描述
    private String introduce;

    //活动海报
    private String fileNameUrl;

    //电子票是否有效(0,无效 1.有效)
    private Integer effective;

}