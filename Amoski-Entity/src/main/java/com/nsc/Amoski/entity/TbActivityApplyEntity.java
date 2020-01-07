package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//报名活动用户信息表
@Entity
@Table(name = "TB_ACTIVITY_APPLY")
@Data
public class TbActivityApplyEntity implements Serializable {
    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //用户姓名
    @Column(name = "NAME")
    public String name;
    //用户手机
    @Column(name = "MOBILE")
    public String mobile;
    //报名人数
    @Column(name = "COUNT")
    public String count;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
}
