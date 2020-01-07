package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//粉丝关注表
@Entity
@Table(name = "FANS_ATTENTION")
@Data
public class FansAttentionEntity   implements  Serializable{
    //粉丝id
    @Id
    @ColumnName("粉丝id")
    private String id;
    //关注会员id
    @Column(name = "MEMBER_ID")
    @ColumnName("关注会员id")
    private String memberId;
    //被关注会员id
    @Column(name = "FANS_MEMBER_ID")
    @ColumnName("被关注会员id")
    private String fansMemberId;
    //关注时间
    @Column(name = "CREATE_DATE")
    @ColumnName("关注时间")
    private Date createDate;

}