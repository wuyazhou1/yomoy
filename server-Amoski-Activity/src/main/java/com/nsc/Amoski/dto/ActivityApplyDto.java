package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//报名活动用户信息表
@Data
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
}
