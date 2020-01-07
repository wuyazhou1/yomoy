package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

//活动订单表
@Entity
@Table(name = "TB_VALID_ORDER_CODE")
@Data
public class TbValidOrderCodeEntity implements  Serializable{
    //订单id

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_VALID_ORDER_CODE_SEQUENCE")
    private Integer id;
    //订单详情id
    @Column(name = "DETAIL_ID")
    private Integer detailId;
    //活动时辰安排id
    @Column(name = "SCHEDULE_TIME_ID")
    private Integer scheduleTimeId;
    //验票数字码
    @Column(name = "VALID_CODE")
    private String validCode;
    //验票状态(1.成功 2.已使用 3.已失效 4.无效)
    @Column(name = "STATE")
    private String state;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;

}