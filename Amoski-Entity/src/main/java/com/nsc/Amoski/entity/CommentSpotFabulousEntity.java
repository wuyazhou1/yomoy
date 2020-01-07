package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//评论点赞表
@Entity
@Table(name = "COMMENT_SPOT_FABULOUS")
@Data
public class CommentSpotFabulousEntity  implements  Serializable{
    //点赞id
    @Id
    @ColumnName("点赞id")
    private String id;
    //评论id
    @Column(name = "COMMENT_ID")
    @ColumnName("评论id")
    private String commentId;
    //点赞会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("点赞会员id")
    private String memberId;
    //是否查看点赞
    @Column(name = "IS_READ")
    @ColumnName("是否查看点赞")
    private String isRead;
    //点赞时间
    @Column(name = "CREATE_DATE")
    @ColumnName("点赞时间")
    private Date createDate;

}
