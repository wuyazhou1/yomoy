package com.nsc.Amoski.entity;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
//动态图片表
@Entity
@Table(name = "DYNAMIC_IMAGE")
@Data
@ApiModel("动态图片数据")
public class DynamicImageEntity  implements  Serializable{
    //动态图片id
    @Id
    @ApiModelProperty("动态图片id")
    private String id;
    //动态id
    @Column(name = "DYNAMIC_ID")
    @ApiModelProperty("动态id")
    private String dynamicId;
    //动态项目路径
    @Column(name = "PROJECT_URL")
    @ApiModelProperty("动态项目路径")
    private String projectUrl;
    //动态方法路径
    @Column(name = "FILE_PATH_URL")
    @ApiModelProperty("动态方法路径")
    private String filePathUrl;
    //动态图片路径
    @Column(name = "FILE_PATH")
    @ApiModelProperty("动态图片路径")
    private String filePath;
    //动态压缩图片路径
    @ApiModelProperty("动态压缩图片路径")
    @Column(name = "IMG_COMPRESS")
    private String imgCompress;
    //动态图片名称
    @Column(name = "FILE_NAME_URL")
    @ApiModelProperty("动态图片名称")
    private String fileNameUrl;
    @Column(name = "WIDTH")
    @ApiModelProperty("图片宽度")
    private String width;
    @Column(name = "HEIGHT")
    @ApiModelProperty("图片高度")
    private String height;
}
