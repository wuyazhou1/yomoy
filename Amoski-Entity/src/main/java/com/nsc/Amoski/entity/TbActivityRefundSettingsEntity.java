package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//活动退款设置
@Entity
@Table(name = "TB_ACTIVITY_REFUND_SETTINGS")
@Data
public class TbActivityRefundSettingsEntity  implements  Serializable{
    //活动退款设置id
    @Id
    private String id;
    //活动基础code
    @Column(name = "BASICS_ID")
    private String basicsId;
    //截止天数
    @Column(name = "CLOSING_DAY")
    private String closingDay;
    //收取服务费用
    @Column(name = "SERVICE_CHARGE")
    private String serviceCharge;

}