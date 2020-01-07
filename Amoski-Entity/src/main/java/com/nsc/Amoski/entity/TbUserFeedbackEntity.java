package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//用户意见反馈表
@Entity
@Table(name = "TB_USER_FEEDBACK")
@Data
public class TbUserFeedbackEntity  implements  Serializable{
    //主键(自动生成)
    @Id
    private Integer id;
    //意见类型(关联字典表)
    @Column(name = "SUGGESTION_ID")
    private String suggestionId;
    //用户id
    @Column(name = "MEMBER_ID")
    private Integer memberId;
    //意见备注
    @Column(name = "REMAKE")
    private String remake;

}