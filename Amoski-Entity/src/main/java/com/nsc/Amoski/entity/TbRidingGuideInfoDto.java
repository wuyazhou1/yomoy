package com.nsc.Amoski.entity;


import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

//骑行路书信息表
@Data
public class TbRidingGuideInfoDto extends PageDto implements Serializable {

    //主键(自动增长)
    public Integer id;
    //路书标题
    private String title          ;
    //路书海报url
    private String bill           ;
    //特色
    private String feature        ;
    //出发地
    private String startaddr      ;
    //途径点
    private String routepoint     ;
    //强度等级
    private String strengthgrade  ;
    //风景等级
    private String scenerygrad    ;
    //经验要求
    private String experience     ;
    //适宜季节
    private String season         ;
    //状态(0.删除 1.草稿 2.已发布)
    private Integer status         ;
    //浏览量
    private Integer lookcount      ;
    //骑行时间
    private String ridingtime     ;
    //车型建议
    private String vehicletype    ;
    //途径点数量
    private Integer routepointcount;
    //预计里程
    private String aboutdis       ;
    //简介
    private String introduction   ;
    //创建时间
    private Timestamp createTime    ;
    //创建人
    private String createUser    ;
    //目的地
    private String endaddr        ;
    //关键字
    private String keys           ;
    //路书类型(1.官方路书 2.自定义路书)
    private String guideType           ;
    //海报图片baseUrl
    private String baseUrl        ;
    //途径点经度
    private String lng    ;
    //途径点纬度
    private String lat       ;

    //用户id
    private Integer userId;

    //搜索字段
    private String searchVal;
    //排序类型
    private String orderByType;
    //宽
    private String width;
    //高
    private String height;
}
