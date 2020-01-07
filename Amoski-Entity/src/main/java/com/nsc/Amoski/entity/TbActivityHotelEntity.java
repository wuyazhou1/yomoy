package com.nsc.Amoski.entity;



import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//活动酒店表
@Entity
@Table(name = "TB_ACTIVITY_HOTEL")
@Data
public class TbActivityHotelEntity  implements  Serializable{
    //活动时辰安排id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_HOTEL_SEQUENCE")
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //部门代码
    @Column(name = "ORG_CODE")
    private String orgCode;
    //活动日程安排id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId;
    //经纬度Y轴
    @Column(name = "Y_AXIS")
    private String yAxis;
    //经纬度X轴
    @Column(name = "X_AXIS")
    private String xAxis;
    //酒店名称
    @Column(name = "HOTEL_NAME")
    private String hotelName;
    //酒店政策
    @Column(name = "HOTEL_POLICY")
    private String hotelPolicy;
    //联系人
    @Column(name = "CONTACTS")
    private String contacts;
    //联系电话
    @Column(name = "CONTACTS_TEL")
    private String contactsTel;
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

    //活动酒店餐厅图片表
    @Transient
    private List<TbHotelRestaurantImageEntity> tbHotelRestaurantImageEntity;
    //酒店房型表
    @Transient
    private List<TbActivityHotelRoomEntity> tbActivityHotelRoomEntity;
}
