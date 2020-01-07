package com.nsc.Amoski.entity;




import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

//骑行路书数据
@Entity
@Table(name = "TB_RIDING_TRAVEL_PLAN")
@Data
public class TbRidingTravelPlanEntity  implements  Serializable{
    //主键(自动增长)
    @Id
    private Integer id;
    //路书图片id
    @Column(name = "IMG_ID")
    private String imgId;
    //描述
    @Column(name = "RIDING_DESC")
    private String ridingDesc;
    //经度
    @Column(name = "LGT")
    private String lgt;
    //纬度
    @Column(name = "LAT")
    private String lat;
    //创建时间
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;
    //骑行id
    @Column(name = "RIDING_ID")
    private Integer ridingId;

}