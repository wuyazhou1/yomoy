package com.nsc.Amoski.dto;


import lombok.Data;

import java.io.Serializable;

//骑行信息详情表
@Data
public class RidingInfoDetailDto  implements  Serializable{
    //主键(自动增长)
    private Integer id;
    //骑行id
    private Integer ridingId;
    //骑行总爬升
    private Integer climbHeight;
    //骑行最快速度(km/h)
    private Integer maxSpeed;
    //骑行压弯
    private Integer ridingBend;
    //0-100m最短用时(s)
    private Integer maxAcceleratedSpeed;
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

}