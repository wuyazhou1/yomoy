package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//动态@我表
@Entity
@Table(name = "DYNAMIC_CALL_ME")
@Data
public class DynamicCallMeEntity  implements  Serializable{
    //@我id
    @Id
    @ColumnName("@我id")
    private String id;
    //动态id
    @Column(name = "DYNAMIC_ID")
    @ColumnName("动态id")
    private String dynamicId;
    //@我会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("@我会员id")
    private String memberId;
    //@我会员名称
    @Column(name = "MEMBER_NAME")
    @ColumnName("@我会员名称")
    private String memberName;
    //@我会员头像路径
    @Column(name = "MEMBER_IMAGE")
    @ColumnName("@我会员头像路径")
    private String memberImage;
    //是否查看@我
    @Column(name = "IS_READ")
    @ColumnName("是否查看@我")
    private String isRead;
    //@我时间
    @Column(name = "CREATE_DATE")
    @ColumnName("@我时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}