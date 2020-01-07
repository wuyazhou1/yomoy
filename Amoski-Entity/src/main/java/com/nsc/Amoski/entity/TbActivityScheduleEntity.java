package com.nsc.Amoski.entity;

import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



//活动日程安排表
@Entity
@Table(name = "TB_ACTIVITY_SCHEDULE")
@Data
public class TbActivityScheduleEntity  implements  Serializable{

    //当前天数
    @Column(name = "DAYS_STATISTICS")
    private String daysStatistics;
    //日程id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_SCHEDULE_SEQUENCE")
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //部门代码
    @Column(name = "ORG_CODE")
    private String orgCode;
    //出发地
    @Column(name = "PLACE_DEPARTURE")
    private String placeDeparture;
    //目的地
    @Column(name = "DESTINATION")
    private String destination;
    //介绍
    @Column(name = "INTRODUCE")
    private String introduce;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private Date createData;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    //活动时辰安排表
    @Transient
    private List<ActivityCalendarImagesEntity> activityCalendarImagesEntity;
    //活动时辰安排表
    @Transient
    private List<TbActivityTimeHistoryEntity> tbActivityTimeHistoryEntity;
    //活动线路安排表
    @Transient
    private List<TbActivityRouteEntity> tbActivityRouteEntity;
    //活动酒店表
    @Transient
    private TbActivityHotelEntity tbActivityHotelEntity;
    //活动酒店餐厅表
    @Transient
    private List<TbHotelRestaurantEntity> tbHotelRestaurantEntity;


}
