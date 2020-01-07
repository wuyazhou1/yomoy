package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//菜单表
@Entity
@Table(name = "T_MENU")
@Data
public class TMenuEntity  implements  Serializable{
    //主键（自动生成）
    @Id
    public String id;
    //代码
    @Column(name = "CODE")
    public String code;
    //上级菜单
    @Column(name = "PARENT_CODE")
    public String parentCode;
    //菜单名称
    @Column(name = "MENU_NAME")
    public String menuName;
    //排序编号
    @Column(name = "ORDER_CODE")
    public String orderCode;
    //菜单连接
    @Column(name = "MENU_URL")
    public String menuUrl;
    //创建人
    @Column(name = "CREATE_NAME")
    public String createName;
    //创建时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date createDate;
    //修改人
    @Column(name = "UPDATE_NAME")
    public String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date updateDate;
    //备注
    @Column(name = "REMARK")
    public String remark;
}