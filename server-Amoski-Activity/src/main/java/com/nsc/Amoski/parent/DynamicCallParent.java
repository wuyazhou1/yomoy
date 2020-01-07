package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.ColumnName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class DynamicCallParent {
    //@我id
    @Id
    @MyValidator(name="id" ,ColumnName = "@我id")
    private String id;
    //动态id
    @Column(name = "DYNAMIC_ID")
    @MyValidator(name="dynamicId" ,ColumnName = "动态id")
    private String dynamicId;
    //@我会员id
    @Column(name = "MEMBER_ID")
    @MyValidator(name="memberId" ,ColumnName = "@我会员id")
    private String memberId;
    //@我会员名称
    @Column(name = "MEMBER_NAME")
    @MyValidator(name="memberName" ,ColumnName = "@我会员名称")
    private String memberName;
    //@我会员头像路径
    @Column(name = "MEMBER_IMAGE")
    @MyValidator(name="memberImage" ,ColumnName = "@我会员头像路径")
    private String memberImage;
    //是否查看@我0
    @Column(name = "IS_READ")
    @MyValidator(name="isRead" ,ColumnName = "是否查看@我")
    private String isRead;
    //@我时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @MyValidator(name="createDate" ,ColumnName = "@我时间")
    private Date createDate;
}
