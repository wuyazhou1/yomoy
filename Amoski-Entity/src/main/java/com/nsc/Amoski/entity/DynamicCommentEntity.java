package com.nsc.Amoski.entity;

import javax.persistence.*;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

//APP动态评论表
@Entity
@Table(name = "DYNAMIC_COMMENT")
@Data
public class DynamicCommentEntity  implements  Serializable{

    @Id
    @ColumnName("动态品论id")
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "DYNAMIC_COMMENT_SEQUENCE")
    private Integer id;
    //动态id
    @Column(name = "DYNAMIC_ID")
    @ColumnName("动态id")
    private String dynamicId;
    //回复评论id
    @Column(name = "REPLY_COMMENT_ID")
    @ColumnName("回复评论id")
    private String replyCommentId;
    //评论内容
    @Column(name = "COMMENT_CONTENT")
    @ColumnName("评论内容")
    private String commentContent;
    //评论会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("评论会员id")
    private String memberId;
    //是否阅读评论0未阅读，1已阅读
    @Column(name = "IS_READ")
    @ColumnName("是否阅读评论0未阅读，1已阅读")
    private String isRead;
    //评论时间
    @Column(name = "CREATE_DATE")
    @ColumnName("评论时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @Column(name = "REPLY_MEMBER_ID")
    @ColumnName("回复用户id")
    private String replyMemberId;
}