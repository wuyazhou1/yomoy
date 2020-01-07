package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

//商品信息表
@Data
public class ProductInfoDto extends PageDto implements Serializable {
    private static final long serialVersionUID= 9789645389612L;
    //主键(自动增长)
    private Integer id;
    //商品名称
    private String productName;
    //商品种类
    private Integer productCategory;
    //商品价格
    private Double productPrice;
    //商品描述
    private String productDesc;
    //商品状态(1.上架,0.下架)
    private String status;
    //商品详情
    private String productDetail;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //修改时间
    private Timestamp updateTime;
    //修改人
    private String updateUser;
    //商品图
    private String productImg;
    //商品图基础路径
    private String baseUrl;
    //商品缩略图
    private String smallUrl;

    //条件查询商品价格区间
    private String wherePrice;
}
