package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Data
public class ProductAttributeDto implements Serializable {

    //主键(自动增长)
    public Integer id;
    //商品类别id
    public Integer categoryId;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;
    //属性名称
    public String attributeName;
    //属性值
    public String attributeValue;
    //状态(0:删除 1:正常)
    public String status;
}
