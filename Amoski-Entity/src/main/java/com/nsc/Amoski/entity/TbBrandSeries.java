package com.nsc.Amoski.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Entity
@Table(name = "TB_PRODUCT_ATTRIBUTE")
@Data
public class TbBrandSeries implements Serializable {

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer id;
    //系列名称
    @Column(name = "SERIES_NAME")
    public Integer seriesName;
    //系列英文名
    @Column(name = "SERIES_ENGLISH_NAME")
    public Integer seriesEnglishName;
    //创建时间
    @Column(name = "CREATE_TIME")
    public Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    public String createUser;
    //品牌id
    @Column(name = "BRAND_ID")
    public String brandId;

}
