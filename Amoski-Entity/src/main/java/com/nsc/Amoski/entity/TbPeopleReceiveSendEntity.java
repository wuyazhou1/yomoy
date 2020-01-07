package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//活动报名人员接送
@Entity
@Table(name = "TB_PEOPLE_RECEIVE_SEND")
@Data
public class TbPeopleReceiveSendEntity  implements  Serializable{
    //到达时间
    @Column(name = "ARRIVE_DATE")
    private Date arriveDate;
    //接送机id
    @Id
    private Integer id;
    //报名人员id
    @Column(name = "PEOPLE_ID")
    private String peopleId;
    //接送类型（1接，2送）
    @Column(name = "TYPE")
    private String type;
    //航班时间
    @Column(name = "FLIGHT_DATE")
    private Date flightDate;
    //航班号
    @Column(name = "FLIGHT_NUMBER")
    private String flightNumber;
    //出发地
    @Column(name = "PLACE_DEPARTURE")
    private String placeDeparture;
    //目的地
    @Column(name = "DESTINATION")
    private String destination;
    //机场及航站楼
    @Column(name = "DESCRIBE")
    private String describe;
    //是否接送
    @Column(name = "MEET_GIVE")
    private String meetGive;

}
