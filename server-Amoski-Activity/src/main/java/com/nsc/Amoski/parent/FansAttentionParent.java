package com.nsc.Amoski.parent;

import com.nsc.Amoski.config.MyValidator;
import com.nsc.Amoski.entity.ColumnName;
import com.nsc.Amoski.entity.PagingBeanQuery;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


@Data
public class FansAttentionParent extends PagingBeanQuery implements Serializable{
    //是否关注
    private String followed;
    //粉丝id
    @Id
    @MyValidator(name="id" ,ColumnName = "粉丝id")
    private String id;
    //关注会员id
    @Column(name = "MEMBER_ID")
    @MyValidator(name="memberId" ,ColumnName = "关注会员id")
    private String memberId;
    //被关注会员id
    @Column(name = "FANS_MEMBER_ID")
    @MyValidator(name="fansMemberId" ,ColumnName = "被关注会员id" ,NotNull = true)
    private String fansMemberId;
    //关注时间
    @Column(name = "CREATE_DATE")
    @ColumnName("关注时间")
    private String createDate;

    public String memberName;
    public String memberImage;
}
