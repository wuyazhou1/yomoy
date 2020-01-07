package com.nsc.Amoski.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_MAP_SERVER_DATA")
@Data
public class TbMapServerDataEntity implements  Serializable{

    //主键(自动增长)
    @Id
    private Integer id;
    //高德地图key
    @Column(name = "MAP_KEY")
    private String mapKey;
    //服务id
    @Column(name = "SERVER_ID")
    private String serverId;
    //服务名称
    @Column(name = "SERVER_NAME")
    private String serverName;
    //服务描述
    @Column(name = "SERVER_DESC")
    private String serverDesc;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;

}