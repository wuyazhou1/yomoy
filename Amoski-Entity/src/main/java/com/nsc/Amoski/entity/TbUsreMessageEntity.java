package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//用户消息表
@Entity
@Table(name = "TB_USRE_MESSAGE")
@Data
public class TbUsreMessageEntity  implements  Serializable{
    //主键(自动生成)
    @Id
    public Integer id;
    //用户id
    @Column(name = "MEMBER_ID")
    public Integer memberId;
    //消息类型(1.系统消息 2.个人数据消息)
    @Column(name = "MSG_TYPE")
    public String msgType;
    //消息内容
    @Column(name = "MSG_CONTENT")
    public String msgContent;
    //消息图片
    @Column(name = "MSG_IMG")
    public String msgImg;
    //消息详情路径
    @Column(name = "MSG_DETAIL_URL")
    public String msgDetailUrl;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //状态
    @Column(name = "STATUS")
    public String status;
    //消息标题
    @Column(name = "MSG_TITLE")
    public String msg_title;

}