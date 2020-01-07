package com.nsc.Amoski.entity;


import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

//活动订单表
@Entity
@Table(name = "TB_ACTIVITY_ORDER")
@Data
public class TbActivityOrderEntity  implements  Serializable{
    //订单id

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_ORDER_SEQUENCE")
    private Integer id;
    //订单代码
    @Column(name = "CODE")
    private String code;
    //活动id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //支付方式(1.微信 2.支付宝)
    @Column(name = "PAY_TYPE")
    private String payType;
    //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
    @Column(name = "STATE")
    private String state;
    //订单金额
    @Column(name = "PRIMITIVE_MONEY_SUM")
    private BigDecimal primitiveMoneySum;
    //订单数量
    @Column(name = "SUM_COUNT")
    private Integer sumCount;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private Timestamp createData;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;
    //联系人手机号
    @Column(name = "CONTACT_TEL")
    private String contactTel;
    //联系人姓名
    @Column(name = "CONTACT_NAME")
    private String contactName;
    //会员id
    @Column(name = "member_id")
    private Integer member_id;
    //是否提醒(0.不提醒 1.提醒)
    @Column(name = "ISREMIND")
    private String isRemind;

   /* //订单详情
    @Transient
    private List<TbActivityOrderDetailsEntity> orderDetailList;

    //订单报名人员
    @Transient
    private List<TbActivitySignUpEntity> signUpList;*/

}