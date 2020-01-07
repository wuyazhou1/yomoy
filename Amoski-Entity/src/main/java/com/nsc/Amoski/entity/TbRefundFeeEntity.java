package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//活动退款费率表
@Entity
@Table(name = "TB_REFUND_FEE")
@Data
public class TbRefundFeeEntity implements  Serializable{
    //订单id

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_REFUND_FEE_SEQUENCE")
    private Integer id;
    //订单代码
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动id
    @Column(name = "DAY")
    private String day;
    //支付方式(1.微信 2.支付宝)
    @Column(name = "FEE")
    private String fee;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATE")
    private Timestamp createDate;

}