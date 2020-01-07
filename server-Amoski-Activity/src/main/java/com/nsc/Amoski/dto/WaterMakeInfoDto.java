package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.PageDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//水印信息表
@Data
public class WaterMakeInfoDto extends PageDto implements Serializable {
    //主键(自动增长)
    private Integer id;
    //水印图片地址
    private String imgUrl;
    //创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    //创建人
    private String createUser;
    //部门id
    private String deptId;
    //状态(0:禁用 1:可用)
    private String status;
    //缩略图路径
    private String smallImgUrl;
    //图片基础路径
    private String imgBaseUrl;
    //状态(0:禁用 1:可用)
    private String remake;
    //修改时间
    private Timestamp updateTime;
    //修改人
    private String updateUser;
    //是否透明(用来显示水印图片或加到图片上)
    private String type;

}
