package com.nsc.Amoski.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//组队角色信息表
@Data
public class TeamRoleDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //角色名称
    private String roleName;
    //角色颜色
    private String roleColor;
    //角色描述
    private String roleDesc;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;

}