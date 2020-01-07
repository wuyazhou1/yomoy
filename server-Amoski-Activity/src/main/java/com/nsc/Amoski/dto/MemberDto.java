package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//会员实体
@Data
public class MemberDto implements  Serializable{
    public Integer id;
    //会员名称
    public String name;
    //登入会员名
    public String loginname;
    //登入密码
    public String password;
    //加密密钥
    public String salt;
    //是否可用
    public String locked;
    //部门代码
    public String orgCode;
    //备注
    public String remark;
    //绑定标识备注（微信|QQ|账号|备用|备用）0代表没有绑定，1代表已经绑定
    public String bindingIdentification;
    //登入标识
    public String loginIdentification;
    //修改用户
    public String updateName;
    //修改时间
    public String updateDate;

}
