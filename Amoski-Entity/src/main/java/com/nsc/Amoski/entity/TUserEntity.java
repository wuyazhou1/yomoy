package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//用户表
@Entity
@Table(name = "T_USER")
@Data
public class TUserEntity  implements  Serializable{
    //创建用户
    @Column(name = "CREATE_NAME")
    public String createName;
    //创建时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;
    //修改用户
    @Column(name = "UPDATE_NAME")
    public String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;
    //主键（自动生成）
    @Id
    public String id;
    //用户代码
    @Column(name = "CODE")
    public String code;
    //用户名称
    @Column(name = "NAME")
    public String name;
    //登入用户名
    @Column(name = "LOGINNAME")
    public String loginname;
    //登入密码
    @Column(name = "PASSWORD")
    public String password;
    //加密密钥
    @Column(name = "SALT")
    public String salt;
    //是否可用1可用   ,0不可用
    @Column(name = "LOCKED")
    public String locked;
    //部门代码
    @Column(name = "ORG_CODE")
    public String orgCode;
    //备注
    @Column(name = "REMARK")
    public String remark;

}
