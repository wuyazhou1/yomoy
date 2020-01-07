package com.nsc.Amoski.entity;




import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//酒店房型表
@Entity
@Table(name = "TB_ACTIVITY_HOTEL_ROOM")
@Data
public class TbActivityHotelRoomEntity  implements  Serializable{
    //床型id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_ACTIVITY_HOTEL_ROOM_SEQUENCE")
    private Integer id;
    //酒店id
    @Column(name = "HOTEL_ID")
    private String hotelId;
    //型号(1[大床房]2,[中等床房],3[小床房])
    @Column(name = "HOTEL_TYPE")
    private String hotelType;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //部门代码
    @Column(name = "ORG_CODE")
    private String orgCode;
    //数量
    @Column(name = "NUMBER_COUNT")
    private String numberCount;
    //价格
    @Column(name = "PRICE")
    private String price;
    //设施
    @Column(name = "FACILITIES")
    private String facilities;
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

    //活动房型图片
    @Transient
    public List<TbActivityRoomImageEntity> tbActivityRoomImageEntity;
}