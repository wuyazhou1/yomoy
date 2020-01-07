package com.nsc.Amoski.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

//用户骑行信息表
@Data
public class RidingInfoDto  implements  Serializable{




    //主键(自动增长)
    private Integer id;
    //起点
    private String startPosition;
    //途径点(多个,分隔)
    private String passPosition;
    //终点
    private String endPosition;
    //创建时间
    private Timestamp createTime;
    //创建人
    private String createUser;
    //用户id
    private Integer memberId;
    //骑行文件数据路径
    private String ridingFileUrl;
    //文件基础路径
    private String baseUrl;
    //修改时间
    private Timestamp updateTime;
    //修改人
    private String updateUser;
    //总路程(km)
    private Double totalDistance;
    //骑行总用时(h)
    private String totalTime;
    //骑行平均速度(km/h)
    private String averageSpeed;

    //骑行结束轨迹图url
    private String trackImgUrl;

    //小图路径
    public String smallImgUrl;
    //图片
    public String imgUrl;
    //基础路径
    public String imgBaseUrl;
    //骑行数据绑定车辆id
    private String ridingVehicleId;

    //骑行结束背景图
    private String ridingEndBackgroudImg;


    //骑行所有总路程
    private Double allTotalDis;
    //总时长
    private Integer allTotalTime;
    //骑行次数
    private Integer allRidingCount;
    //月份骑行最远
    private Double maxDis;
    //骑行月份
    private String ridingMonth;


    //详情数据字段

    //骑行id
    private Integer ridingId;
    //骑行总爬升
    private Double climbHeight;
    //骑行最快速度(km/h)
    private Double maxSpeed;
    //骑行压弯
    private Double ridingBend;
    //0-100m最短用时(s)
    private Double maxAcceleratedSpeed;
    //急刹次数
    private Integer emergencyBrakeTime;
    //打点次数
    private Integer punchPoint;
    //拍照上传次数
    private Integer photoTime;
    //骑行环境污染程度
    private String degreePollution;
    //全程最高pm2.5值
    private String pmTwoFive;
    //全程最高湿度
    private String humidity;

    //查询条件
    private String queryDate;
    //默认半年  1:一年
    private String queryType;

    private String fileUrl;

    //排名 地区
    private String areaRank;
    //排名 全国
    private String countryRank;


    //骑行按月份统计数据
    private List<RidingInfoDto> list;

}