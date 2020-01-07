package com.nsc.Amoski.dto;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//活动报名人表
@Data
public class ActivitySignUpDto implements  Serializable{
    //报名id
    private Integer id;
    //活动基础id
    private String basicsId;
    //姓名
    private String name;
    //性别
    private String sex;
    //住址区域
    private String region;
    //身份证号码
    private String identityNumber;
    //手机号码
    private String tel;
    //俱乐部
    private String club;
    //报名数量
    private String numberApplicants;
    //缴费金额
    private String paymentAmount;
    //验票数字码
    private String verificationCode;
    //验票时间
    private Date verificationDate;
    //票种名称
    private String ticketName;
    //紧急联系人
    private String emergencyContact;
    //紧急联系电话
    private String emergencyTel;
    //驾驶经验
    private String drivingExperience;
    //创建人
    private String createName;
    //创建时间
    private Date createData;
    //修改人
    private String updateName;
    //修改时间
    private Date updateDate;



    //报名人员接送信息

    //到达时间
    private Date arriveDate;
    //接送机id
    private Integer rid;
    //报名人员id
    private String peopleId;
    //接送类型（1接，2送）
    private String type;
    //航班时间
    private Date flightDate;
    //航班号
    private String flightNumber;
    //出发地
    private String placeDeparture;
    //目的地
    private String destination;
    //机场及航站楼
    private String describe;

}
