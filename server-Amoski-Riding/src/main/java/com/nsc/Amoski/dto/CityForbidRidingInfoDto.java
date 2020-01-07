package com.nsc.Amoski.dto;

import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Data
public class CityForbidRidingInfoDto extends PageDto implements  Serializable{

    //主键(自动增长)
    private Integer id;
    //城市代码
    private String cityCode;
    //城市名称
    private String cityName;
    //禁止区域个数
    private Integer forbidAreaCount;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //政策描述
    private String policyDesc;
    //修改时间
    private Timestamp updateTime;
    //修改人
    private String updateUser;
    //状态(0:消禁 1:禁止)
    private String status;

    //所有城市
    private String citys;
}