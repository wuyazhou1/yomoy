package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.PagingBeanQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "DYNAMIC_SPOT_FABULOUS")
@Data
@ApiModel("动态点赞数据")
public class DynamicSpotFabulousParent extends PagingBeanQuery implements Serializable {

    @ApiModelProperty("动态数据")
    private ReleaseDynamicParent ReleaseDynamicParent;
    @ApiModelProperty("动态评论数据")
    private DynamicCommentParent DynamicCommentParent;


    //评论id
    @ApiModelProperty("评论id")
    private String commentId;//COMMENT_ID
    //点赞id
    @ApiModelProperty("点赞id")
    private String id;
    //动态id
    @ApiModelProperty("动态id")
    private String dynamicId;
    //点赞会员id
    @ApiModelProperty("点赞会员id")
    private String memberId;
    //是否查看点赞
    @ApiModelProperty("是否查看点赞")
    private String isRead;
    //点赞时间
    @ApiModelProperty("点赞时间")
    private String createDate;

    @ApiModelProperty("点赞会员名称")
    public String memberName;

    @ApiModelProperty("点赞会员头像路径")
    private String memberImages;

}
