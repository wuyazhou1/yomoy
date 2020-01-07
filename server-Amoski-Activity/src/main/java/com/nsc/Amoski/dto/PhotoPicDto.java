package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//水印信息表
@Data
public class PhotoPicDto extends PageDto implements Serializable {
    private static final long serialVersionUID= 97896288345312L;
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
    //相册id
    public Integer photoId;
    //用户id
    public Integer memberId;
    //图片类型(1.相册 2.骑行中途拍照图片 3.路书图片)
    public Integer imgType;

    //删除的图片id
    public String ids;
}
