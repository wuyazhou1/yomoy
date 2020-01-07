package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//用户消息表
@Data
public class UsreMessageDto extends PageDto implements  Serializable{
    private static final long serialVersionUID= 978976290645312L;
    //主键(自动生成)
    private Integer id;
    //用户id
    private Integer memberId;
    //消息类型(1.系统消息 2.个人数据消息)
    private String msgType;
    //消息内容
    private String msgContent;
    //消息图片
    private String msgImg;
    //消息详情路径
    private String msgDetailUrl;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //状态(1.已读 0.未读)
    private String status;
    //消息标题
    private String msg_title;
}