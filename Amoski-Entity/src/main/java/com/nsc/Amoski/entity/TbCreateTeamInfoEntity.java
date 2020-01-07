package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_CREATE_TEAM_INFO")
@Data
public class TbCreateTeamInfoEntity implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_CREATE_TEAM_INFO_SEQUENCE")
    private Integer id;
    //队伍名称
    @Column(name = "TEAM_NAME")
    private String teamName;
    //队伍加入口令
    @Column(name = "TEAM_CODE")
    private String teamCode;
    //队长
    @Column(name = "TEAMER")
    private Integer teamer;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //状态(0:失效 1:有效)
    @Column(name = "STATUS")
    private String status;
    //有效时间(天)
    @Column(name = "VALID_DAY")
    private Integer validDay;
    //有效截止时间
    @Column(name = "VALID_END_TIME")
    private Timestamp validEndTime;

}