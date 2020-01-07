package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Entity
@Table(name = "TB_PHOTO_PIC")
@Data
public class TbPhotoPic implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //原图路径
    @Column(name = "IMG_URL")
    public String imgUrl;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //小图路径
    @Column(name = "SMALL_URL")
    public String smallUrl;
    //状态(1:可用)
    @Column(name = "STATUS")
    public String status;
    //基础路径
    @Column(name = "BASE_URL")
    public String baseUrl;
    //相册id
    @Column(name = "PHOTO_ID")
    public Integer photoId;
    //用户id
    @Column(name = "MEMBER_ID")
    public Integer memberId;
    //图片类型(1.相册 2.轨迹图 3.路书图片 4.骑行中途拍照图片)
    @Column(name = "IMG_TYPE")
    public Integer imgType;

}
