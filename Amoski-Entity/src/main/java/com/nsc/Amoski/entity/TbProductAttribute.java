package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Entity
@Table(name = "TB_PRODUCT_ATTRIBUTE")
@Data
public class TbProductAttribute implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //商品类别id
    @Column(name = "CATEGORY_ID")
    public Integer categoryId;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //属性名称
    @Column(name = "ATTRIBUTE_NAME")
    public String attributeName;
    //属性值
    @Column(name = "ATTRIBUTE_VALUE")
    public String attributeValue;
    //状态(0:删除 1:正常)
    @Column(name = "STATUS")
    public String status;

}
