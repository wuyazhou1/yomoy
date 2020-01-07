package com.nsc.Amoski.entity.jg;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 李阳
 * @date 2019/12/12 11:35
 */
@Data
@ToString
@ApiModel(value = "JGIMUser", description = "极光IM用户对象")
public class JGIMUser implements Serializable {

    @ApiModelProperty(value = "用户名", name = "username")
    private String username;
    @ApiModelProperty(value = "密码", name = "password")
    private String password;
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;
    @ApiModelProperty(value = "头像", name = "avatar")
    private String avatar;
    @ApiModelProperty(value = "生日", name = "birthday")
    private String birthday;
    @ApiModelProperty(value = "签名", name = "signature")
    private String signature;
    @ApiModelProperty(value = "性别", name = "gender")
    private Integer gender;
    @ApiModelProperty(value = "用户所属地区", name = "region")
    private String region;
    @ApiModelProperty(value = "用户详细地址", name = "address")
    private String address;

}
