package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//app动态收藏表
@Entity
@Table(name = "DYNAMIC_COLLECTION")
@Data
public class DynamicCollectionEntity  implements  Serializable{
    //动态收藏id
    @Id
    @ColumnName("动态收藏id")
    private String id;
    //动态id
    @Column(name = "DYNAMIC_ID")
    @ColumnName("动态id")
    private String dynamicId;
    //会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("会员id")
    private String memberId;
    //是否阅读
    @Column(name = "IS_READ")
    @ColumnName("是否阅读")
    private String isRead;
    //创建时间
    @Column(name = "CREATE_DATE")
    @ColumnName("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}