package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//骑行上传所有图片
@Entity
@Table(name = "TB_RIDING_PIC")
@Data
public class TbRidingPicEntity implements Serializable {

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
    //图片类型(1.路书介绍图)
    @Column(name = "IMG_TYPE")
    public Integer imgType;

}
