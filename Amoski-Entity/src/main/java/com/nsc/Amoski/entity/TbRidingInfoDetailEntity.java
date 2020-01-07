package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//骑行信息详情表
@Entity
@Table(name = "TB_RIDING_INFO_DETAIL")
@Data
public class TbRidingInfoDetailEntity  implements  Serializable{
    //主键(自动增长)
    @Id
    private Integer id;
    //骑行id
    @Column(name = "RIDING_ID")
    private Integer ridingId;
    //骑行总爬升
    @Column(name = "CLIMB_HEIGHT")
    private Double climbHeight;
    //骑行最快速度(km/h)
    @Column(name = "MAX_SPEED")
    private Double maxSpeed;
    //骑行压弯
    @Column(name = "RIDING_BEND")
    private Double ridingBend;
    //0-100m最短用时(s)
    @Column(name = "MAX_ACCELERATED_SPEED")
    private Double maxAcceleratedSpeed;
    //急刹次数
    @Column(name = "EMERGENCY_BRAKE_TIME")
    private Integer emergencyBrakeTime;
    //打点次数
    @Column(name = "PUNCH_POINT")
    private Integer punchPoint;
    //拍照上传次数
    @Column(name = "PHOTO_TIME")
    private Integer photoTime;
    //骑行环境污染程度
    @Column(name = "DEGREE_POLLUTION")
    private String degreePollution;
    //全程最高pm2.5值
    @Column(name = "PM_TWO_FIVE")
    private String pmTwoFive;
    //全程最高湿度
    @Column(name = "HUMIDITY")
    private String humidity;

}