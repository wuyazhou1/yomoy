package com.nsc.Amoski.dto;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//商品信息表
@Data
public class ProductCategoryDto implements Serializable {
    //主键(自动增长)
    public Integer id;
    //商品名称
    public String categoryName;
    //商品种类
    public Integer categoryDesc;
    //商品价格
    public Double parentId;
    //商品状态(1.上架,0.下架)
    public String status;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;
}
