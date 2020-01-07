package com.nsc.Amoski.entity;



import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//活动酒店餐厅图片表
@Entity
@Table(name = "TB_HOTEL_RESTAURANT_IMAGE")
@Data
public class TbHotelRestaurantImageEntity  implements  Serializable{
    //活动酒店餐厅图片id
    @Id
    private Integer id;
    //关联类型（1[酒店]，2[餐厅]）
    @Column(name = "TYPE")
    private String type;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //关联id
    @Column(name = "RELATION_ID")
    private String relationId;
    //活动项目路径
    @Column(name = "PROJECT_URL")
    private String projectUrl;
    //活动文件方法路径
    @Column(name = "FILE_PATH_URL")
    private String filePathUrl;
    //活动文件路径
    @Column(name = "FILE_NAME_URL")
    private String fileNameUrl;

}