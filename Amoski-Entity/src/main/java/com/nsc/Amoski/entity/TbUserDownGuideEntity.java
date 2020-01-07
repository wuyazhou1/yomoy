package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户下载路书表
@Entity
@Table(name = "TB_USER_DOWN_GUIDE")
@Data
public class TbUserDownGuideEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_USER_DOWN_GUIDE_SEQUENCE")
    private Integer id;
    //用户id
    @Column(name = "MEMBER_ID")
    private Integer memberId;
    //路书id
    @Column(name = "GUIDE_ID")
    private Integer guideId;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
}