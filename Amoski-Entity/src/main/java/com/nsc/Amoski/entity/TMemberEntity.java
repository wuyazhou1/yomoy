package com.nsc.Amoski.entity;


import javax.persistence.*;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

//会员表
@Entity
@Table(name = "T_MEMBER")
@Data
public class TMemberEntity  implements  Serializable{
    public TMemberEntity(MemberView memberView){
        this.setId(memberView.getId());
        this.setName(memberView.getName());
        this.setLoginname(memberView.getLoginname());
        this.setPassword(memberView.getPassword());
        this.setSalt(memberView.getSalt());
        this.setTel(memberView.getTel());
        this.setLocked(memberView.getLocked());
        this.setOrgCode(memberView.getOrgCode());
        this.setRemark(memberView.getRemark());
        this.setBindingIdentification(memberView.getBindingIdentification());
        this.setLoginIdentification(memberView.getLoginIdentification());
        this.setUpdateName(memberView.getUpdateName());
    }
    public TMemberEntity(){}

    //主键（自动生成）
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "T_MEMBER_SEQUENCE")
    public Integer id;
    //会员名称
    @Column(name = "NAME")
    public String name;
    //登入会员名
    @Column(name = "LOGINNAME")
    public String loginname;
    //登入密码
    @Column(name = "PASSWORD")
    public String password;
    //加密密钥
    @Column(name = "SALT")
    public String salt;
    //电话号码
    @Column(name = "TEL")
    public String tel;
    //是否可用
    @Column(name = "LOCKED")
    public String locked;
    //部门代码
    @Column(name = "ORG_CODE")
    public String orgCode;
    //备注
    @Column(name = "REMARK")
    public String remark;
    //绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定
    @Column(name = "BINDING_IDENTIFICATION")
    public String bindingIdentification;
    //登入标识
    @Column(name = "LOGIN_IDENTIFICATION")
    public String loginIdentification;
    //修改用户
    @Column(name = "UPDATE_NAME")
    public String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;

    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;

}
