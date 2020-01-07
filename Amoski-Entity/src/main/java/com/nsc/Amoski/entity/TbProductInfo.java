package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//商品信息表
@Entity
@Table(name = "TB_PRODUCT_INFO")
@Data
public class TbProductInfo implements Serializable {
    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //商品名称
    @Column(name = "PRODUCT_NAME")
    public String productName;
    //商品种类
    @Column(name = "PRODUCT_CATEGORY")
    public Integer productCategory;
    //商品价格
    @Column(name = "PRODUCT_PRICE")
    public Double productPrice;
    //商品描述
    @Column(name = "PRODUCT_DESC")
    public String productDesc;
    //商品状态(1.上架,0.下架)
    @Column(name = "STATUS")
    public String status;
    //商品详情
    @Column(name = "PRODUCT_DETAIL")
    public String productDetail;
    //商品详情图
    @Column(name = "PRODUCT_DETAIL_IMG")
    public String productDetailImg;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //修改时间
    @Column(name = "UPDATE_TIME")
    public Timestamp updateTime;
    //修改人
    @Column(name = "UPDATE_USER")
    public String updateUser;
    //商品图
    @Column(name = "PRODUCT_IMG")
    private String productImg;
    //商品图基础路径
    @Column(name = "BASE_URL")
    private String baseUrl;
    //商品缩略图
    @Column(name = "SMALL_URL")
    private String smallUrl;
    //商品类别id
    @Column(name = "CATEGORY_ID")
    private String categoryId;
}
