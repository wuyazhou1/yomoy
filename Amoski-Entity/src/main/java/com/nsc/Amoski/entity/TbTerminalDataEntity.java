package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_TERMINAL_DATA")
@Data
public class TbTerminalDataEntity implements  Serializable{

    //主键(自动增长)
    @Id
    private Integer id;
    //用户id
    @Column(name = "MEMBER_ID")
    private Integer memberId;
    //终端id
    @Column(name = "TERMINAL_ID")
    private String terminalId;
    //服务id
    @Column(name = "SERVER_ID")
    private String serverId;
    //终端名称
    @Column(name = "TERMINAL_NAME")
    private String terminalName;
    //终端描述
    @Column(name = "TERMINAL_DESC")
    private String terminalDesc;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;


}