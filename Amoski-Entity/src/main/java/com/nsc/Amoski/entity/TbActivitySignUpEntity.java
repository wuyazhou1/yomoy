package com.nsc.Amoski.entity;





import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//活动报名人员表
@Entity
@Table(name = "TB_ACTIVITY_SIGN_UP")
@Data
public class TbActivitySignUpEntity  implements  Serializable{
    //报名id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_SIGN_UP_SEQUENCE")
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //头像路径
    @Column(name = "HEAD_PORTRAIT_URL")
    private String headPortraitUrl;
    //null
    @Column(name = "ORG_CODE")
    private String orgCode;
    //姓名
    @Column(name = "NAME")
    private String name;
    //性别
    @Column(name = "SEX")
    private String sex;
    //住址区域
    @Column(name = "REGION")
    private String region;
    //身份证号码
    @Column(name = "IDENTITY_NUMBER")
    private String identityNumber;
    //手机号码
    @Column(name = "TEL")
    private String tel;
    //俱乐部
    @Column(name = "CLUB")
    private String club;
    //订单编号
    @Column(name = "ORDER_CODE")
    private String orderCode;
    //票号id
    @Column(name = "INVOICE_ID")
    private String invoiceId;
    //身份
    @Column(name = "IDENTITYS")
    private String identitys;
    //车型
    @Column(name = "models")
    private String models;
    //排量
    @Column(name = "displacement")
    private String displacement;
    //服装数码
    @Column(name = "clothing_digital")
    private String clothingDigital;
    //备注
    @Column(name = "remake")
    private String remake;

    //摩托牌照
    @Column(name = "MOTORCYCLE_LICENSE")
    private String motorcycleLicense;
    //汽车牌照
    @Column(name = "VEHICLE_LICENSE")
    private String vehicleLicense;
    //验票数字码
    @Column(name = "VERIFICATION_CODE")
    private String verificationCode;
    //验票时间
    @Column(name = "VERIFICATION_DATE")
    private String verificationDate;
    //票种名称
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    //紧急联系人
    @Column(name = "EMERGENCY_CONTACT")
    private String emergencyContact;
    //紧急联系电话
    @Column(name = "EMERGENCY_TEL")
    private String emergencyTel;
    //驾驶经验
    @Column(name = "DRIVING_EXPERIENCE")
    private String drivingExperience;




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


    //活动票种表
    @Transient
    private String state;
}