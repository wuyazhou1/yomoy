package com.nsc.Amoski.entity;

import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

//活动发票表
@Entity
@Table(name = "TB_CTIVITY_INVOICE")
@Data
public class TbCtivityInvoiceEntity  implements  Serializable{
    //发票id
    @Id
    private Integer id;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //发票名称
    @Column(name = "NAME_INVOICE")
    private String nameInvoice;
    //票价
    @Column(name = "TICKET_PRICE")
    private BigDecimal ticketPrice;
    //数量
    @Column(name = "NUMBER_COUNT")
    private Integer numberCount;
    //描述
    @Column(name = "DESCRIBE")
    private String describe;
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