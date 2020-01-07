package com.nsc.Amoski.entity;


import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//活动房型图片
@Entity
@Table(name = "TB_ACTIVITY_ROOM_IMAGE")
@Data
public class TbActivityRoomImageEntity  implements  Serializable{
    //活动房型图片id
    @Id
    private Integer id;
    //房型id
    @Column(name = "ROOM_ID")
    private String roomId;
    //null
    @Column(name = "RELATION_ID")
    private String relationId;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //部门代码
    @Column(name = "ORG_CODE")
    private String orgCode;
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

