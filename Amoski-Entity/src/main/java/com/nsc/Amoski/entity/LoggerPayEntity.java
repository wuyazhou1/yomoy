package com.nsc.Amoski.entity;



import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
//付款日志表
@Entity
@Table(name = "LOGGER_PAY")
@Data
public class LoggerPayEntity  implements  Serializable{
    //付款日志id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "LOGGER_PAY_SEQUENCE")
    private Integer id;
    //日志总表id
    @Column(name = "PARENT_ID")
    private String parentId;
    //订单详情id
    @Column(name = "ORDER_DETAILS_ID")
    private String orderDetailsId;
    //订单关联类型1.活动关联类型
    @Column(name = "ASSOCIATION_TYPE")
    private String associationType;
    //退款金额
    @Column(name = "MONEY")
    private String money;
    //余额，现在为0
    @Column(name = "BALANCE")
    private String balance;

}


