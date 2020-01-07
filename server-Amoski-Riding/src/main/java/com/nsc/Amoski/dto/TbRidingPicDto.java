package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//骑行上传所有图片
@Data
public class TbRidingPicDto implements Serializable {

    //主键(自动增长)
    public Integer id;
    //原图路径
    public String imgUrl;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;
    //小图路径
    public String smallUrl;
    //状态(1:可用)
    public String status;
    //基础路径
    public String baseUrl;
    //图片类型(1.路书介绍图)
    public Integer imgType;

}
