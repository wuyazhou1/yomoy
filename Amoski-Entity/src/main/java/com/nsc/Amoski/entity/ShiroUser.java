package com.nsc.Amoski.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ShiroUser implements Serializable {
    private static final long serialVersionUID = 4125096758372084309L;
    /**
     *   ShiroUser suser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
     */
    public Integer id;
    public String code;
    public String name;
    public String loginName;
    public String password;
    private String orgCode;
    private String isUser;//0用户，1会员
    private String salt;

    public ShiroUser(){}

    public ShiroUser(TUserEntity user) {
        this.setId(Integer.parseInt(user.getId()));
        this.setCode(user.getCode());
        this.setName(user.getName());
        this.setLoginName(user.getLoginname());
        this.setOrgCode(user.getOrgCode());
        this.setIsUser("0");
        this.setPassword(user.getPassword());
    }

    public ShiroUser(MemberView user) {
        this.setId(user.getId());
        this.setCode(user.getId()+"");
        this.setName(user.getName());
        this.setLoginName(user.getLoginname());
        this.setOrgCode(user.getOrgCode());
        this.setIsUser("1");
    }

    @Override
    public String toString() {
        return id+","+loginName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShiroUser user = (ShiroUser) o;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (code != null ? !code.equals(user.code) : user.code != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (loginName != null ? !loginName.equals(user.loginName) : user.loginName != null) return false;
        if (orgCode != null ? !orgCode.equals(user.orgCode) : user.orgCode != null) return false;
        if (isUser != null ? !isUser.equals(user.isUser) : user.isUser != null) return false;
        if (salt != null ? !salt.equals(user.salt) : user.salt != null) return false;
        return true;
    }

    @Override
    public int hashCode() {

        Integer result = (Integer) (id ^ (id >>> 32));
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (loginName != null ? loginName.hashCode() : 0);
        result = 31 * result + (orgCode != null ? orgCode.hashCode() : 0);
        result = 31 * result + (isUser != null ? isUser.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        return result;
    }

    //验证盐
    public String getCredentialsSalt() {
        return loginName + salt;
    }
    public void setCredentialsSalt(String star) {

    }

}
