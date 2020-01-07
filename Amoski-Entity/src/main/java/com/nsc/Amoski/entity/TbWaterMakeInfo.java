package com.nsc.Amoski.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

//水印信息表
@Entity
@Table(name = "TB_WATERMAKE_INFO")
@Data
public class TbWaterMakeInfo implements Serializable {
    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //水印图片地址
    @Column(name = "IMG_URL")
    public String imgUrl;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //部门id
    @Column(name = "DEPT_ID")
    public String deptId;
    //状态(0:禁用 1:可用)
    @Column(name = "STATUS")
    public String status;
    //水印缩略图
    @Column(name = "SMALL_IMG_URL")
    public String smallImgUrl;
    //图片基础路径
    @Column(name = "IMG_BASE_URL")
    public String imgBaseUrl;
    //水印备注
    @Column(name = "REMAKE")
    public String remake;
    //修改时间
    @Column(name = "UPDATE_TIME")
    public Timestamp updateTime;
    //修改人
    @Column(name = "UPDATE_USER")
    public String updateUser;
    //是否透明(用来显示水印图片或加到图片上)
    @Column(name = "TYPE")
    private String type;
}
