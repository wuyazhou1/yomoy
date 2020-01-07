package com.nsc.Amoski.entity;


import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//人员住宿表
@Entity
@Table(name = "TB_PEOPLE_PUT_UP")
@Data
public class TbPeoplePutUpEntity  implements  Serializable{
    //住宿id
    @Id
    private Integer id;
    //人员id
    @Column(name = "PEOPLE_ID")
    private String peopleId;
    //行程天数
    @Column(name = "DAYS_OF_TRAVEL")
    private String daysOfTravel;
    //酒店名称
    @Column(name = "HOTEL_NAME")
    private String hotelName;
    //房型
    @Column(name = "ROOM_TYPE")
    private String roomType;
    //房号
    @Column(name = "ROOM_NUMBER")
    private String roomNumber;
    @Transient
    private List<Map<String,Object>> hotelNameMap;
    @Transient
    private List<Map<String,Object>> roomTypeMap;
    @Transient
    private Map<String,Object> option;
}