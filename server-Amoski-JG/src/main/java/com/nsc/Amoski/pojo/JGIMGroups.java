package com.nsc.Amoski.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 李阳
 * @date 2019/12/13 14:42
 */
@Data
@ToString
@ApiModel(value = "JGIMGroups", description = "极光IM群组对象")
public class JGIMGroups implements Serializable {

    @ApiModelProperty(value = "群主账号", name = "owner")
    private String owner;
    @ApiModelProperty(value = "群名", name = "gname")
    private String gname;
    @ApiModelProperty(value = "描述", name = "desc")
    private String desc;
    @ApiModelProperty(value = "群主头像", name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "群类型", name = "flag")
    private Integer flag;
    @ApiModelProperty(value = "成员用户名", name = "userlist")
    private String[] userlist;

}
