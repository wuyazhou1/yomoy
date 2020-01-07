package com.nsc.Amoski.entity;


import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//退款审核表
@Entity
@Table(name = "TB_REFUND_AUDIT")
@Data
public class TbRefundAuditEntity  implements  Serializable{
    //退款审核id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_REFUND_AUDIT_SEQUENCE")
    private Integer id;
    //订单详情id
    @Column(name = "ORDER_DETAILS_ID")
    private String orderDetailsId;
    //会员id
    @Column(name = "MEMBER_ID")
    private String memberId;
    //订单关联类型1.活动订单
    @Column(name = "ASSOCIATION_TYPE")
    private String associationType;
    //退款金额
    @Column(name = "REFUND_MONEY")
    private String refundMoney;
    //1提交退款，2审核驳回退款，3审核通过退款
    @Column(name = "STATE")
    private String state;
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

}

