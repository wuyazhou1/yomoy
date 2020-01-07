package com.nsc.Amoski.entity;



import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
//动态会员屏蔽表
@Entity
@Table(name = "DYNAMIC_SHIELD")
@ApiModel("动态会员屏蔽表")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DynamicShieldEntity  implements  Serializable{
    //动态会员屏蔽id
    @Id
    @ApiModelProperty("动态会员屏蔽id")
    private Integer id;
    //当前会员id
    @Column(name = "MEMBER_ID")
    @ApiModelProperty("当前会员id")
    private Integer memberId;
    //会员屏蔽id
    @Column(name = "SHIELD_MEMBER_ID")
    @ApiModelProperty("会员屏蔽id")
    private Integer shieldMemberId;
    //屏蔽时间
    @Column(name = "CREATE_DATE")
    @ApiModelProperty("屏蔽时间")
    private Date createDate;

}