package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_TEAM_PERSONNEL_INFO")
@Data
public class TbTeamPersonnelInfoEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_TEAM_PERSONNEL_INFO_SEQUENCE")
    private Integer id;
    //队伍id
    @Column(name = "team_id")
    private Integer teamId;
    //会员id
    @Column(name = "member_id")
    private Integer memberId;
    //会员名称
    @Column(name = "member_name")
    private String memberName;
    //会员头像路径
    @Column(name = "member_header_url")
    private String memberHeaderUrl;
    //队伍角色id
    @Column(name = "team_role_id")
    private Integer teamRoleId;
    //队伍角色名称
    @Column(name = "team_role_name")
    private String teamRoleName;
    //队伍角色颜色
    @Column(name = "TEAM_ROLE_COLOR")
    private String teamRoleColor;
    //加入队伍时间
    @Column(name = "create_time")
    private Timestamp createTime;

    //加入队伍地址
    @Column(name = "JOIN_ADDR")
    private String joinAddr;
    //离开队伍时间
    @Column(name = "LEAVE_TIME")
    private Timestamp leaveTime;
    //离开队伍地址
    @Column(name = "LEAVE_ADDR")
    private String leaveAddr;

}