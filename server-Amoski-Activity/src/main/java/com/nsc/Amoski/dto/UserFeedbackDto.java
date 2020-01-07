package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

//用户意见反馈表
@Data
public class UserFeedbackDto  implements  Serializable{
    //主键(自动生成)
    private Integer id;
    //意见类型(关联字典表)
    private String suggestionId;
    //用户id
    private Integer memberId;
    //意见备注
    private String remake;

}