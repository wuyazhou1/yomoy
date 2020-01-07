package com.nsc.Amoski.parent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("动态数据类")
public class ReleaseDynamicParent extends PagingBeanQuery implements Serializable {

    private ReleaseDynamicParent parentDynamin;
    //动态id
    @Id
    @ApiModelProperty(value="动态id",required = true,dataType = "动态id")
    private String id;
    //上级动态id
    @Column(name = "PARENT_DYNAMIN_ID")
    @ApiModelProperty("上级动态id")
    private String parentDynaminId;
    //动态类型,1相册类型,2视频类型
    @Column(name = "TYPE")
    @MyValidator(name = "type",ColumnName="动态类型,1相册类型,2视频类型,3发布活动动态,4发布路书活动",NotNull = true,min = 0)
    @ApiModelProperty("动态类型,1相册类型,2视频类型,3发布活动动态,4发布路书活动")
    private String type;

    //关联类型id
    @Column(name = "RELATION_TYPE_ID")
    @ColumnName("关联类型id")
    @ApiModelProperty("关联类型id")
    private String relationTypeId;

    //评分排序字段
    @Column(name = "SCORE")
    @MyValidator(name ="score" ,ColumnName="评分排序字段")
    @ApiModelProperty("评分排序字段")
    private String score;
    //会员id
    @Column(name = "MEMBER_ID")
    @JsonProperty(value = "memberId")
    @ApiModelProperty("会员id")
    private String memberId;

    //会员名称
    @Column(name = "MEMBER_NAME")
    @ApiModelProperty("会员名称")
    private String memberName;
    //会员头像
    @Column(name = "MEMBER_IMAGE_URL")
    @ApiModelProperty("会员头像")
    private String memberImageUrl;
    //发布地址
    @Column(name = "RELEASE_ADDRESS")
    @MyValidator(name="releaseAddress" ,ColumnName = "发布地址")
    @ApiModelProperty("发布地址")
    private String releaseAddress;
    //发布内容
    @Column(name = "PUBLISH_CONTENT")
    //@MyValidator(name="publishContent" ,ColumnName = "发布内容", NotNull = true, lengthMax = 140)
    @ApiModelProperty("发布内容")
    private String publishContent;
    //发布位置经度
    @Column(name = "Y_AXIS")
    @MyValidator(name="yAxis" ,ColumnName = "发布位置经度", NotNull = true, min=1)
    @ApiModelProperty("发布位置经度")
    private String yAxis;
    //发布位置纬度
    @Column(name = "X_AXIS")
    @MyValidator(name="xAxis" ,ColumnName = "发布位置纬度", NotNull = true, min=1)
    @ApiModelProperty("发布位置纬度")
    private String xAxis;
    //状态1为公开状态，2为私有状态
    @Column(name = "STATE")
    @MyValidator(name="state" ,ColumnName = "状态", NotNull = true, min=1)
    @ApiModelProperty("状态")
    private String state;
    //创建时间
    @Column(name = "CREATE_DATE")
    @MyValidator(name="createDate" ,ColumnName = "创建时间")
    @ApiModelProperty("创建时间")
    private String createDate;

    @MyValidator(name="typeTitle" ,ColumnName = "关联类型标题")
    @ApiModelProperty("关联类型标题")
    private String typeTitle;
    @MyValidator(name="typeImage" ,ColumnName = "关联类型图片")
    @ApiModelProperty("关联类型图片")
    private String typeImage;
    @MyValidator(name="typeImage" ,ColumnName = "关联类型描述")
    @ApiModelProperty("关联类型描述")
    private String typeDesc;

    @ApiModelProperty("app动态评论总数")
    private String commentCount;
    @ApiModelProperty("app动态收藏总数")
    private String collectionCount;
    @ApiModelProperty("app动态点赞总数")
    private String fabulousCount;



    //修改时间
    @Column(name = "UPDATE_DATE")
    @MyValidator(name="updateDate" ,ColumnName = "修改时间")
    @ApiModelProperty("修改时间")
    private String updateDate;


    // 是否已经点赞
    @ApiModelProperty("是否已经点赞")
    private String isLike;
    // 是否已经收藏
    @ApiModelProperty("是否已经收藏")
    private String hasCollection;
    //是否关注
    @ApiModelProperty("是否关注")
    private String followed;
    //距离
    @ApiModelProperty("距离")
    private String sqrtvalue;


    private String distance;

    @ApiModelProperty("是否保存相册，0不保存相册，1保存相册")
    private String saveAlbum;


    @ApiModelProperty("动态图片集合")
    private List<DynamicImageEntity> dynamicImageList;

    //@MyValidator(name="mentionListId" ,ColumnName = "@我的会员id，以逗号隔开")
    @ApiModelProperty("@我的会员id，以逗号隔开")
    private String mentionListId;


    @ApiModelProperty("@我的会员id，以逗号隔开")
    private List<DynamicSpotFabulousParent> DynamicSpotFabulousList;
}
