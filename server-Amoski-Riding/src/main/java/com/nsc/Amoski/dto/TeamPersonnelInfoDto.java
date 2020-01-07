package com.nsc.Amoski.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//组队人员信息表
@Data
public class  TeamPersonnelInfoDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //队伍id
    private Integer teamId;
    //会员id
    private Integer memberId;
    //会员名称
    private String memberName;
    //会员头像路径
    private String memberHeaderUrl;
    //队伍角色id
    private Integer teamRoleId;
    //队伍角色名称
    private String teamRoleName;
    //队伍角色颜色
    private String teamRoleColor;
    //加入队伍时间
    private Timestamp createTime;

    //加入队伍地址
    private String joinAddr;
    //离开队伍时间
    private Timestamp leaveTime;
    //离开队伍地址
    private String leaveAddr;

    //队伍口令
    private String teamCode;

    //状态
    private String status;

    //最后一次上传点数据
    private String lastPoint;
}