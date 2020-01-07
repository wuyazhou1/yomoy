package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import lombok.Data;


@Data
public class CommentSpotFabulousParent {
    //点赞id
    @MyValidator(name="id" ,ColumnName = "点赞id")
    private String id;
    //评论id
    @MyValidator(name="commentId" ,ColumnName = "评论id",NotNull = true)
    private String commentId;
    //点赞会员id
    @MyValidator(name="memberId" ,ColumnName = "点赞会员id")
    private String memberId;
    //是否查看点赞
    @MyValidator(name="isRead" ,ColumnName = "是否查看点赞")
    private String isRead;
    //点赞时间
    @MyValidator(name="createDate" ,ColumnName = "点赞时间")
    private String createDate;
}
