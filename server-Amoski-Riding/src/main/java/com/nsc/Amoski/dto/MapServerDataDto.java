package com.nsc.Amoski.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Data
public class MapServerDataDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //高德地图key
    private String mapKey;
    //服务id
    private String serverId;
    //服务名称
    private String serverName;
    //服务描述
    private String serverDesc;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;

    //终端id
    private String terminalId;
    //终端名称
    private String terminalName;
    //终端描述
    private String terminalDesc;

}