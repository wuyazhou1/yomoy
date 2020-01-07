package com.nsc.Amoski.dto;


import com.nsc.Amoski.entity.PageDto;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


//活动日程图片表
@Data
public class ActivityCalendarImagesDto extends PageDto implements  Serializable{
    //活动日程图片id
    private String id;
    //活动日程图片代码
    private String code;
    //活动门店代码
    private String orgCode;
    //活动基础id
    private String basicsId;
    //活动线程id
    private String scheduleId;
    //活动项目路径
    private String projectUrl;
    //活动文件方法路径
    private String filePathUrl;
    //活动压缩图片路径
    private String imgCompress;
    //活动文件路径
    private String fileNameUrl;
    //相册状态1保存，2发布
    private String state;
    //拍摄时间
    private String uploadTime;
    //显示文件名
    private String showFileName;
    //创建时间
    private String createDate;
    //活动图片路径
    private String filePath;

}

