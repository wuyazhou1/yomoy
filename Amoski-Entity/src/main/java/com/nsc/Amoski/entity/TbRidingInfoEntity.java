package com.nsc.Amoski.entity;
import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

//用户骑行信息表
@Entity
@Table(name = "TB_RIDING_INFO")
@Data
public class TbRidingInfoEntity  implements  Serializable{

    //主键(自动增长)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_RIDING_INFO_SEQUENCE")
    private Integer id;
    //起点
    @Column(name = "START_POSITION")
    private String startPosition;
    //途径点(多个,分隔)
    @Column(name = "PASS_POSITION")
    private String passPosition;
    //终点
    @Column(name = "END_POSITION")
    private String endPosition;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //用户id
    @Column(name = "MEMBER_ID")
    private Integer memberId;
    //骑行文件数据路径
    @Column(name = "RIDING_FILE_URL")
    private String ridingFileUrl;
    //文件基础路径
    @Column(name = "BASE_URL")
    private String baseUrl;
    //修改时间
    @Column(name = "UPDATE_TIME")
    private Timestamp updateTime;
    //修改人
    @Column(name = "UPDATE_USER")
    private String updateUser;
    //总路程(km)
    @Column(name = "TOTAL_DISTANCE")
    private Double totalDistance;
    //骑行总用时(h)
    @Column(name = "TOTAL_TIME")
    private String totalTime;
    //骑行平均速度(km/h)
    @Column(name = "AVERAGE_SPEED")
    private String averageSpeed;
    //骑行结束轨迹图url
    @Column(name = "TRACK_IMG_URL")
    private String trackImgUrl;
    //骑行数据绑定车辆id
    @Column(name = "RIDNG_vehicle_id")
    private String ridingVehicleId;
    //骑行结束背景图
    @Column(name = "riding_end_backgroud_img")
    private String ridingEndBackgroudImg;
}