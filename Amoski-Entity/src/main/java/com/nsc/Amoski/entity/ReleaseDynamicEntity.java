package com.nsc.Amoski.entity;

import javax.persistence.*;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

//动态表
@Entity
@Table(name = "RELEASE_DYNAMIC")
@Data
public class ReleaseDynamicEntity  implements  Serializable{

    //动态id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "RELEASE_DYNAMIC_SEQUENCE")
    @ColumnName("动态id")
    private Integer id;
    //关联类型id
    @Column(name = "RELATION_TYPE_ID")
    @ColumnName("关联类型id")
    private String relationTypeId;
    //上级动态id
    @Column(name = "PARENT_DYNAMIN_ID")
    @ColumnName("上级动态id")
    private String parentDynaminId;
    //动态类型,1相册类型,2视频类型
    @Column(name = "TYPE")
    @ColumnName("动态类型,1相册类型,2视频类型")
    private String type;
    //会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("会员id")
    private String memberId;
    //会员名称
    @Column(name = "MEMBER_NAME")
    @ColumnName("会员名称")
    private String memberName;
    //会员头像
    @Column(name = "MEMBER_IMAGE_URL")
    @ColumnName("会员头像")
    private String memberImageUrl;
    //发布地址
    @Column(name = "RELEASE_ADDRESS")
    @ColumnName("发布地址")
    private String releaseAddress;
    //发布内容
    @Column(name = "PUBLISH_CONTENT")
    @ColumnName("发布内容")
    private String publishContent;
    //发布位置经度
    @Column(name = "Y_AXIS")
    @ColumnName("发布位置经度")
    private String yAxis;
    //发布位置纬度
    @Column(name = "X_AXIS")
    @ColumnName("发布位置纬度")
    private String xAxis;
    //评分排序字段
    @Column(name = "SCORE")
    @ColumnName("评分排序字段")
    private String score;
    //状态1为公开状态，2为私有状态
    @Column(name = "STATE")
    @ColumnName("状态1为公开状态，2为私有状态")
    private String state;
    //创建时间
    @Column(name = "CREATE_DATE")
    @ColumnName("创建时间")
    private Date createDate;
    //修改时间
    @Column(name = "UPDATE_DATE")
    @ColumnName("修改时间")
    private Date updateDate;


}
