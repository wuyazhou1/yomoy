package com.nsc.Amoski.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//猎鹰服务终端信息
@Data
public class TerminalDataDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //用户id
    private Integer memberId;
    //终端id
    private String terminalId;
    //服务id
    private String serverId;
    //终端名称
    private String terminalName;
    //终端描述
    private String terminalDesc;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;


}