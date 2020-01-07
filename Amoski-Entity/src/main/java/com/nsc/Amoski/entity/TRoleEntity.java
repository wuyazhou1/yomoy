package com.nsc.Amoski.entity;




import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//null
@Entity
@Table(name = "T_ROLE")
@Data
public class TRoleEntity  implements  Serializable{
    //主键（自动生成）
    @Id
    public String id;
    //角色代码
    @Column(name = "CODE")
    public String code;
    //角色名称
    @Column(name = "ROLE_NAME")
    public String roleName;
    //是否公众角色
    @Column(name = "ISPUBLIC")
    public String ispublic;
    //部门id
    @Column(name = "ORG_CODE")
    public String orgCode;
    //是否可用0不可用，1可用
    @Column(name = "LOCKFLAG")
    public String lockflag;
    //备注
    @Column(name = "REMARK")
    public String remark;
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

}