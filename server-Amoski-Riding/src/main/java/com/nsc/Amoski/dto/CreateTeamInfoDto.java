package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.TbTeamPersonnelInfoEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//骑行组队信息表
@Data
public class CreateTeamInfoDto implements  Serializable{
    private static final long serialVersionUID = 12121323232L;

    //主键(自动增长)
    private Integer id;
    //队伍名称
    private String teamName;
    //队伍加入口令
    private String teamCode;
    //队长
    private Integer teamer;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //状态(0:失效 1:有效)
    private String status;
    //有效时间(天)
    private Integer validDay;
    //有效截止时间
    private Timestamp validEndTime;

    //队员人员信息
    private List<TbTeamPersonnelInfoEntity> list;

    //加入队伍地址
    private String joinAddr;
    //离开队伍时间
    private Timestamp leaveTime;
    //离开队伍地址
    private String leaveAddr;

    //队伍最大人数
    private Integer teamMaxCount;

    //redis队伍数据结构
    private Map<String,TeamPersonnelInfoDto> personMap=new HashMap<String,TeamPersonnelInfoDto>();

    //队员人员信息
    private List<TeamPersonnelInfoDto> dtoList=new ArrayList<TeamPersonnelInfoDto>();

    //导航信息
    private String navigationPoint;
}