package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//水印信息表
@Data
public class UserPhotoDto implements Serializable {
    private static final long serialVersionUID= 97890872645312L;
    //主键(自动增长)
    public Integer id;
    //相册名称
    public String photoName;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;
    //会员id
    public String memerId;
    //状态(0:禁用 1:可用)
    public String status;
    //图片基础地址
    private String baseUrl;
    //图片小图地址
    private String smallUrl;
}
