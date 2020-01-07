package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//水印信息表
@Data
public class BrandSeriesDto implements Serializable {

    //主键(自动增长)
    public Integer id;
    //系列名称
    public Integer seriesName;
    //系列英文名
    public Integer seriesEnglishName;
    //创建时间
    public Timestamp createTime;
    //创建人
    public String createUser;
    //品牌id
    public String brandId;
}
