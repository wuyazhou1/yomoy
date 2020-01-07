package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//退款审核表
@Data
public class RefundAuditDto implements  Serializable{
    //退款审核id
    private Integer id;
    //订单详情id
    private String orderDetailsId;
    //会员id
    private String memberId;
    //订单关联类型1.活动订单
    private String associationType;
    //退款金额
    private String refundMoney;
    //1提交退款，2审核驳回退款，3审核通过退款
    private String state;
    //创建人
    private String createName;
    //创建时间
    private Timestamp createData;
    //修改人
    private String updateName;
    //修改时间
    private Timestamp updateDate;

    //详情字段

}

