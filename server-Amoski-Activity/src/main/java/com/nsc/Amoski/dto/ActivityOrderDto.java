package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.TbActivityBillImageEntity;
import com.nsc.Amoski.entity.TbActivitySignUpEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//活动订单表
@Data
public class ActivityOrderDto implements  Serializable{
    //订单id
    @Id
    private String id;
    //订单代码
    private String code;
    //支付方式(1.微信 2.支付宝)
    private String payType;
    //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
    private String state;
    //订单金额
    private BigDecimal primitiveMoneySum;
    //订单数量
    private Integer sumCount;
    //创建人
    private String createName;
    //创建时间
    private Timestamp createData;
    //修改人
    private String updateName;
    //修改时间
    private Timestamp updateDate;
    //联系人手机号
    private String contactTel;
    //联系人姓名
    private String contactName;
    //会员id
    private Integer memberId;
    //领取码
    private Integer receivingCode;
    //是否提醒(0.不提醒 1.提醒)
    private String isRemind;


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
    //住址区域
    private String region;
    //身份证号码
    private String identityNumber;
    public void setIdentityNumber(String identityNumber){
        if(identityNumber!=null){
            this.identityNumber=identityNumber.substring(0,6)+"******"+identityNumber.substring(identityNumber.length()-2);
        }
    }
    //手机号码
    private String tel;
    //俱乐部
    private String club;
    //订单编号
    private String orderCode;
    //身份
    private String identitys;
    //车型
    private String models;
    //排量
    private String displacement;
    //服装数码
    private String clothingDigital;
    //备注
    private String remake;
    //摩托牌照
    private String motorcycleLicense;
    //汽车牌照
    private String vehicleLicense;
    //验票数字码
    private String verificationCode;
    //验票时间
    private String verificationDate;
    //紧急联系人
    private String emergencyContact;
    //紧急联系电话
    private String emergencyTel;
    //驾驶经验
    private String drivingExperience;

    //订单所有报名人员
    private List<ActivityOrderDto> list;

    //活动海报图片表
    private List<TbActivityBillImageEntity> tbActivityBillImageEntity;

    //订单详情字段
    //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
    private String detailState;
    //订单金额
    private BigDecimal primitiveMoney;
    //使用时间
    private Timestamp applyDate;
    //退票时间
    private Timestamp refundTicketDate;
    //同意退款时间
    private Timestamp agreeDate;


    //生成订单jsonStr
    private String createOrderJsonStr;

    //活动信息

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

    //途径点
    private String pathPoint;

    //退款字段

    //退款金额
    private String refundMoney;
    //1提交退款，2审核驳回退款，3审核通过退款
    private String restate;
    //创建人
    private String recreateName;
    //创建时间
    private Timestamp recreateData;
    //修改人
    private String reupdateName;
    //修改时间
    private Timestamp reupdateDate;

    //海报图片
    private String fileNameUrl;

    //电子票是否有效(0,无效 1.有效)
    private Integer effective;

}