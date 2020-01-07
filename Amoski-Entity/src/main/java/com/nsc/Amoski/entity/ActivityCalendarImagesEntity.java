package com.nsc.Amoski.entity;





import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

//活动日程图片表
@Entity
@Table(name = "ACTIVITY_CALENDAR_IMAGES")
@Data
public class ActivityCalendarImagesEntity  implements  Serializable{
    //活动日程图片id
    @Id
    private Integer id;
    //活动日程图片代码
    @Column(name = "CODE")
    private String code;
    //活动门店代码
    @Column(name = "ORG_CODE")
    private String orgCode;
    //活动基础id
    @Column(name = "BASICS_ID")
    private String basicsId;
    //活动日程id
    @Column(name = "SCHEDULE_ID")
    private String scheduleId;
    //相册状态1保存，2发布
    @Column(name = "STATE")
    private String state;
    //活动项目路径
    @Column(name = "PROJECT_URL")
    private String projectUrl;
    //活动文件方法路径
    @Column(name = "FILE_PATH_URL")
    private String filePathUrl;
    //活动图片路径
    @Column(name = "FILE_PATH")
    private String filePath;
    //活动压缩图片名称
    @Column(name = "IMG_COMPRESS")
    private String imgCompress;
    //活动图片名称
    @Column(name = "FILE_NAME_URL")
    private String fileNameUrl;
    //显示文件名
    @Column(name = "SHOW_FILE_NAME")
    private String showFileName;
    //拍摄时间
    @Column(name = "UPLOAD_TIME")
    private Date uploadTime;
    //创建时间
    @Column(name = "CREATE_DATE")
    private Date createDate;

}