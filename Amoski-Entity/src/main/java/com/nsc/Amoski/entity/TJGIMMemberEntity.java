package com.nsc.Amoski.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 李阳
 * @date 2019/12/16 17:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TJGIMMemberEntity implements Serializable {

    //主键
    private String id;
    //外键用户表id
    private Integer member_id;
    //用户名
    private String username;
    //密码
    private String password;
    //时间
    private String ctime;

}
