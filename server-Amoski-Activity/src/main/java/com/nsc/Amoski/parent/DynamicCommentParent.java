package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.PagingBeanQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


@Table(name = "DYNAMIC_COMMENT")
@Data
@ApiModel("动态评论数据")
public class DynamicCommentParent extends PagingBeanQuery implements Serializable {

    @ApiModelProperty("动态品论id")
    private String id;
    //动态id
    @ApiModelProperty("动态id")
    @MyValidator(name="dynamicId" ,ColumnName = "动态id" ,NotNull = true)
    private String dynamicId;
    //回复评论id
    @ApiModelProperty("回复评论id")
    private String replyCommentId;
    //评论内容
    @MyValidator(name="commentContent" ,ColumnName = "评论内容",NotNull = true)
    @ApiModelProperty("评论内容")
    private String commentContent;
    //评论会员id
    @MyValidator(name="memberId" ,ColumnName = "评论会员id")
    @ApiModelProperty("评论会员id")
    private String memberId;
    @ApiModelProperty("评论会员名称")
    private String memberName;
    @ApiModelProperty("评论会员头像路径")
    private String memberImages;
    //是否阅读评论0未阅读，1已阅读
    @ApiModelProperty("是否阅读评论0未阅读，1已阅读")
    private String isRead;
    //评论时间
    @ApiModelProperty("评论时间")
    private String createDate;

    @ApiModelProperty("评论时间")
    private String replyCommentCount;
    @ApiModelProperty("动态评论集合数据")
    private List<DynamicCommentParent> dynamicCommentList;

    @ApiModelProperty("回复用户id")
    private String replyMemberId;
    @ApiModelProperty("回复用户名称")
    private String replyMemberName;
    @ApiModelProperty("回复头像路径")
    private String replyMemberImages;
    //点赞评论数量
    @ApiModelProperty("点赞评论数量")
    private String likeCount;
    @ApiModelProperty("当前用户是否点赞")
    //当前用户是否点赞
    private String isLike;
}
