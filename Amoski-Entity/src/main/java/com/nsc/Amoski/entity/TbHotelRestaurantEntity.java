package com.nsc.Amoski.entity;


import javax.persistence.*;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

//活动酒店餐厅表
@Entity
@Table(name = "TB_HOTEL_RESTAURANT")
@Data
public class TbHotelRestaurantEntity  implements  Serializable{
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private Date updateDate;
    //餐厅id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator="cach_code_seq_gen")
    @SequenceGenerator(name = "cach_code_seq_gen", sequenceName = "TB_HOTEL_RESTAURANT_SEQUENCE")
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动日程id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId;
    //餐厅排序id
    @Column(name = "ORDER_ID")
    private String orderId;
    //餐厅名称
    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;
    //特色
    @Column(name = "CHARACTERISTIC")
    private String characteristic;
    //金额
    @Column(name = "PRICE")
    private String price;
    //桌数
    @Column(name = "TABLE_NUMBER")
    private String tableNumber;
    //小计
    @Column(name = "SUM")
    private String sum;
    //联系人
    @Column(name = "CONTACTS")
    private String contacts;
    //电话
    @Column(name = "CONTACTS_TEL")
    private String contactsTel;
    //菜单
    @Column(name = "MENU")
    private String menu;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private Date createData;

    //活动酒店餐厅图片表
    @Transient
    private List<TbHotelRestaurantImageEntity> tbHotelRestaurantImageEntity;
}