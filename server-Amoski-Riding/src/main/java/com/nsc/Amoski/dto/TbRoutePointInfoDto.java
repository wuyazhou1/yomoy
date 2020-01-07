package com.nsc.Amoski.dto;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

//途径点信息
@Data
public class TbRoutePointInfoDto implements Serializable {

    //主键(自动增长)
    public String id;
    //途径点名称
    private String route_name     ;
    //预计骑行时间
    private String ridingtime     ;
    //预计里程
    private String aboutdis       ;
    //路线小图标
    private String routeIcon     ;
    //途径点备注
    private String remake         ;
    //路线id
    private String routeId       ;
    //点类型
    private String type;
    //结束地点经度
    private String lng    ;
    //结束地点纬度
    private String lat       ;
    //图片路径
    private String imgUrl       ;
    //图片基础路径
    private String baseUrl       ;
    //当前点到下一个点的线路描述
    private String linedesc       ;

}
