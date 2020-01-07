package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//活动订单购物车表
@Entity
@Table(name = "TB_ACTIVITY_ORDER_CART")
@Data
public class TbActivityOrderCartEntity  implements  Serializable{
    //购物车id
    @Id
    private String id;
    //购物车代码
    @Column(name = "CODE")
    private String code;
    //1以提交，2未付款，3已付款，4提交审核退款，已退款
    @Column(name = "STATE")
    private String state;
    //订单总金额
    @Column(name = "PRIMITIVE_MONEY_SUM")
    private String primitiveMoneySum;
    //订单总人数
    @Column(name = "SUM_COUNT")
    private String sumCount;
    //创建人
    @Column(name = "CREATE_NAME")
    private String createName;
    //创建时间
    @Column(name = "CREATE_DATA")
    private String createData;
    //修改人
    @Column(name = "UPDATE_NAME")
    private String updateName;
    //修改时间
    @Column(name = "UPDATE_DATE")
    private String updateDate;

}

