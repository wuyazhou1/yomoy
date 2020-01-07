package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//组队角色信息表
@Entity
@Table(name = "TB_TEAM_ROLE")
@Data
public class TbTeamRoleEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_TEAM_ROLE_SEQUENCE")
    private Integer id;
    //角色名称
    @Column(name = "ROLE_NAME")
    private String roleName;
    //角色颜色
    @Column(name = "ROLE_COLOR")
    private String roleColor;
    //角色描述
    @Column(name = "ROLE_DESC")
    private String roleDesc;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;

}