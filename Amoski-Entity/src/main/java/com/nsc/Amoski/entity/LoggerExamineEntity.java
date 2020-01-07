package com.nsc.Amoski.entity;


import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
//退款审核日志表
@Entity
@Table(name = "LOGGER_EXAMINE")
@Data
public class LoggerExamineEntity  implements  Serializable{
    //日志总id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "LOGGER_EXAMINE_SEQUENCE")
    private Integer id;
    //日志总表id
    @Column(name = "PARENT_ID")
    private String parentId;
    //订单详情id
    @Column(name = "ORDER_DETAILS_ID")
    private String orderDetailsId;
    //退款审核id
    @Column(name = "REFUND_AUDIT_ID")
    private String refundAuditId;
    //退款金额
    @Column(name = "MONEY")
    private String money;
    //余额，现在为0
    @Column(name = "BALANCE")
    private String balance;
    //订单关联类型1活动订单
    @Column(name = "ASSOCIATION_TYPE")
    private String associationType;

}


