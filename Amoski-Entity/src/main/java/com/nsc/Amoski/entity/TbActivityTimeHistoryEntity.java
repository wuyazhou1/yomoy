package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//活动时辰安排表
@Entity
@Table(name = "TB_ACTIVITY_TIME_HISTORY")
@Data
public class TbActivityTimeHistoryEntity  implements  Serializable{

    //是否验票
    @Column(name = "INSPECT_TICKET")
    private String inspectTicket;
    //活动时辰安排id
    @Id
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动日程安排id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId;
    //开始时间
    @Column(name = "START_TIME")
    private String startTime;
    //结束时间
    @Column(name = "STOP_TIME")
    private String stopTime;
    //描述类型
    @Column(name = "INTRODUCE_TYPE")
    private String introduceType;
    //描述
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

}
