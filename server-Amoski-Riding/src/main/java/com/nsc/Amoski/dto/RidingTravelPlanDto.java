package com.nsc.Amoski.dto;


import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//骑行路书数据
@Data
public class RidingTravelPlanDto  implements  Serializable{
    //主键(自动增长)
    private Integer id;
    //路书图片id
    private String imgId;
    //描述
    private String ridingDesc;
    //经度
    private String lgt;
    //纬度
    private String lat;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //骑行id
    private Integer ridingId;

}