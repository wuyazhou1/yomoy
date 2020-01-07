package com.nsc.Amoski.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 李阳
 * @date 2019/12/19 16:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TJGIMResourceEntity implements Serializable {

    //主键
    private String id;
    //外键用户表id
    private Integer member_id;
    //媒体ID
    private String media_id;
    //文件的crc32校验码
    private Long media_crc32;
    //图片原始宽度
    private Integer width;
    //图片原始高度
    private Integer height;
    //图片格式
    private String format;
    //文件大小
    private Integer fsize;
    //音频时长
    private Long duration;
    //音频hash值
    private String hash;
    //文件所在路径
    private String filepath;
    //文件名
    private String filename;
    //时间
    private String ctime;
    //文件类型
    private String data_type;

}
