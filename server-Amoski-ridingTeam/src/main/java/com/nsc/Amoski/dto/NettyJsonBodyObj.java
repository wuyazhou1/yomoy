package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.TbTeamRoleEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NettyJsonBodyObj {

    private String token;//请求类型

    private String teamName;//队伍名称

    private String status;//修改状态

    private String longitude;//经度
    private String latitude;//纬度

    private String memberName;//队员名称

    private String memberHeaderUrl;//队员头像

    private String joinAddr;//队员加入地址

    private String userIds;//所有删除userIds

    private String roleId;//角色id

    private TeamPersonnelInfoDto pesonInfo;//队员信息

    private List<TbTeamRoleEntity> roleList;//所有角色

    //队员人员信息
    private List<TeamPersonnelInfoDto1> dtoList;

    private CreateTeamInfoDto redisData;//队伍对象

    //导航信息
    private String navigationPoint;
}
