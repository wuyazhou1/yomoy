package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//活动订单表
@Entity
@Table(name = "TB_ACTIVITY_ORDER_DETAILS")
@Data
public class TbActivityOrderDetailsEntity implements  Serializable{

    //订单详细id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_ORDER_DETAIL_SEQUENCE")
    private Integer id;
    //订单代码
    @Column(name = "CODE")
    private String code;
    /*//领取码
    @Column(name = "RECEIVING_CODE")
    private String receivingCode;*/
    //订单id
    @Column(name = "ORDER_ID")
    private String orderId;
    //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
    @Column(name = "STATE")
    private String state;
    //订单金额
    @Column(name = "PRIMITIVE_MONEY")
    private BigDecimal primitiveMoney;
    //订单总金额
    @Column(name = "PRIMITIVE_MONEY_SUM")
    private BigDecimal primitiveMoneySum;
    //订单数量
    @Column(name = "SUM_COUNT")
    private String sumCount;
    //使用时间
    @Column(name = "APPLY_DATE")
    private Timestamp applyDate;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private Timestamp updateDate;

    //票种id
    @Column(name = "INVOICE_ID")
    private String invoiceId;
    //票种名称
    @Column(name = "INVOICE_NAME")
    private String invoiceName;
    //报名人id
    @Column(name = "SIGN_UP_ID")
    private String signUpId;
    //退票时间
    @Column(name = "REFUND_TICKET_DATE")
    private Timestamp refundTicketDate;
    //同意退款时间
    @Column(name = "AGREE_DATE")
    private Timestamp agreeDate;
}