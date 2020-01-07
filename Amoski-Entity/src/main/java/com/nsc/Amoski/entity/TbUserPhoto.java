package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Entity
@Table(name = "TB_USER_PHOTO")
@Data
public class TbUserPhoto implements Serializable {
    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    //相册名称
    @Column(name = "PHOTO_NAME")
    private String photoName;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //会员id
    @Column(name = "MEMBER_ID")
    private Integer memerId;
    //状态(0:禁用 1:可用)
    @Column(name = "STATUS")
    private String status;
}
