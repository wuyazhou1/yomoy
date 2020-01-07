package com.nsc.Amoski.dto;



import java.io.Serializable;
import java.sql.Timestamp;

//报名活动用户信息表
public class ActivityApplyDto implements Serializable {
    //主键(自动增长)
    public Integer id;
    //用户姓名
    public String name;
    //用户手机
    public String mobile;
    //报名人数
    public String count;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;

    @Override
    public String toString() {
        return "ActivityApplyDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", count='" + count + '\'' +
                ", createTime=" + createTime +
                ", createUser='" + createUser + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}
