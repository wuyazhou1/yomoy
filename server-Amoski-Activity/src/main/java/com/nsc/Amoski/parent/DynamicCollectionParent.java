package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.ColumnName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


@Data
public class DynamicCollectionParent  implements Serializable {
    private ReleaseDynamicParent ReleaseDynamicParent;
    //动态收藏id
    @MyValidator(name="id" ,ColumnName = "动态收藏id" )
    private String id;
    //动态id
    @MyValidator(name="dynamicId" ,ColumnName = "动态id" ,NotNull = true)
    private String dynamicId;
    //会员id
    @MyValidator(name="memberId" ,ColumnName = "会员id" )
    private String memberId;

    //会员名称
    public String memberName;

    //会员头像
    private String memberImages;


    //是否阅读
    @MyValidator(name="isRead" ,ColumnName = "是否阅读" )
    private String isRead;
    //创建时间
    //@MyValidator(name="createDate" ,ColumnName = "创建时间" )
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
}
