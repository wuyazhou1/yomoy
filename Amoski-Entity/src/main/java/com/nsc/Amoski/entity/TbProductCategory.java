package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//商品信息表
@Entity
@Table(name = "TB_PRODUCT_CATEGORY")
@Data
public class TbProductCategory implements Serializable {
    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //商品名称
    @Column(name = "CATEGORY_NAME")
    public String categoryName;
    //商品种类
    @Column(name = "CATEGORY_DESC")
    public Integer categoryDesc;
    //商品价格
    @Column(name = "PARENT_ID")
    public Double parentId;
    //商品状态(1.上架,0.下架)
    @Column(name = "STATUS")
    public String status;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
}
